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
package de.yaio.app.core.datadomainservice;

import de.yaio.app.core.datadomain.CachedData;
import de.yaio.app.core.datadomain.DataDomain;
import de.yaio.app.core.node.BaseNode;
import de.yaio.app.core.nodeservice.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/** 
 * businesslogic for dataDomain: CachedData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class CachedDataServiceImpl extends DataDomainRecalcImpl implements CachedDataService {

    // Logger
    private static final Logger LOGGER = Logger.getLogger(CachedDataServiceImpl.class);

    private static CachedDataServiceImpl instance = new CachedDataServiceImpl();
    
    /**
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static CachedDataServiceImpl getInstance() {
        return instance;
    }

    /** 
     * add me as DataDomainRecalcer to the Service-Config
     * @param nodeService            instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc = CachedDataServiceImpl.getInstance();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) {
        if (node == null) {
            return;
        }
        // Check if node is compatibel
        if (!CachedData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }

        // Roll
        this.initParentHierarchy((CachedData) node);
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) {
        // NOP
    }
    
    @Override
    public Class<?> getRecalcTargetClass() {
        return CachedData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return CachedDataService.CONST_RECALC_ORDER;
    }
    
    
    @Override
    public void initParentHierarchy(final CachedData node) {
        if (!StringUtils.isEmpty(node.getCachedParentHierarchy())) {
            return;
        }

        String parentHierarchy = "," + ((BaseNode)node).getSysUID() + ",";
        if (node.getParentNode() != null) {
            if (StringUtils.isEmpty(node.getParentNode().getCachedParentHierarchy())) {
                initParentHierarchy(node.getParentNode());
            }

            parentHierarchy = node.getParentNode().getCachedParentHierarchy() + parentHierarchy;
        }

        node.setCachedParentHierarchy(parentHierarchy);
    }
}
