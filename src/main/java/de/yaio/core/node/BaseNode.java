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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.yaio.core.datadomain.BaseData;
import de.yaio.core.datadomain.BaseWorkflowData;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.DescData;
import de.yaio.core.datadomain.MetaData;
import de.yaio.core.datadomain.SysData;
import de.yaio.core.datadomainservice.MetaDataService;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataService;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.nodeservice.BaseNodeService;
import de.yaio.core.nodeservice.NodeService;

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
    DescData, BaseWorkflowData {
    
    public static final String CONST_NODETYPE_IDENTIFIER_UNKNOWN = "UNKNOWN";
    public static Map<String, Object> CONST_MAP_NODETYPE_IDENTIFIER = new HashMap<String, Object>();
    static {
        // Defaults
        CONST_MAP_NODETYPE_IDENTIFIER.put(CONST_NODETYPE_IDENTIFIER_UNKNOWN, CONST_NODETYPE_IDENTIFIER_UNKNOWN);
        // Abarten
        CONST_MAP_NODETYPE_IDENTIFIER.put("UNKNOWN", CONST_NODETYPE_IDENTIFIER_UNKNOWN);
        CONST_MAP_NODETYPE_IDENTIFIER.put("UNBEKANNT", CONST_NODETYPE_IDENTIFIER_UNKNOWN);
    }

    protected static SysDataService sysDataService = new SysDataServiceImpl();
    protected static MetaDataService metaDataService = new MetaDataServiceImpl();
    protected static NodeService nodeService = new BaseNodeService();
    public SysDataService getSysDataService() {
        return sysDataService;
    }
    public static SysDataService getConfiguredSysDataService() {
        return sysDataService;
    }
    public static final void setSysDataService(SysDataService newSysDataService) {
        sysDataService = newSysDataService;
    }
    public static MetaDataService getConfiguredMetaDataService() {
        return metaDataService;
    }
    public MetaDataService getMetaDataService() {
        return metaDataService;
    }

    public static final void setMetaDataService(MetaDataService newMetaDataService) {
        metaDataService = newMetaDataService;
    }
    public static NodeService getConfiguredNodeService() {
        return nodeService;
    }
    public NodeService getNodeService() {
        return nodeService;
    }

    private Long importTmpId;
    
    /**
     */
    @Size(min = 1, max = 64000)
    private String fullSrc;

    /**
     */
    @Size(max = 5000)
    private String srcName;

    /**
     */
    @NotNull
    @Size(max = 2000)
    private String name;

    /**
     */
    @Min(0L)
    @Max(50L)
    private Integer ebene;

    /**
     */
    @Size(max = 64000)
    private String nodeDesc;

    /**
     */
    @Size(max = 5)
    private String docLayoutTagCommand;

    /**
     */
    @Size(max = 255)
    private String docLayoutAddStyleClass;

    /**
     */
    @Size(max = 80)
    private String docLayoutShortName;

    /**
     */
    @Size(max = 5)
    private String docLayoutFlgCloseDiv;

    /**
     */
    @Id
    @Size(max = 80)
    private String sysUID;

    /**
     */
    @Size(max = 50)
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
    private String metaNodePraefix;

    /**
     */
    @Size(max = 10)
    private String metaNodeNummer;

    /**
     */
    @Size(max = 255)
    private String metaNodeTypeTags;

    /**
     */
    @Size(max = 255)
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
    private String planTask;

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
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "parentNode")
    private Set<BaseNode> childNodes = new HashSet<BaseNode>();

    /**
     */
    @ManyToOne
    private BaseNode parentNode;
    
    /**
     */
    @Size(max = 255)
    private String state;

    /**
     */
    @Size(max = 255)
    private String type;
    
    
    //####################
    // Hirarchy-functions
    //####################
    @Transient
    protected Map<String, DataDomain> childNodesByNameMapMap = new LinkedHashMap <String, DataDomain>();

    public void initChildNodesByNameMap() {
        Set<BaseNode> childSet = childNodes;
        for (BaseNode child : childSet) {
            childNodesByNameMapMap.put(child.getIdForChildByNameMap(), child);
        }
    }

    @Override
    public void setParentNode(BaseNode parentNode) {
        getNodeService().setParentNode(this, parentNode, true);
    }
    @Override
    public void setParentNodeOnly(DataDomain parentNode) {
        this.parentNode = (BaseNode)parentNode;
    }

    @Override
    public String getIdForChildByNameMap() {
        return getSrcName();
    }

    @Override
    public void addChildNode(DataDomain childNode) {
        if (childNode != null) {
            this.childNodesByNameMapMap.put(childNode.getIdForChildByNameMap(), childNode);
            this.childNodes.add((BaseNode)childNode);
        }
    }
    
    @Override
    public void setParentNode(DataDomain parentNode) {
        setParentNode((BaseNode)parentNode);
    }

    @Override
    public Map<String, DataDomain> getChildNodesByNameMap() {
        return childNodesByNameMapMap;
    }

    @Override
    public boolean hasChildNode(DataDomain childNode) {
        if (    (childNode != null) 
             && (childNodesByNameMapMap.get(childNode.getIdForChildByNameMap()) != null)) {
            return true;
        }
    
        return false;
    }

    //####################
    // Recalc-functions
    //####################
    
    @Override
    public void recalcData(int recursionDirection) throws Exception {
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
    public String getDataBlocks4CheckSum() throws Exception {
        // Content erzeugen
        StringBuffer data = new StringBuffer();
        data.append(this.getType())
            .append(this.getState())
            .append(" name=").append(getName())
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
            .append(" desc=").append(getNodeDesc())
            ;
        return data.toString();
    }

    //####################
    // Workflow-functions TODO - separate type+state
    //####################
    @Override
    public void setType(String type) {
        this.type = type;
        if (BaseWorkflowData.class.isInstance(this)) {
            this.state = type;
        }
    }

    @Override
    public void setState(String state) {
        this.state = state;
        if (BaseWorkflowData.class.isInstance(this)) {
            this.type = state;
        }
    }

    @Override
    public Map<String, Object> getConfigState() {
        return CONST_MAP_NODETYPE_IDENTIFIER;
    }
    
    @Override
    public boolean isWFStatus (String state) {
        return getNodeService().isWFStatus(state);
    }

    @Override
    public boolean isWFStatusDone (String state) {
        return getNodeService().isWFStatusDone(state);
    }

    @Override
    public boolean isWFStatusOpen (String state) {
        return getNodeService().isWFStatusOpen(state);
    }

    @Override
    public boolean isWFStatusCanceled(String state) {
        return getNodeService().isWFStatusCanceled(state);
    }
    
    //####################
    // service-functions
    //####################
    public String toString() {
        return "ProjektNode: " + this.getSrcName() + " ID: " + this.getWorkingId() + " SRC:" + this.getFullSrc();
    }

    @Override
    public String getNameForLogger() {
        return this.getSrcName();    
    }
    
    @Override
    public String getWorkingId() {
        String res = this.getImportTmpId().toString();
        
        if (this.getMetaNodeNummer() != null && (this.getMetaNodeNummer().length() > 0)) {
            res = this.getMetaNodePraefix() + this.getMetaNodeNummer();
        }
        return res;
    }

    
    
    @Override
    public String getParentNameHirarchry(String delimiter, boolean directionForward) {
        String parentNames = "";
        if (delimiter == null) {
            delimiter = "";
        }

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
}
