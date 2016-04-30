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
package de.yaio.core.nodeservice;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.DataDomainRecalc;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/** 
 * baseservice
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class NodeServiceImpl implements NodeService {

    // Logger
    private static final Logger LOGGER = Logger.getLogger(NodeServiceImpl.class);
    
    /** Hashmap of the configured DadaDomainRecacler by ClassName */
    public Map<Class<?>, DataDomainRecalc> hshDataDomainRecalcerByClass = 
                    new HashMap<Class<?>, DataDomainRecalc>();
    
    /** sorted Treeset of the configured DadaDomainRecacler */
    protected Set<DataDomainRecalc> hshDataDomainRecalcer = 
                    new TreeSet<DataDomainRecalc>();
    //////////////
    // service-functions for configuration
    //////////////
    @Override
    public void addDataDomainRecalcer(final DataDomainRecalc dataDomainRecalcer) {
        if (dataDomainRecalcer.getRecalcTargetOrder() < 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SKIP: Targetorder < 0 TargetOrder:" 
                        + dataDomainRecalcer.getRecalcTargetOrder()
                        + " Recalcer:" + dataDomainRecalcer.getClass().getName());
                return;
            }
        }
        this.hshDataDomainRecalcer.add(dataDomainRecalcer);
        this.hshDataDomainRecalcerByClass.put(dataDomainRecalcer.getClass(), dataDomainRecalcer);
    }
    
    //////////////
    // service-functions for recalc
    //////////////
    @Override
    public void doRecalc(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        this.doRecalcBeforeChildren(node, recurseDirection);
        if (recurseDirection == NodeService.RecalcRecurseDirection.CHILDREN) {
            this.doRecalcChildren(node, recurseDirection);
        }
        this.doRecalcAfterChildren(node, recurseDirection);
        
        // Recalc parents
        if (recurseDirection == NodeService.RecalcRecurseDirection.PARENT) {
            DataDomain parent = node.getParentNode();
            if (parent != null) {
                this.doRecalc(parent, recurseDirection);
            }
        }
    }


    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        for (DataDomainRecalc recalcer : this.hshDataDomainRecalcer) {
            if (recalcer.getRecalcTargetClass().isInstance(node)) {
                LOGGER.debug("doRecalcBeforeChildren " + recalcer.getClass().getName());
                recalcer.doRecalcBeforeChildren(node, recurseDirection);
            } else {
                LOGGER.debug("doRecalcBeforeChildren SKIP: Node is not of type + " 
                        + recalcer.getRecalcTargetClass().getName() 
                        + " Node=" + node.getNameForLogger());
            }
        }
    }


    @Override
    public void doRecalcChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        if (recurseDirection == NodeService.RecalcRecurseDirection.CHILDREN) {
            for (String name : node.getChildNodesByNameMap().keySet()) {
                node.getChildNodesByNameMap().get(name).recalcData(recurseDirection);
            }
        }
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        for (DataDomainRecalc recalcer : this.hshDataDomainRecalcer) {
            if (recalcer.getRecalcTargetClass().isInstance(node)) {
                LOGGER.debug("doRecalcAfterChildren " + recalcer.getClass().getName());
                recalcer.doRecalcAfterChildren(node, recurseDirection);
            } else {
                LOGGER.debug("doRecalcAfterChildren SKIP: Node is not of type + " 
                        + recalcer.getRecalcTargetClass().getName() 
                        + " Node=" + node.getNameForLogger());
            }
        }
    }
}
