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
package de.yaio.core.nodeservice;

import java.util.Map;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.BaseWorkflowDataServiceImpl;
import de.yaio.core.datadomainservice.MetaDataServiceImpl;
import de.yaio.core.datadomainservice.SysDataServiceImpl;
import de.yaio.core.node.BaseNode;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     businesslogic for entity: BaseNode
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class BaseNodeService extends NodeServiceImpl {
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeServiceImpl.class);
    
    public BaseNodeService() {
        configureDataDomainRecalcer();
    }
    
    @Override
    public void configureDataDomainRecalcer() {
        MetaDataServiceImpl.configureDataDomainRecalcer(this);
        SysDataServiceImpl.configureDataDomainRecalcer(this);
        BaseWorkflowDataServiceImpl.configureDataDomainRecalcer(this);
    }


    @Override
    public void setParentNode(DataDomain baseNode, DataDomain parentNode, 
            boolean flgRenewParent) {
        // Parentnode setzen, falls geaendert
        boolean flgParentChanged = false;
        if (baseNode.getParentNode() != parentNode) {
            baseNode.setParentNodeOnly(parentNode);
            flgParentChanged = true;
        }

        // Ebene aktualisieren, falls Aenderung erfolgt oder Renew gewuenscht
        boolean flgEbeneChanged = false;
        if (flgParentChanged || flgRenewParent) {
            int newEbene = baseNode.getEbene();
            if (parentNode != null) {
                if (! flgRenewParent) {
                    // SKIP
                } else if (baseNode.hasChildNode(parentNode)) {
                    // SKIP gehoere schon zur Liste
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("SKIP: Gehoere schon zur Liste: Me=" 
                            + baseNode.getNameForLogger() 
                            + " Parent=" + parentNode.getNameForLogger());
                } else {
                    // nur anhaengen, wenn renew-Flag gesetzt und noch nicht vorhanden
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("add2AsChild Me=" 
                                + baseNode.getNameForLogger() 
                                + " Parent=" + parentNode.getNameForLogger());
                    parentNode.addChildNode(baseNode);
                }
                newEbene = parentNode.getEbene() + 1;
            } else {
                newEbene = 1;
            }
            
            // Ebene nur setzen, wenn geaendert
            if (baseNode.getEbene().intValue() != newEbene) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Ebene change to " + newEbene + "Me=" 
                                + baseNode.getNameForLogger());
                baseNode.setEbene(newEbene);
                flgEbeneChanged = true;

            }
        }

        // falls Ebene geaendert: alle Kinder aktualisieren (Ebene usw.)
        if (flgEbeneChanged) {
            Map<String, DataDomain> lstChidNodes = baseNode.getChildNodesByNameMap();
            if (lstChidNodes != null) {
                for (String nodeName : lstChidNodes.keySet()) {
                    DataDomain node =
                        (DataDomain)lstChidNodes.get(nodeName);
                    node.setParentNode((BaseNode)baseNode);
                }
            }
        }
    }

    @Override
    public void recalcData(DataDomain baseNode, int recursionDirection) throws Exception {
        this.doRecalc(baseNode, recursionDirection);
    }
}