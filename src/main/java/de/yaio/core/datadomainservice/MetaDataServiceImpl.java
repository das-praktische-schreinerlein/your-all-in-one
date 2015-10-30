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
package de.yaio.core.datadomainservice;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.MetaData;
import de.yaio.core.nodeservice.NodeService;

/** 
 * businesslogic for dataDomain: MetaData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class MetaDataServiceImpl extends DataDomainRecalcImpl implements MetaDataService {
    
    /** Logger */ 
    private static final Logger LOGGER =
            Logger.getLogger(MetaDataServiceImpl.class);

    private static MetaDataServiceImpl instance = new MetaDataServiceImpl();
    
    protected NodeNumberService nodeNumberService = NodeNumberServiceImpl.getInstance();
    
    /** 
     * return the main instance of this service
     * @FeatureDomain                Persistence
     * @FeatureResult                return the main instance of this service
     * @FeatureKeywords              Persistence
     * @return                       the main instance of this service
     */
    public static MetaDataServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Class<?> getRecalcTargetClass() {
        return MetaData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return MetaDataService.CONST_RECALC_ORDER;
    }

    /** 
     * add me as DataDomainRecalcer to the Service-Config
     * @FeatureDomain                DataExport Presentation
     * @FeatureKeywords              Config
     * @param nodeService            instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc = MetaDataServiceImpl.getInstance();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final int recurceDirection) throws Exception {
        if (node == null) {
            return;
        }
        // Check if node is compatibel
        if (!MetaData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }

        // Roll
        this.initMetaData((MetaData) node);
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final int recurceDirection) throws Exception {
        // NOP
    }
    
    
    @Override
    public void initMetaData(final MetaData node) throws Exception {
        // Daten einlesen
        String praefix = node.getMetaNodePraefix();
        String id = node.getMetaNodeNummer();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("initMetaData id=" + id 
                            + " praefix=" + praefix 
                            + " for:" + node.getNameForLogger());
        }

        // Praefix initialisieren
        if (praefix == null || praefix.length() <= 0) {
            MetaData vorgaenger = node.getParentNode();
            while (vorgaenger != null && (praefix == null || praefix.length() <= 0)) {
                praefix = vorgaenger.getMetaNodePraefix();
                vorgaenger = vorgaenger.getParentNode();
            }
            node.setMetaNodePraefix(praefix);
        }

        // ID intialisieren
        if ((praefix != null && praefix.length() > 0)
            && (id == null || id.length() <= 0)) {
            node.setMetaNodeNummer(this.getNextNodeNumber(node).toString());
        }
    }

    @Override
    public Object getNextNodeNumber(final MetaData node) throws Exception {
        return getNodeNumberService().getNextNodeNumber(node);
    }

    @Override
    public void setNodeNumberService(final NodeNumberService newNodeNumberService)
            throws Exception {
        this.nodeNumberService = newNodeNumberService;
        
    }

    @Override
    public NodeNumberService getNodeNumberService() throws Exception {
        return nodeNumberService;
    }
}
