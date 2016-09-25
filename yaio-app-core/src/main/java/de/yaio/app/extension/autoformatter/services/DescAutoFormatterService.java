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
package de.yaio.app.extension.autoformatter.services;

import de.yaio.app.core.datadomain.DescData;
import de.yaio.app.core.datadomainservice.TriggeredDataDomainRecalc;
import de.yaio.commons.io.IOExceptionWithCause;

import java.io.IOException;

/** 
 * businesslogic for dataDomain: DescData (format desc )
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public interface DescAutoFormatterService extends TriggeredDataDomainRecalc {

    /** 
     * automaticly format the desc of a node
     * @param datanode               node
     * @throws IOExceptionWithCause  if something logical went wrong
     * @throws IOException           if something physical went wrong
     */
    void autoFormatDesc(DescData datanode) throws IOExceptionWithCause, IOException;
}
