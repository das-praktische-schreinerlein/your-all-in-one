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
    public static final int CONST_ORDER = 55;

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

    public Integer getPlanDuration();
    public void setPlanDuration(Integer planDuration);
    public DurationMeasure getPlanDurationMeasure();
    public void setPlanDurationMeasure(DurationMeasure planDurationMeasure);
    
    public PredecessorType getPlanPredecessorType();
    public void setPlanPredecessorType(PredecessorType predecessorType);
    public BaseNode getPlanPredecessor();
    public void setPlanPredecessor(BaseNode predecessor);
    public PredecessorDependencieType getPlanPredecessorDependencieType();
    public void setPlanPredecessorDependencieType(PredecessorDependencieType predecessorDependencieType);
    
    public Integer getPlanPredecessorShift();
    public void setPlanPredecessorShift(Integer predecessorShift);
    public DurationMeasure getPlanPredecessorShiftMeasure();
    public void setPlanPredecessorShiftMeasure(DurationMeasure planPredecessorShiftMeasure);
}
