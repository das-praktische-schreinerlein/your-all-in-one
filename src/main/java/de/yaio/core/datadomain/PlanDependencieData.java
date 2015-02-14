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
package de.yaio.core.datadomain;

import javax.xml.bind.annotation.XmlTransient;

import de.yaio.core.node.BaseNode;

/**
 * <h4>FeatureDomain:</h4>
 *     DataDefinition
 * <h4>FeatureDescription:</h4>
 *     interface for DataDomain: PlanDependencies (duration, inheritance, 
 *     predecessor...) of the Node
 * 
 * @package de.yaio.core.datadomain
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category collaboration
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface PlanDependencieData extends DataDomain {
    int CONST_ORDER = 55;

    @XmlTransient
    public static enum DurationMeasure {
        min, 
        h, 
        d, 
        w, 
        m
    }
    
    @XmlTransient
    public static enum PredecessorType {
        parent, 
        predecessor
    }

    @XmlTransient
    public static enum PredecessorDependencieType {
        NO, 
        StartStart,
        EndStart,
        EndEnd,
        inherit
    }

    Integer getPlanDuration();
    void setPlanDuration(Integer planDuration);
    DurationMeasure getPlanDurationMeasure();
    void setPlanDurationMeasure(DurationMeasure planDurationMeasure);
    
    PredecessorType getPlanPredecessorType();
    void setPlanPredecessorType(PredecessorType predecessorType);
    BaseNode getPlanPredecessor();
    void setPlanPredecessor(BaseNode predecessor);
    PredecessorDependencieType getPlanPredecessorDependencieType();
    void setPlanPredecessorDependencieType(PredecessorDependencieType predecessorDependencieType);
    
    Integer getPlanPredecessorShift();
    void setPlanPredecessorShift(Integer predecessorShift);
    DurationMeasure getPlanPredecessorShiftMeasure();
    void setPlanPredecessorShiftMeasure(DurationMeasure planPredecessorShiftMeasure);
}
