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
package de.yaio.app.core.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.yaio.app.core.datadomain.*;
import de.yaio.app.core.datadomainservice.MetaDataService;
import de.yaio.app.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.app.core.datadomainservice.SysDataService;
import de.yaio.app.core.datadomainservice.SysDataServiceImpl;
import de.yaio.app.core.nodeservice.BaseNodeService;
import de.yaio.app.core.nodeservice.NodeService;
import de.yaio.app.datatransfer.importer.parser.Parser;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/** 
 * bean with Node-data (as base) and belonging businesslogic
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
@Configurable
@Entity
public class BaseNode implements BaseData, MetaData, SysData,
        DescData, BaseWorkflowData, StatData, CachedData {
    
    // Validator
    protected static ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();

    protected static SysDataService sysDataService = SysDataServiceImpl.getInstance();
    protected static MetaDataService metaDataService = MetaDataServiceImpl.getInstance();
    protected static BaseNodeService nodeService = BaseNodeService.getInstance();

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
    @Size(min = 1, max = 64000000)
    @Transient
    private String fullSrc;

    /**
     */
    @Size(max = 64000000)
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
    @Size(max = 64000000)
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
             message = "metaNodePraefix can only contain characters and numbers.")
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
    @Pattern(regexp = "(" + Parser.CONST_PATTERN_SEG_SUBTYPE + ")*?",
             message = "metaNodeSubType can only contain characters.")
    private String metaNodeSubType;

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentNode", fetch = FetchType.LAZY)
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
     * count of my children with urlRes
     */
    @Min(0)
    private Integer statUrlResCount;

    /**
     * count of my children with info
     */
    @Min(0)
    private Integer statInfoCount;

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
    @Type(type="de.yaio.app.core.datadomain.WorkflowStateUserType")
    private WorkflowState workflowState;

    /**
     * the cached parentHierarchy as ,id1,,id2,,id3,
     */
    @Size(max = 2000)
    private String cachedParentHierarchy;


    /**
     * position in list
     */
    @Min(0L)
    private Integer sortPos;

    /**
     * version
     */
    @Version
    @Column(name = "version")
    private Integer version;

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


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
     * summary of all children workflowstate with the highest priority
     */
    public WorkflowState getWorkflowState() {
        if (workflowState == null || workflowState == WorkflowState.NOWORKFLOW) {
            return this.getBaseNodeService().getWorkflowState(this);
        }
        return workflowState;
    }
    public void setWorkflowState(final WorkflowState istState) {
        workflowState = istState;
    }

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
     * @param pRecursionLevel        how many recursion-level will be read from DB
     */
    public void initChildNodesFromDB(final int pRecursionLevel) {
        this.getBaseNodeService().initChildNodesFromDB(this, pRecursionLevel);
    }

    //####################
    // Hierarchy-functions
    //####################
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
        getBaseNodeService().setParentNode(this, (BaseNode)parentNode, true);
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
    public void recalcData(final NodeService.RecalcRecurseDirection recursionDirection) {
        getBaseNodeService().recalcData(this, recursionDirection);
    }
    
    @Override
    public void initMetaData() {
        getMetaDataService().initMetaData(this);
    }

    @Override
    public void initSysData(boolean flgForceUpdate) {
        getSysDataService().initSysData(this, flgForceUpdate);
    }

    @Override
    @XmlTransient
    @JsonIgnore
    public String getDataBlocks4CheckSum() {
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

    @Override
    public void resetIstChildrenSumData() {
        this.setIstChildrenSumAufwand(null);
        this.setIstChildrenSumEnde(null);
        this.setIstChildrenSumStand(null);
        this.setIstChildrenSumStart(null);
    }

    @Override
    public void resetPlanCalcData() {
        this.setPlanCalcCheckSum(null);
        this.setPlanCalcEnde(null);
        this.setPlanCalcStart(null);
    }

    @Override
    public void resetPlanDependencieData() {
        this.setPlanDuration(null);
        this.setPlanDurationMeasure(null);
        this.setPlanPredecessor(null);
        this.setPlanPredecessorType(null);
        this.setPlanPredecessorDependencieType(null);
        this.setPlanPredecessorShift(null);
        this.setPlanPredecessorShiftMeasure(null);
    }

    @Override
    public void resetPlanChildrenSumData() {
        this.setPlanChildrenSumAufwand(null);
        this.setPlanChildrenSumEnde(null);
        this.setPlanChildrenSumStart(null);
    }

    @Override
    public void resetStatData() {
        this.setStatChildNodeCount(null);
        this.setStatInfoCount(null);
        this.setStatUrlResCount(null);
        this.setStatWorkflowCount(null);
        this.setStatWorkflowTodoCount(null);
    }

    @Override
    public void resetBaseWorkflowData() {
        this.setState(null);
        this.setWorkflowState(WorkflowState.NOWORKFLOW);
    }

    @Override
    public void resetDescData() {
        this.setNodeDesc(null);
    }

    @Override
    public void resetSysData() {
        this.setSysChangeCount(null);
        this.setSysChangeDate(null);
        this.setSysCreateDate(null);
        this.setSysCurChecksum(null);
        this.setSysUID(null);
    }

    @Override
    public void resetMetaData() {
        this.setMetaNodeNummer(null);
        this.setMetaNodePraefix(null);
    }

    @Override
    public void resetCachedData() {
        this.setCachedParentHierarchy(null);
    }

    /**
     * getter
     */
    public Long getImportTmpId() {
        return this.importTmpId;
    }

    public void setImportTmpId(Long importTmpId) {
        this.importTmpId = importTmpId;
    }

    public String getFullSrc() {
        return this.fullSrc;
    }

    public void setFullSrc(String fullSrc) {
        this.fullSrc = fullSrc;
    }

    public String getSrcName() {
        return this.srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEbene() {
        return this.ebene;
    }

    public void setEbene(Integer ebene) {
        this.ebene = ebene;
    }

    public String getNodeDesc() {
        return this.nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getDocLayoutTagCommand() {
        return this.docLayoutTagCommand;
    }

    public void setDocLayoutTagCommand(String docLayoutTagCommand) {
        this.docLayoutTagCommand = docLayoutTagCommand;
    }

    public String getDocLayoutAddStyleClass() {
        return this.docLayoutAddStyleClass;
    }

    public void setDocLayoutAddStyleClass(String docLayoutAddStyleClass) {
        this.docLayoutAddStyleClass = docLayoutAddStyleClass;
    }

    public String getDocLayoutShortName() {
        return this.docLayoutShortName;
    }

    public void setDocLayoutShortName(String docLayoutShortName) {
        this.docLayoutShortName = docLayoutShortName;
    }

    public String getDocLayoutFlgCloseDiv() {
        return this.docLayoutFlgCloseDiv;
    }

    public void setDocLayoutFlgCloseDiv(String docLayoutFlgCloseDiv) {
        this.docLayoutFlgCloseDiv = docLayoutFlgCloseDiv;
    }

    public String getSysUID() {
        return this.sysUID;
    }

    public void setSysUID(String sysUID) {
        this.sysUID = sysUID;
    }

    public String getSysCurChecksum() {
        return this.sysCurChecksum;
    }

    public void setSysCurChecksum(String sysCurChecksum) {
        this.sysCurChecksum = sysCurChecksum;
    }

    public Integer getSysChangeCount() {
        return this.sysChangeCount;
    }

    public void setSysChangeCount(Integer sysChangeCount) {
        this.sysChangeCount = sysChangeCount;
    }

    public Date getSysCreateDate() {
        return this.sysCreateDate;
    }

    public void setSysCreateDate(Date sysCreateDate) {
        this.sysCreateDate = sysCreateDate;
    }

    public Date getSysChangeDate() {
        return this.sysChangeDate;
    }

    public void setSysChangeDate(Date sysChangeDate) {
        this.sysChangeDate = sysChangeDate;
    }

    public String getMetaNodePraefix() {
        return this.metaNodePraefix;
    }

    public void setMetaNodePraefix(String metaNodePraefix) {
        this.metaNodePraefix = metaNodePraefix;
    }

    public String getMetaNodeNummer() {
        return this.metaNodeNummer;
    }

    public void setMetaNodeNummer(String metaNodeNummer) {
        this.metaNodeNummer = metaNodeNummer;
    }

    public String getMetaNodeTypeTags() {
        return this.metaNodeTypeTags;
    }

    public void setMetaNodeTypeTags(String metaNodeTypeTags) {
        this.metaNodeTypeTags = metaNodeTypeTags;
    }

    public String getMetaNodeSubType() {
        return this.metaNodeSubType;
    }

    public void setMetaNodeSubType(String metaNodeSubType) {
        this.metaNodeSubType = metaNodeSubType;
    }

    public Date getPlanStart() {
        return this.planStart;
    }

    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    public Date getPlanEnde() {
        return this.planEnde;
    }

    public void setPlanEnde(Date planEnde) {
        this.planEnde = planEnde;
    }

    public Double getPlanAufwand() {
        return this.planAufwand;
    }

    public void setPlanAufwand(Double planAufwand) {
        this.planAufwand = planAufwand;
    }

    public String getPlanTask() {
        return this.planTask;
    }

    public void setPlanTask(String planTask) {
        this.planTask = planTask;
    }

    public Date getPlanCalcStart() {
        return this.planCalcStart;
    }

    public void setPlanCalcStart(Date planCalcStart) {
        this.planCalcStart = planCalcStart;
    }

    public Date getPlanCalcEnde() {
        return this.planCalcEnde;
    }

    public void setPlanCalcEnde(Date planCalcEnde) {
        this.planCalcEnde = planCalcEnde;
    }

    public String getPlanCalcCheckSum() {
        return this.planCalcCheckSum;
    }

    public void setPlanCalcCheckSum(String planCalcCheckSum) {
        this.planCalcCheckSum = planCalcCheckSum;
    }

    public Date getPlanChildrenSumStart() {
        return this.planChildrenSumStart;
    }

    public void setPlanChildrenSumStart(Date planChildrenSumStart) {
        this.planChildrenSumStart = planChildrenSumStart;
    }

    public Date getPlanChildrenSumEnde() {
        return this.planChildrenSumEnde;
    }

    public void setPlanChildrenSumEnde(Date planChildrenSumEnde) {
        this.planChildrenSumEnde = planChildrenSumEnde;
    }

    public Double getPlanChildrenSumAufwand() {
        return this.planChildrenSumAufwand;
    }

    public void setPlanChildrenSumAufwand(Double planChildrenSumAufwand) {
        this.planChildrenSumAufwand = planChildrenSumAufwand;
    }

    public Integer getPlanDuration() {
        return this.planDuration;
    }

    public void setPlanDuration(Integer planDuration) {
        this.planDuration = planDuration;
    }

    public PlanDependencieData.DurationMeasure getPlanDurationMeasure() {
        return this.planDurationMeasure;
    }

    public void setPlanDurationMeasure(PlanDependencieData.DurationMeasure planDurationMeasure) {
        this.planDurationMeasure = planDurationMeasure;
    }

    public PlanDependencieData.PredecessorType getPlanPredecessorType() {
        return this.planPredecessorType;
    }

    public void setPlanPredecessorType(PlanDependencieData.PredecessorType planPredecessorType) {
        this.planPredecessorType = planPredecessorType;
    }

    public BaseNode getPlanPredecessor() {
        return this.planPredecessor;
    }

    public void setPlanPredecessor(BaseNode planPredecessor) {
        this.planPredecessor = planPredecessor;
    }

    public PlanDependencieData.PredecessorDependencieType getPlanPredecessorDependencieType() {
        return this.planPredecessorDependencieType;
    }

    public void setPlanPredecessorDependencieType(PlanDependencieData.PredecessorDependencieType planPredecessorDependencieType) {
        this.planPredecessorDependencieType = planPredecessorDependencieType;
    }

    public Integer getPlanPredecessorShift() {
        return this.planPredecessorShift;
    }

    public void setPlanPredecessorShift(Integer planPredecessorShift) {
        this.planPredecessorShift = planPredecessorShift;
    }

    public PlanDependencieData.DurationMeasure getPlanPredecessorShiftMeasure() {
        return this.planPredecessorShiftMeasure;
    }

    public void setPlanPredecessorShiftMeasure(PlanDependencieData.DurationMeasure planPredecessorShiftMeasure) {
        this.planPredecessorShiftMeasure = planPredecessorShiftMeasure;
    }

    public Double getIstStand() {
        return this.istStand;
    }

    public void setIstStand(Double istStand) {
        this.istStand = istStand;
    }

    public Date getIstStart() {
        return this.istStart;
    }

    public void setIstStart(Date istStart) {
        this.istStart = istStart;
    }

    public Date getIstEnde() {
        return this.istEnde;
    }

    public void setIstEnde(Date istEnde) {
        this.istEnde = istEnde;
    }

    public Double getIstAufwand() {
        return this.istAufwand;
    }

    public void setIstAufwand(Double istAufwand) {
        this.istAufwand = istAufwand;
    }

    public String getIstTask() {
        return this.istTask;
    }

    public void setIstTask(String istTask) {
        this.istTask = istTask;
    }

    public Double getIstChildrenSumStand() {
        return this.istChildrenSumStand;
    }

    public void setIstChildrenSumStand(Double istChildrenSumStand) {
        this.istChildrenSumStand = istChildrenSumStand;
    }

    public Date getIstChildrenSumStart() {
        return this.istChildrenSumStart;
    }

    public void setIstChildrenSumStart(Date istChildrenSumStart) {
        this.istChildrenSumStart = istChildrenSumStart;
    }

    public Date getIstChildrenSumEnde() {
        return this.istChildrenSumEnde;
    }

    public void setIstChildrenSumEnde(Date istChildrenSumEnde) {
        this.istChildrenSumEnde = istChildrenSumEnde;
    }

    public Double getIstChildrenSumAufwand() {
        return this.istChildrenSumAufwand;
    }

    public void setIstChildrenSumAufwand(Double istChildrenSumAufwand) {
        this.istChildrenSumAufwand = istChildrenSumAufwand;
    }

    public Set<BaseNode> getChildNodes() {
        return this.childNodes;
    }

    public void setChildNodes(Set<BaseNode> childNodes) {
        this.childNodes = childNodes;
    }

    public Integer getStatChildNodeCount() {
        return this.statChildNodeCount;
    }

    public void setStatChildNodeCount(Integer statChildNodeCount) {
        this.statChildNodeCount = statChildNodeCount;
    }

    public Integer getStatWorkflowCount() {
        return this.statWorkflowCount;
    }

    public void setStatWorkflowCount(Integer statWorkflowCount) {
        this.statWorkflowCount = statWorkflowCount;
    }

    public Integer getStatWorkflowTodoCount() {
        return this.statWorkflowTodoCount;
    }

    public void setStatWorkflowTodoCount(Integer statWorkflowTodoCount) {
        this.statWorkflowTodoCount = statWorkflowTodoCount;
    }

    public void setStatUrlResCount(Integer statUrlResCount) {
        this.statUrlResCount = statUrlResCount;
    }

    public Integer getStatUrlResCount() {
        return this.statUrlResCount;
    }

    public void setStatInfoCount(Integer statInfoCount) {
        this.statInfoCount = statInfoCount;
    }

    public Integer getStatInfoCount() {
        return this.statInfoCount;
    }

    public BaseNode getParentNode() {
        return this.parentNode;
    }

    public String getState() {
        return this.state;
    }

    public String getType() {
        return this.type;
    }

    public Integer getSortPos() {
        return this.sortPos;
    }

    public void setSortPos(Integer sortPos) {
        this.sortPos = sortPos;
    }

    public int getCurSortIdx() {
        return this.curSortIdx;
    }

    public void setCurSortIdx(int curSortIdx) {
        this.curSortIdx = curSortIdx;
    }

    public Map<String, DataDomain> getChildNodesByNameMapMap() {
        return this.childNodesByNameMapMap;
    }

    public void setChildNodesByNameMapMap(Map<String, DataDomain> childNodesByNameMapMap) {
        this.childNodesByNameMapMap = childNodesByNameMapMap;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCachedParentHierarchy() {
        return this.cachedParentHierarchy;
    }

    public void setCachedParentHierarchy(String cachedParentHierarchy) {
        this.cachedParentHierarchy = cachedParentHierarchy;
    }
}
