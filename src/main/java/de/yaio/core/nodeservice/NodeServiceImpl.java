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

import java.util.HashMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomainservice.DataDomainRecalc;


/**
 * <h4>FeatureDomain:</h4>
 *     BusinessLogic
 * <h4>FeatureDescription:</h4>
 *     baseservice
 * 
 * @package de.yaio.core.dataservice
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public abstract class NodeServiceImpl implements NodeService {

    /** sorted Treeset of the configured DadaDomainRecacler */
    protected TreeSet<DataDomainRecalc> hshDataDomainRecalcer = 
                    new TreeSet<DataDomainRecalc>();
    /** Hashmap of the configured DadaDomainRecacler by ClassName */
    public HashMap<Class<?>, DataDomainRecalc> hshDataDomainRecalcerByClass = 
                    new HashMap<Class<?>, DataDomainRecalc>();
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(NodeServiceImpl.class);
    
    //////////////
    // service-functions for configuration
    //////////////
    @Override
    public void addDataDomainRecalcer(final DataDomainRecalc dataDomainRecalcer) {
        if (dataDomainRecalcer.getRecalcTargetOrder() < 0 ) {
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
    public void doRecalc(final DataDomain node, final int recurceDirection) throws Exception {
        this.doRecalcBeforeChildren(node, recurceDirection);
        if (recurceDirection == CONST_RECURSE_DIRECTION_CHILDREN) {
            this.doRecalcChildren(node, recurceDirection);
        }
        this.doRecalcAfterChildren(node, recurceDirection);
        
        // Recalc parents
        if (recurceDirection == CONST_RECURSE_DIRECTION_PARENT) {
            DataDomain parent = node.getParentNode();
            if (parent != null) {
                this.doRecalc(parent, recurceDirection);
            }
        }
    }


    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final int recurceDirection) throws Exception {
        for (DataDomainRecalc recalcer : this.hshDataDomainRecalcer) {
            if (recalcer.getRecalcTargetClass().isInstance(node)) {
                LOGGER.debug("doRecalcBeforeChildren " + recalcer.getClass().getName());
                recalcer.doRecalcBeforeChildren(node, recurceDirection);
            } else {
                LOGGER.debug("doRecalcBeforeChildren SKIP: Node is not of type + " 
                        + recalcer.getRecalcTargetClass().getName() 
                        + " Node=" + node.getNameForLogger());
            }
        }
    }


    @Override
    public void doRecalcChildren(final DataDomain node, final int recurceDirection) throws Exception {
        if (recurceDirection == CONST_RECURSE_DIRECTION_CHILDREN) {
            for (String name : node.getChildNodesByNameMap().keySet()) {
                node.getChildNodesByNameMap().get(name).recalcData(recurceDirection);
            }
        }
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final int recurceDirection) throws Exception {
        for (DataDomainRecalc recalcer : this.hshDataDomainRecalcer) {
            if (recalcer.getRecalcTargetClass().isInstance(node)) {
                LOGGER.debug("doRecalcAfterChildren " + recalcer.getClass().getName());
                recalcer.doRecalcAfterChildren(node, recurceDirection);
            } else {
                LOGGER.debug("doRecalcAfterChildren SKIP: Node is not of type + " 
                        + recalcer.getRecalcTargetClass().getName() 
                        + " Node=" + node.getNameForLogger());
            }
        }
    }
    
    
    //////////////
    // service-functions for other
    //////////////
    
    @Override
    public boolean isWFStatus(final String state) {
        return false;
    }

    @Override
    public boolean isWFStatusDone(final String state) {
        return false;
    }

    @Override
    public boolean isWFStatusOpen(final String state) {
        return false;
    }

    @Override
    public boolean isWFStatusCanceled(final String state) {
        return false;
    }
}
