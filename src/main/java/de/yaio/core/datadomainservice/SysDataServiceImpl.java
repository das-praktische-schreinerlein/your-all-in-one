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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.commons.data.DataUtils;
import de.yaio.core.datadomain.DataDomain;
import de.yaio.core.datadomain.SysData;
import de.yaio.core.nodeservice.NodeService;

/** 
 * businesslogic for dataDomain: SysData
 * 
 * @FeatureDomain                BusinessLogic
 * @package                      de.yaio.core.dataservice
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class SysDataServiceImpl extends DataDomainRecalcImpl implements SysDataService {

    protected static int VAR_CUR_UID = 1;

    // Logger
    private static final Logger LOGGER = Logger.getLogger(SysDataServiceImpl.class);

    private static SysDataServiceImpl instance = new SysDataServiceImpl();
    
    protected final DateFormat UIDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /** 
     * return the main instance of this service
     * @return                       the main instance of this service
     */
    public static SysDataServiceImpl getInstance() {
        return instance;
    }

    /** 
     * add me as DataDomainRecalcer to the Service-Config
     * @param nodeService            instance of the nodeService which will call me as recalcer
     */
    public static void configureDataDomainRecalcer(final NodeService nodeService) {
        DataDomainRecalc baseDataDomainRecalc = SysDataServiceImpl.getInstance();
        nodeService.addDataDomainRecalcer(baseDataDomainRecalc);
    }
    
    @Override
    public void doRecalcBeforeChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        // NOP
    }

    @Override
    public void doRecalcAfterChildren(final DataDomain node, final NodeService.RecalcRecurseDirection recurseDirection) throws Exception {
        if (node == null) {
            return;
        }
        // Check if node is compatibel
        if (!SysData.class.isInstance(node)) {
            throw new IllegalArgumentException();
        }

        // Roll
        this.initSysData((SysData) node);
    }
    
    @Override
    public Class<?> getRecalcTargetClass() {
        return SysData.class;
    }

    @Override
    public int getRecalcTargetOrder() {
        return SysDataService.CONST_RECALC_ORDER;
    }
    
    
    @Override
    public void initSysData(final SysData node) throws Exception {
        // UID generieren, wenn noch nicht belegt
        String uid = node.getSysUID();
        if (uid == null || uid.length() < 1) {
            node.setSysUID("DT" + UIDF.format(new Date()) + VAR_CUR_UID);
            VAR_CUR_UID++;
        }

        // Create-Date generieren
        Date created = node.getSysCreateDate();
        if (created == null) {
            node.setSysCreateDate(new Date());
        }

        // Checksum testen und ggf. aktualisieren
        String checksum = node.getSysCurChecksum();
        String newChecksum = this.getCheckSum(node);
        boolean flgChanged = false;
        if ((checksum == null) || (!newChecksum.equals(checksum))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("initSysData checksum changed old:" + checksum 
                           + " new:" + newChecksum 
                           + " cmp=" + newChecksum.equals(checksum)
                           + " nullchecksum=" + (checksum == null));
            }
            if (!newChecksum.equals(checksum)) {
                flgChanged = true;
            }
            checksum = newChecksum;
            node.setSysCurChecksum(checksum);
            node.setFlgForceUpdate(true);
        }

        // AenderungsDatum
        Date changed = node.getSysChangeDate();
        if (changed == null || flgChanged) {
            node.setSysChangeDate(new Date());
        }

        Integer changedCount = node.getSysChangeCount();
        if (flgChanged) {
            if (changedCount == null) {
                changedCount = new Integer(0);
            }
            changedCount++;
            node.setSysChangeCount(changedCount);
        }
    }

    @Override
    public String getCheckSum(final SysData node) throws Exception {
        // Daten holen
        String data = node.getDataBlocks4CheckSum();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getDataBlocks4CheckSum = " + data);
        }

        // Checksumme
        return DataUtils.generateCheckSum(data);
    }
}
