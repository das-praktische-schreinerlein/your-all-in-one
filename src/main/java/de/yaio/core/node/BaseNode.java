/**
 * <h4>FeatureDomain:</h4>
 *     Collaboration
 *
 * <h4>FeatureDescription:</h4>
 *     software for projectmanagement and documentation
 * 
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.core.node;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.yaio.core.datadomain.BaseData;
import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.DescData;
import de.yaio.core.datadomain.MetaData;
import de.yaio.core.datadomain.StatData;
import de.yaio.core.datadomain.SysData;
import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.dbservice.BaseNodeDBService;
import de.yaio.core.dbservice.BaseNodeDBServiceImpl;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;
import de.yaio.datatransfer.importer.parser.Parser;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 *     Persistence
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     bean with Node-data (as base) and belonging businesslogic
 * 
 * @package de.yaio.core.node
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BaseNode implements BaseData, MetaData, SysData, 
    DescData, BaseWorkflowData, StatData {
    
    // Validator
    protected static ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();

    protected static SysDataService sysDataService = new SysDataServiceImpl();
    protected static MetaDataService metaDataService = new MetaDataServiceImpl();
    protected static NodeService nodeService = new BaseNodeService();
    protected static BaseNodeDBService baseNodeDBService = BaseNodeDBServiceImpl.getInstance();
    
    // Logger
    private static final Logger LOGGER = Logger.getLogger(BaseNode.class);

    /**
     * next position in list
     */
    @Transient
    @XmlTransient
    @JsonIgnore
    protected int curSortIdx = 0;

    //####################
    // Hierarchy-functions
    //####################
    @Transient
    @XmlTransient
    @JsonIgnore
    protected Map<String, DataDomain> childNodesByNameMapMap = new LinkedHashMap <String, DataDomain>();

    /** dummy classname for JSON-Exporter**/
    private String className;

    /**
     * flag to indicate that item hat to be update
     */
    @Transient
    @XmlTransient
    @JsonIgnore
    private boolean flgForceUpdate = false;
    
    private Long importTmpId;
    
    /**
     */
    @Size(min = 1, max = 64000)
    @Transient
    private String fullSrc;

    /**
     */
    @Size(max = 64000)
    private String srcName;

    /**
     */
    @NotNull
    @Size(min = 0, max = 2000)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_NAME + "*)?", 
             message = "name can only contain characters.")
    private String name;

    /**
     */
    @Min(0L)
    @Max(50L)
    private Integer ebene;

    /**
     */
    @Size(max = 64000)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_DESC + "*)?", 
             message = "desc can only contain characters.")
    private String nodeDesc;

    /**
     */
    @Size(max = 5)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_LAYOUTCOMMAND + "*)?", 
             message = "docLayoutTagCommand can only contain characters.")
    private String docLayoutTagCommand;

    /**
     */
    @Size(max = 255)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_STYLECLASS + "*)?", 
             message = "docLayoutAddStyleClass can only contain characters.")
    private String docLayoutAddStyleClass;

    /**
     */
    @Size(max = 80)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_SHORTNAME + "*)?", 
             message = "docLayoutShortName can only contain characters.")
    private String docLayoutShortName;

    /**
     */
    @Size(max = 5)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_FLAG + "*)?", 
             message = "docLayoutFlgCloseDiv can only contain characters.")
    private String docLayoutFlgCloseDiv;

    /**
     */
    @Id
    @Size(max = 80)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_UID + "*)?", 
             message = "sysUID can only contain characters.")
    private String sysUID;

    /**
     */
    @Size(max = 50)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_CHECKSUM + "*)?", 
             message = "sysCurChecksum can only contain characters.")
    private String sysCurChecksum;

    /**
     */
    @Min(0L)
    private Integer sysChangeCount;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sysCreateDate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sysChangeDate;

    /**
     */
    @Size(max = 10)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_PRAEFIX + "*)?", 
             message = "metaNodePraefix can only contain characters.")
    private String metaNodePraefix;

    /**
     */
    @Size(max = 10)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_ID + "*)?", 
             message = "metaNodeNummer can only contain digits.")
    private String metaNodeNummer;

    /**
     */
    @Size(max = 255)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_TAGS + ")*?", 
             message = "metaNodeTypeTags can only contain characters.")
    private String metaNodeTypeTags;

    /**
     */
    @Size(max = 255)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_TAGS + ")*?", 
             message = "metaNodeSubTypeTags can only contain characters.")
    private String metaNodeSubTypeTags;

    /**
     * first date of planed work
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planStart;

    /**
     * last date of planed work
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planEnde;
    
    /**
     * hours of planed work
     */
    @Min(0L)
    private Double planAufwand;

    /**
     */
    @Size(max = 30)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_TASK + ")*?", 
             message = "planTask can only contain characters.")
    private String planTask;

    /**
     * first date of planed work calced from my data with planDependencies: planStart
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planCalcStart;

    /**
     * last date of planed work calced from my data with planDependencies: planEnde
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planCalcEnde;

    /**
     * checksum to cache planCalcs
     */
    @Size(max = 50)
    private String planCalcCheckSum;
    
    /**
     * first date of planed work of me and all children: planStart
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planChildrenSumStart;

    /**
     * last date of planed work of me and all children: planEnde
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date planChildrenSumEnde;

    /**
     * summary of me and all children: planAufwand
     */
    @Min(0L)
    private Double planChildrenSumAufwand;

    /**
     * planned duration of the task
     */
    @Min(0L)
    private Integer planDuration;
    
    
    /**
     * measure for planDuration
     */
    @Enumerated(EnumType.STRING)
    private DurationMeasure planDurationMeasure;

    /**
     * type of predecessor
     */
    @Enumerated(EnumType.STRING)
    private PredecessorType planPredecessorType;

    /**
     * predecessor
     */
    @ManyToOne
    private BaseNode planPredecessor;
    
    /**
     * dependency to the predecessor
     */
    @Enumerated(EnumType.STRING)
    private PredecessorDependencieType planPredecessorDependencieType;

    /**
     * shift to the predecessor
     */
    private Integer planPredecessorShift;

    /**
     * measure of predecessorShift
     */
    @Enumerated(EnumType.STRING)
    private DurationMeasure planPredecessorShiftMeasure;

    /**
     * current stand of work
     */
    @Min(0L)
    @Max(100L)
    private Double istStand;

    /**
     * current date of first work
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date istStart;

    /**
     * current date of last work
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date istEnde;

    /**
     * use hours of work
     */
    @Min(0L)
    private Double istAufwand;

    /**
     */
    @Size(max = 30)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_TASK + ")*?", 
             message = "istTask can only contain characters.")
    private String istTask;

    /**
     * summary of me and all children: istStand
     */
    @Min(0L)
    @Max(100L)
    private Double istChildrenSumStand;

    /**
     * first date of work of me and all children: istEnde
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date istChildrenSumStart;

    /**
     * last date of work of me and all children: istEnde
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date istChildrenSumEnde;

    /**
     * summary of me and all children: istAufwand
     */
    @Min(0L)
    private Double istChildrenSumAufwand;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "parentNode", fetch = FetchType.LAZY)
    @Transient
    @XmlTransient
    @JsonIgnore
    private Set<BaseNode> childNodes = new LinkedHashSet<BaseNode>();

    /**
     * count of my direct children: for caching the count of childNodes
     */
    @Min(0)
    private Integer statChildNodeCount;

    /**
     * count of my children with workflow (tasks, events...)
     */
    @Min(0)
    private Integer statWorkflowCount;

    /**
     * count of my children with open workflow (tasks, events...)
     */
    @Min(0)
    private Integer statWorkflowTodoCount;

    /**
     */
    @ManyToOne
    private BaseNode parentNode;
    
    /**
     */
    @Size(max = 255)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_STATE + "*)?", 
             message = "state can only contain characters.")
    private String state;

    /**
     */
    @Size(max = 255)
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_TYPE + "*)?", 
             message = "type can only contain characters.")
    private String type;

    /**
     * summary of all children workflowstate with the highest priority
     */
    @Enumerated
    private WorkflowState workflowState;

    /**
     * position in list
     */
    @Min(0L)
    private Integer sortPos;

    public BaseNode() {
        super();
    }

    @XmlTransient
    @JsonIgnore
    public SysDataService getSysDataService() {
        return sysDataService;
    }
    @XmlTransient
    @JsonIgnore
    public static SysDataService getConfiguredSysDataService() {
        return sysDataService;
    }
    @XmlTransient
    @JsonIgnore
    public static final void setSysDataService(final SysDataService newSysDataService) {
        sysDataService = newSysDataService;
    }
    @XmlTransient
    @JsonIgnore
    public static MetaDataService getConfiguredMetaDataService() {
        return metaDataService;
    }
    @XmlTransient
    @JsonIgnore
    public MetaDataService getMetaDataService() {
        return metaDataService;
    }

    @XmlTransient
    @JsonIgnore
    public static final void setMetaDataService(final MetaDataService newMetaDataService) {
        metaDataService = newMetaDataService;
    }
    @XmlTransient
    @JsonIgnore
    public NodeService getNodeService() {
        return nodeService;
    }

    /**
     * @return the {@link BaseNode#baseNodeDBService}
     */
    @XmlTransient
    @JsonIgnore
    public BaseNodeDBService getBaseNodeDBService() {
        return baseNodeDBService;
    }
    /**
     * @param newBaseNodeDBService the {@link BaseNode#baseNodeDBService} to set
     */
    @XmlTransient
    @JsonIgnore
    public void setBaseNodeDBService(final BaseNodeDBService newBaseNodeDBService) {
        baseNodeDBService = newBaseNodeDBService;
    }

    
    
    
    /**
     * summary of all children workflowstate with the highest priority
     */
    public WorkflowState getWorkflowState() {
        if (workflowState == null) {
            return WorkflowState.NOWORKFLOW;
        }
        return workflowState;
    };
    public void setWorkflowState(final WorkflowState istState) {
        workflowState = istState;
    };

    //
    // checks 
    //
    //
    
    @Override
    public Set<ConstraintViolation<BaseNode>> validateMe() {
        Validator validator = validationFactory.getValidator();

        Date start = null;
        if (LOGGER.isDebugEnabled()) {
            start = new Date();
        }
        Set<ConstraintViolation<BaseNode>> violations = validator.validate(this);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("validation duration=" + ((new Date()).getTime() - start.getTime()) 
                            + "ms for" + this.getNameForLogger());
        }
        
        return violations;
    }

    /**
     * checks if planStart <= planEnde
     * @return true/false
     */
    @AssertTrue(message = "planStart must be <= planEnde") 
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isPlanValidRange() {
        if (getPlanStart() != null && getPlanEnde() != null) {
            if (LOGGER.isDebugEnabled() && !(getPlanEnde().compareTo(getPlanStart()) >= 0)) {
                LOGGER.error("planStart must be <= planEnde:" + this.getNameForLogger() 
                                + " ist=" + getIstStart() 
                                + " istEnde=" + getIstEnde());
            }
            return getPlanEnde().compareTo(getPlanStart()) >= 0;
        }
        return true;
    }    

    /**
     * checks if getPlanStart() <= CONST_MAXDATE && >= CONST_MINDATE
     * @return true/false
     */
    @AssertTrue(message = "planStart is out of range") 
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isPlanStartValid() {
        if (getPlanStart() != null) {
            return (getPlanStart().compareTo(CONST_MINDATE) >= 0) 
                    && (getPlanStart().compareTo(CONST_MAXDATE) <= 0);
        }
        return true;
    }    
    
    /**
     * checks if getPlanEnde() <= CONST_MAXDATE && >= CONST_MINDATE
     * @return true/false
     */
    @AssertTrue(message = "planEnde is out of range") 
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isPlanEndeValid() {
        if (getPlanEnde() != null) {
            return (getPlanEnde().compareTo(CONST_MINDATE) >= 0) 
                    && (getPlanEnde().compareTo(CONST_MAXDATE) <= 0);
        }
        return true;
    }    

    /**
     * checks if istStart <= istEnde
     * @return true/false
     */
    @AssertTrue(message = "istStart must be <= istEnde") 
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isIstValidRange() {
        if (getIstStart() != null && getIstEnde() != null) {
            if (LOGGER.isDebugEnabled() && !(getIstEnde().compareTo(getIstStart()) >= 0)) {
               LOGGER.error("istStart must be <= istEnde:" + this.getNameForLogger() 
                               + " ist=" + getIstStart() 
                               + " istEnde=" + getIstEnde());
            }
            return getIstEnde().compareTo(getIstStart()) >= 0;
        }
        return true;
    }    

    /**
     * checks if istStart <= CONST_MAXDATE && >= CONST_MINDATE
     * @return true/false
     */
    @AssertTrue(message = "istStart is out of range") 
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isIstStartValid() {
        if (getIstStart() != null) {
            return (getIstStart().compareTo(CONST_MINDATE) >= 0) 
                    && (getIstStart().compareTo(CONST_MAXDATE) <= 0);
        }
        return true;
    }    
    
    /**
     * checks if istEnde <= CONST_MAXDATE && >= CONST_MINDATE
     * @return true/false
     */
    @AssertTrue(message = "istEnde is out of range")
    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isIstEndeValid() {
        if (getIstEnde() != null) {
            return (getIstEnde().compareTo(CONST_MINDATE) >= 0) 
                    && (getIstEnde().compareTo(CONST_MAXDATE) <= 0);
        }
        return true;
    }    
    
    //####################
    // persistence-functions
    //####################
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     initialize the Children from database (childNodes and childNodesByNameMapMap)
     *     recursivly
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberfields childNodes
     *     <li>updates memberfields childNodesByNameMapMap
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param pRecursionLevel - how many recursion-level will be read from DB
     */
    public void initChildNodesFromDB(final int pRecursionLevel) {
        int recursionLevel = pRecursionLevel;
        // clear the children
        this.childNodes.clear();
        this.childNodesByNameMapMap.clear();
        
        // read my childNodes
        List<BaseNode> tmpChildNodes = getBaseNodeDBService().findChildNodes(this.getSysUID());
        
        // set new level if it is not -1
        recursionLevel = recursionLevel > 0 ? recursionLevel-- : recursionLevel;

        // interate children
        for (BaseNode childNode : tmpChildNodes) {
            // add to childrenMaps
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("initChildNodesFromDB add to " + this.getNameForLogger() 
                           + " child:" + childNode.getNameForLogger());
            }
            this.addChildNode(childNode);
            
            // check recursionLevel
            if ((recursionLevel == NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN) 
                || (recursionLevel > 0)) {
                // recurse
                childNode.initChildNodesFromDB(recursionLevel);
            }
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     initialize the Children from database (childNodes and childNodesByNameMapMap)
     *     for all parents
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>updates memberfields childNodes otf the parents
     *     <li>updates memberfields childNodesByNameMapMap of the parents
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     */
    public void initChildNodesForParentsFromDB() {
        List<BaseNode> parentHierarchy = this.getParentHierarchy();
        for (BaseNode parent : parentHierarchy) {
            parent.initChildNodesFromDB(0);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     saves the children to database (childNodes) recursivly<br>
     *     check if entityManger contains objects<br>
     *     if not: do persist
     *     if yes or flgForceMerge is set: do merge
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>saves memberfields childNodes to database
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param pRecursionLevel - how many recursion-level will be saved to DB (0 = only my children)
     * @param flgForceMerge - force merge not persists
     * @throws Exception - ioExceptions possible
     */
    public void saveChildNodesToDB(final int pRecursionLevel, final boolean flgForceMerge) throws Exception {
        // set new level if it is not -1
        int recursionLevel = pRecursionLevel;
        recursionLevel = recursionLevel > 0 ? recursionLevel-- : recursionLevel;

        // interate children
        for (BaseNode childNode : this.getChildNodes()) {
            // validate data
//            if (childNode.getMetaNodeNummer() == null) {
//                childNode.initMetaData();
//            }
            if (childNode.getSysUID() == null) {
                childNode.initSysData();
            }

            // persist to DB
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("persistChildNodesToDB from " + this.getNameForLogger() 
                               + " child:" + childNode.getNameForLogger());
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("childNode:" + childNode.getName() + " pos: " + childNode.getSortPos());
                }
                
                // check if persist or merge
                if (entityManager().contains(childNode) || flgForceMerge) {
                    childNode.merge();
                } else {
                    childNode.persist();
                }
                //CHECKSTYLE.OFF: IllegalCatch - Much more readable than catching x exceptions
            } catch (Exception ex) {
                //CHECKSTYLE.ON: IllegalCatch
                LOGGER.error("errors while saving childnode for '" 
                                + sysUID + "':", ex);
                LOGGER.error("error saving node '" 
                                + childNode);
                throw ex;
            }            
//            boolean flgOK = true;
//            try {
//                childNode.persist();
//            } catch (Exception ex) {
//                LOGGER.error("persistChildNodesToDB error for childnode " 
//                           + childNode.getMetaNodePraefix() 
//                           + "," + childNode.getMetaNodeNummer() 
//                           + " SysUID: " + childNode.getSysUID() 
//                           + " Name: " + childNode.getName() 
//                           + " ex:" + ex);
////                LOGGER.error("persistChildNodesToDB error for parent " 
////                        + this.getSysUID() + " Name: " + this.getName());
////                LOGGER.error("persistChildNodesToDB error for childnodedetails " + childNode.getNameForLogger());
////                LOGGER.error("persistChildNodesToDB error for parentdetails " + this.getNameForLogger());
//                flgOK = false;
////                throw new Exception(ex);
//            }
            
            // check recursionLevel
            if ((recursionLevel == NodeService.CONST_DB_RECURSIONLEVEL_ALL_CHILDREN) 
                 || (recursionLevel > 0)) {
                // recurse
//                if (flgOK)
                childNode.saveChildNodesToDB(recursionLevel, flgForceMerge);
            }
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     remove the children from database recursivly
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>deletes all children with this,.getSysUID recursivly from db
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     */
    public void removeChildNodesFromDB() {
        // interate children on db
        for (BaseNode childNode : getBaseNodeDBService().findChildNodes(this.getSysUID())) {
            // persist to DB
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("removeChildNodesFromDB from " + this.getNameForLogger() 
                           + " child:" + childNode.getNameForLogger());
            }
            // recurse
            childNode.removeChildNodesFromDB();
            
            // remove this child
            childNode.remove();
        }
    }

    //####################
    // Hierarchy-functions
    //####################
    public void initChildNodesByNameMap() {
        Set<BaseNode> childSet = childNodes;
        for (BaseNode child : childSet) {
            childNodesByNameMapMap.put(child.getIdForChildByNameMap(), child);
        }
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     get a List of the parent-hierarchy
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<BaseNode> - list of the parents, start with my own parent (not me)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return list of the parents, start with my own parent (not me)
     */
    @Transient
    @XmlTransient
    @JsonIgnore
    public List<BaseNode> getParentHierarchy() {
        List<BaseNode> parentHierarchy = new ArrayList<BaseNode>();
        BaseNode parent = this.getParentNode();
        while (parent != null) {
            parentHierarchy.add(parent);
            parent = parent.getParentNode();
        }
        return parentHierarchy;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     get a List of the Ids of parent-hierarchy 
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue List<String> - list of the parent-sysUIDs, start with my own parent (not me)
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @return list of the parent-sysUIDs, start with my own parent (not me)
     */
    @Transient
    @XmlTransient
    @JsonIgnore
    public List<String> getParentIdHierarchy() {
        List<String> parentIdHierarchy = new ArrayList<String>();
        for (BaseNode parent: getParentHierarchy()) {
            parentIdHierarchy.add(parent.getSysUID());
        }
        
        return parentIdHierarchy;
    }
        
    @Override
    public void setParentNode(final BaseNode parentNode) {
        getNodeService().setParentNode(this, parentNode, true);
    }
    @Override
    @XmlTransient
    @JsonIgnore
    public void setParentNodeOnly(final DataDomain parentNode) {
        this.parentNode = (BaseNode) parentNode;
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getIdForChildByNameMap() {
        if (getSrcName() != null) {
            return getSrcName();
        }
        if (getSysUID() != null) {
            return sysUID;
        }
        return getName();
    }

    @Override
    public void addChildNode(final DataDomain childNode) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("add child:" + childNode.getNameForLogger() + " to " + this.getNameForLogger());
        }
        if (childNode != null) {
            if (childNode.getSortPos() != null) {
                // preserve sortpos of the child
                if (childNode.getSortPos() > curSortIdx) {
                    // update idx
                    curSortIdx = childNode.getSortPos() + BaseNodeService.CONST_CURSORTIDX_STEP;
                }
            } else {
                // set new sortpos for the child
                childNode.setSortPos(curSortIdx);
                curSortIdx = curSortIdx + BaseNodeService.CONST_CURSORTIDX_STEP;
            }
            this.childNodesByNameMapMap.put(childNode.getIdForChildByNameMap(), childNode);
            this.childNodes.add((BaseNode) childNode);
        }
    }
    
    /**
     * <h4>FeatureDomain:</h4>
     *     Persistence
     * <h4>FeatureDescription:</h4>
     *     move the child in childlist to the sortPos
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>reorder childNodes
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Persistence
     * @param child - the child to move in list
     * @param newSortPos - the new position
     */
    public void moveChildToSortPos(final BaseNode child, final Integer newSortPos) {
        // check data
        if (child == null) {
            throw new IllegalArgumentException("child must not be null");
        }
        if (newSortPos == null) {
            throw new IllegalArgumentException("newSortPos must not be null");
        }
        if (!this.childNodes.contains(child)) {
            throw new IllegalArgumentException("child is no member of my childlist");
        }
        
        // iterate childlist and look for child
        boolean flgChildWaiting = true;
        
        // preserve the childnodes in order
        Set<BaseNode> tmpChildNodes = new LinkedHashSet<BaseNode>();
        for (BaseNode curChild : this.childNodes) {
            // add the other child
            tmpChildNodes.add(curChild);
        }
        
        // clear the  orig list
        this.childNodes.clear();
        curSortIdx = 0;
        
        // if the child moves down, then we have to realize that it is no more in list: so we have to sub the idx 
        int newPos = newSortPos.intValue();
        
        // add the childs to the list
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("I " + child.getName() + " want newPos:" + newPos);
        }
        for (BaseNode curChild : tmpChildNodes) {
            // if sortPos of curChild > newSortPos, then insert it here
            if (flgChildWaiting && curChild.getSortPos().intValue() > newPos) {
                this.addChildNode(child);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("added me " + child.getName() + " and got " + child.getSortPos().intValue());
                }
                flgChildWaiting = false;
            }
            if (child.equals(curChild) || child.getSysUID().equalsIgnoreCase(curChild.getSysUID())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("bullshit iam in List already " 
                 + curChild.getSortPos().intValue() + " at " + this.childNodes.size());
                // hey i'm already here
                }
            } else {
                // add the other child
                this.addChildNode(curChild);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("added other child:" + curChild.getName() 
                                    + " new:" + curChild.getSortPos().intValue() + " at " + this.childNodes.size());
                }
            }
            
        }

        // recalc the sortidx
        curSortIdx = 0;
        for (BaseNode curChild : this.getChildNodes()) {
            curChild.setSortPos(curSortIdx);
            curSortIdx = curSortIdx + BaseNodeService.CONST_CURSORTIDX_STEP;
        }
    }
    
    @Override
    public void setParentNode(final DataDomain parentNode) {
        setParentNode((BaseNode) parentNode);
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, DataDomain> getChildNodesByNameMap() {
        return childNodesByNameMapMap;
    }

    @Override
    public boolean hasChildNode(final DataDomain childNode) {
        if ((childNode != null) 
            && (childNodesByNameMapMap.get(childNode.getIdForChildByNameMap()) != null)) {
            return true;
        }
    
        return false;
    }

    //####################
    // Recalc-functions
    //####################
    
    @Override
    public void recalcData(final int recursionDirection) throws Exception {
        getNodeService().recalcData(this, recursionDirection);
    }
    
    @Override
    public void initMetaData() throws Exception {
        getMetaDataService().initMetaData(this);
    }

    @Override
    public void initSysData() throws Exception {
        getSysDataService().initSysData(this);
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getDataBlocks4CheckSum() throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(this.getType())
            .append(this.getState())
            .append(" name=").append(getName())
// TODO difference DB + PPL-Import  .append(" parentNode=").append((getParentNode() != null ? getParentNode().getSysUID() : null))
// TODO difference DB + PPL-Import  .append(" sortPos=").append(getSortPos())
//            .append(" ebene=").append(getEbene())
//            .append(" istStandChildrenSum=").append(getIstChildrenSumStand())
//            .append(" istStartChildrenSum=").append(getIstChildrenSumStart())
//            .append(" istEndeChildrenSum=").append(getIstChildrenSumEnde())
//            .append(" istAufwandChildrenSum=").append(getIstChildrenSumAufwand())
//            .append(" planStartChildrenSum=").append(getPlanChildrenSumStart())
//            .append(" planEndeChildrenSum=").append(getPlanChildrenSumEnde())
//            .append(" planAufwandChildrenSum=").append(getPlanChildrenSumAufwand())
            .append(" docLayoutTagCommand=").append(getDocLayoutTagCommand())
            .append(" docLayoutAddStyleClass=").append(getDocLayoutAddStyleClass())
            .append(" docLayoutShortName=").append(getDocLayoutShortName())
            .append(" docLayoutFlgCloseDiv=").append(getDocLayoutFlgCloseDiv())
            .append(" metaNodePraefix=").append(getMetaNodePraefix())
            .append(" metaNodeNummer=").append(getMetaNodeNummer())
            .append(" metaNodeTypeTags=").append(getMetaNodeTypeTags())
            .append(" metaNodeSubTypeTags=").append(getMetaNodeSubTypeTags())
            .append(" desc=").append(getNodeDesc());
        return data.toString();
    }

    //####################
    // Workflow-functions TODO - separate type+state
    //####################
    @Override
    public void setType(final String type) {
        this.type = type;
        if (BaseWorkflowData.class.isInstance(this)) {
            this.state = type;
        }
    }

    @Override
    public void setState(final String state) {
        this.state = state;
        if (BaseWorkflowData.class.isInstance(this)) {
            this.type = state;
        }
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, Object> getConfigState() {
        return BaseNodeService.CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    public boolean isWFStatus(final String state) {
        return getNodeService().isWFStatus(state);
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        return getNodeService().isWFStatusDone(state);
    }

    @Override
    public boolean isWFStatusOpen(final String state) {
        return getNodeService().isWFStatusOpen(state);
    }

    @Override
    public boolean isWFStatusCanceled(final String state) {
        return getNodeService().isWFStatusCanceled(state);
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public Map<String, WorkflowState> getConfigWorkflowState() {
        return BaseNodeService.CONST_MAP_STATE_WORKFLOWSTATE;
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public WorkflowState getWorkflowStateForState(final String state)  throws IllegalStateException {
        return WorkflowState.NOWORKFLOW;
    };

    @Override
    @XmlTransient
    @JsonIgnore
    public String getStateForWorkflowState(final WorkflowState workflowState)  throws IllegalStateException {
        return this.getState();
    };
    
    
    //####################
    // service-functions
    //####################
    public String getClassName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String toString() {
        return "ProjektNode: " + this.getSrcName() + " ID: " + this.getWorkingId() + " SRC:" + this.getFullSrc();
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getNameForLogger() {
        String nameForLogger = "sysUID_" + this.getSysUID() 
                        + "_name_" + this.getName() + "_srcName_" + this.getSrcName();
        return nameForLogger;    
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String getWorkingId() {
        String res = "UNKNOWN";
        if (this.getMetaNodeNummer() != null && (this.getMetaNodeNummer().length() > 0)) {
            res = this.getMetaNodePraefix() + this.getMetaNodeNummer();
        } else if (this.getImportTmpId() != null) {
            res = this.getImportTmpId().toString();
        }

        return res;
    }

    
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String getParentNameHirarchry(final String pdelimiter, 
                                         final boolean directionForward) {
        String parentNames = "";
        String delimiter = pdelimiter == null ? "" : pdelimiter;

        if (this.getParentNode() != null) {
            if (directionForward) {
                parentNames = this.getParentNode().getParentNameHirarchry(delimiter,
                    directionForward)
                    + delimiter
                    + this.getParentNode().getName();
            } else {
                parentNames = this.getParentNode().getName()
                    + delimiter
                    + this.getParentNode().getParentNameHirarchry(delimiter,
                        directionForward);
            }
        }

        return parentNames;
    }
    
    // TODO cache this
    @XmlTransient
    @JsonIgnore
    public int getMaxChildEbene() {
        int maxEbene = this.getEbene();

        // alle Kinder durchsuchen
        if (this.getChildNodes() != null) {
            for (BaseNode node : this.getChildNodes()) {
                int maxEbeneChild = node.getMaxChildEbene();
                if (maxEbeneChild > maxEbene) {
                    maxEbene = maxEbeneChild;
                }
            }
        }
        return maxEbene;
    }

    @Override
    public boolean isFlgForceUpdate() {
        return this.flgForceUpdate;
    }
    @Override
    public void setFlgForceUpdate(final boolean flgForceUpdate) {
        this.flgForceUpdate = flgForceUpdate;
    }
}
