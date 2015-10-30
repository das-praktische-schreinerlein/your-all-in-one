/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.core.node;
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
import de.yaio.datatransfer.importer.parser.Parser;

/** 
 * bean with Node-data (as base) and belonging businesslogic
 * 
 * @FeatureDomain                DataDefinition Persistence BusinessLogic
 * @package                      de.yaio.core.node
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BaseNode implements BaseData, MetaData, SysData, 
    DescData, BaseWorkflowData, StatData {
    
    // Validator
    protected static ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();

    protected static SysDataService sysDataService = SysDataServiceImpl.getInstance();
    protected static MetaDataService metaDataService = MetaDataServiceImpl.getInstance();
    protected static BaseNodeService nodeService = BaseNodeService.getInstance();
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

    /** 
     * dummy classname for JSON-Exporter
     **/
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
    public BaseNodeService getBaseNodeService() {
        return nodeService;
    }

    /**
     * @return                       the {@link BaseNode#baseNodeDBService}
     */
    @XmlTransient
    @JsonIgnore
    public BaseNodeDBService getBaseNodeDBService() {
        return baseNodeDBService;
    }
    /**
     * @param newBaseNodeDBService   the {@link BaseNode#baseNodeDBService} to set
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
        if (workflowState == null || workflowState == WorkflowState.NOWORKFLOW) {
            return this.getBaseNodeService().getWorkflowState(this);
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
     * @return                       true/false
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
     * @return                       true/false
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
     * @return                       true/false
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
     * @return                       true/false
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
     * @return                       true/false
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
     * @return                       true/false
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
     * initialize the Children from database (childNodes and childNodesByNameMapMap)
     * recursivly
     * @FeatureDomain                Persistence
     * @FeatureResult                updates memberfields childNodes
     * @FeatureResult                updates memberfields childNodesByNameMapMap
     * @FeatureKeywords              Persistence
     * @param pRecursionLevel        how many recursion-level will be read from DB
     */
    public void initChildNodesFromDB(final int pRecursionLevel) {
        this.getBaseNodeService().initChildNodesFromDB(this, pRecursionLevel);
    }

    /** 
     * initialize the Children from database (childNodes and childNodesByNameMapMap)
     * for all parents
     * @FeatureDomain                Persistence
     * @FeatureResult                updates memberfields childNodes otf the parents
     * @FeatureResult                updates memberfields childNodesByNameMapMap of the parents
     * @FeatureKeywords              Persistence
     */
    public void initChildNodesForParentsFromDB() {
        List<BaseNode> parentHierarchy = this.getBaseNodeService().getParentHierarchy(this);
        for (BaseNode parent : parentHierarchy) {
            parent.initChildNodesFromDB(0);
        }
    }

    /** 
     * saves the children to database (childNodes) recursivly<br>
     * check if entityManger contains objects<br>
     * if not: do persist
     * if yes or flgForceMerge is set: do merge
     * @FeatureDomain                Persistence
     * @FeatureResult                saves memberfields childNodes to database
     * @FeatureKeywords              Persistence
     * @param pRecursionLevel        how many recursion-level will be saved to DB (0 = only my children)
     * @param flgForceMerge          force merge not persists
     * @throws Exception             ioExceptions possible
     */
    public void saveChildNodesToDB(final int pRecursionLevel, final boolean flgForceMerge) throws Exception {
        this.getBaseNodeDBService().saveChildNodesToDB(this, pRecursionLevel, flgForceMerge);
    }

    /** 
     * remove the children from database recursivly
     * @FeatureDomain                Persistence
     * @FeatureResult                deletes all children with this,.getSysUID recursivly from db
     * @FeatureKeywords              Persistence
     */
    public void removeChildNodesFromDB() {
        this.getBaseNodeDBService().removeChildNodesFromDB(this);
    }

    //####################
    // Hierarchy-functions
    //####################
    public void initChildNodesByNameMap() {
        Set<BaseNode> childSet = this.getChildNodes();
        for (BaseNode child : childSet) {
            this.getChildNodesByNameMap().put(child.getIdForChildByNameMap(), child);
        }
    }

    @Override
    public void setParentNode(final BaseNode parentNode) {
        getBaseNodeService().setParentNode(this, parentNode, true);
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
            return getSysUID();
        }
        return getName();
    }

    @Override
    public void addChildNode(final DataDomain childNode) {
        this.getBaseNodeService().addChildNode(this, childNode);
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
            && (getChildNodesByNameMap().get(childNode.getIdForChildByNameMap()) != null)) {
            return true;
        }
    
        return false;
    }

    //####################
    // Recalc-functions
    //####################
    
    @Override
    public void recalcData(final int recursionDirection) throws Exception {
        getBaseNodeService().recalcData(this, recursionDirection);
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
        return getBaseNodeService().getDataBlocks4CheckSum(this);
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
    public boolean isWFStatus(final String state) {
        return getBaseNodeService().isWFStatus(state);
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        return getBaseNodeService().isWFStatusDone(state);
    }

    @Override
    public boolean isWFStatusOpen(final String state) {
        return getBaseNodeService().isWFStatusOpen(state);
    }

    @Override
    public boolean isWFStatusCanceled(final String state) {
        return getBaseNodeService().isWFStatusCanceled(state);
    }
    
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
        return getBaseNodeService().getNameForLogger(this);
    }
    
    @Override
    @XmlTransient
    @JsonIgnore
    public String getWorkingId() {
        return getBaseNodeService().getWorkingId(this);
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getParentNameHirarchry(final String pdelimiter, 
                                         final boolean directionForward) {
        return getBaseNodeService().getParentNameHirarchie(this, pdelimiter, directionForward);
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
