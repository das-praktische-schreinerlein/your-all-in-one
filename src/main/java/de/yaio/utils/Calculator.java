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
package de.yaio.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import de.yaio.core.datadomain.BaseWorkflowData.WorkflowState;

/**
 * <h4>FeatureDomain:</h4>
 *     Utils
 * <h4>FeatureDescription:</h4>
 *     Utils for calculating
 * @package de.yaio.utils
 * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category Utils
 * @copyright Copyright (c) 2014, Michael Schreiner
 * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */

public class Calculator {
    /** comparison result arg1 is null or lower than arg2 **/
    public static final int CONST_COMPARE_LT = -1;
    /** comparison result arg1 is greater than arg2 or arg2 is null **/
    public static final int CONST_COMPARE_GT = 1;
    /** comparison result arg1=arg2 or both are null **/
    public static final int CONST_COMPARE_EQ = 0;
    /** calculation result calculate the minimum of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_MIN = 1;
    /** calculation result calculate the maxuimum of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_MAX = 2;
    /** calculation result calculate the sum of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_SUM = 3;
    /** calculation result calculate the state for arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_STATE = 4;
    /** calculation result calculate the multiplication of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_MUL = 5;
    /** calculation result calculate the multiplication-state of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_MULSTATE = 6;
    /** calculation result calculate the workflow-state of arg1 and arg2 **/
    public static final int CONST_CALCULATE_ACTION_WORKFLOWSTATE = 7;

    // einige Double-Vergleiche funktionieren wegen Double-Kommastellen nicht :-(
    /** calculation constant for 0% **/
    public static final double CONST_DOUBLE_NULL = 0.00001;
    /** calculation constant for 100% **/
    public static final double CONST_DOUBLE_100 = 99.99999;
    
    // Logger
    private static final Logger LOGGER =
            Logger.getLogger(Calculator.class);
    

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - Calculation
     * <h4>FeatureDescription:</h4>
     *     executes the action with value1 and value2<br>
     *     result = action(value1, value2)
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue Object - result of the calculation
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Calculator
     * @param arg1 - value1 for calculation
     * @param arg2 - value1 for calculation
     * @param action - calculation action CONST_CALCULATE_ACTION_*
     * @return - result of the calculation
     * @throws Exception - action not allowed possible
     */
    public static Object calculate(final Object arg1, final Object arg2, 
                                   final int action) throws Exception {
        Object res = null;

        // test ob beide belegt
        if (arg1 != null && arg2 != null) {
            // anhand Typ vergleichen
            if (WorkflowState.class.isInstance(arg1)) {
                //
                // BaseWorkflow
                //
                if (action == CONST_CALCULATE_ACTION_WORKFLOWSTATE) {
                    // compare workflowstate
                    WorkflowState a1 = (WorkflowState) arg1;
                    WorkflowState a2 = (WorkflowState) arg2;
                    
                    // compare the workflowstates by their order
                    res = (a1.ordinal() >= a2.ordinal() ? a1 : a2);
                    
                    // special case: if DONE && OPEN -> RUNNING
                    if (a1 == WorkflowState.DONE && a2 == WorkflowState.OPEN) {
                        res = WorkflowState.RUNNING;
                    } else if (a2 == WorkflowState.DONE && a1 == WorkflowState.OPEN) {
                        res = WorkflowState.RUNNING;
                    }
                } else {
                    // unbekannter Typ
                    throw new Exception("Action for Datatype BaseWorkflow not allowed: " + action);
                }
            } else if (Date.class.isInstance(arg1)) {
                //
                // Date
                //
                // je nach Action
                if (action == CONST_CALCULATE_ACTION_MIN) {
                    // Minimum
                    if (((Date) arg1).before((Date) arg2)) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_MAX) {
                    // Maximum
                    if (((Date) arg1).after((Date) arg2)) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else {
                    // unbekannter Typ
                    throw new Exception("Action for Datatype java.util.Date not allowed: " + action);
                }
            } else if (Double.class.isInstance(arg1)) {
                //
                // Double
                //
                // je nach Action
                if (action == CONST_CALCULATE_ACTION_MIN) {
                    // Minimum
                    if (((Double) arg1).doubleValue() < ((Double) arg2).doubleValue()) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_MAX) {
                    // Maximum
                    if (((Double) arg1).doubleValue() > ((Double) arg2).doubleValue()) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_SUM) {
                    // Sum
                    res = ((Double) arg1).doubleValue() + ((Double) arg2).doubleValue();
                } else if (action == CONST_CALCULATE_ACTION_MUL) {
                    // Mul
                    res = ((Double) arg1).doubleValue() * ((Double) arg2).doubleValue();
                } else if (action == CONST_CALCULATE_ACTION_MULSTATE) {
                    // Mul
                    if (((Double) arg1).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn IstStand leer, dann Aufwand nehmen
                        res = new Double(0);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc set arg1=null: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    } else if (((Double) arg2).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn Aufwand leer, dann nichts
                        res = new Double(0);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc set arg2=null: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    } else {
                        // Stand und Aufwand mulitplizieren
                        res = ((Double) arg1).doubleValue() * ((Double) arg2).doubleValue() / 100;
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc MUL: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    }
                } else if (action == CONST_CALCULATE_ACTION_STATE) {
                    // State
                    if (((Double) arg1).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn Aufwand leer, dann nichts
                        res = new Double(0);
                    } else {
                        // Aufwand und hochgerechneten Aufwand dividieren
                        // 100/2 = x/1
                        res = 100 / ((Double) arg2).doubleValue() * ((Double) arg1).doubleValue();
                    }
                } else {
                    // unbekannter Typ
                    throw new Exception("Action for Datatype java.lang.Double not allowed: " + action);
                }
            } else {
                // unbekannter Typ
                throw new Exception("Unknown Datatype: arg1" + arg1.getClass());
            }
        } else {
            if (action == CONST_CALCULATE_ACTION_WORKFLOWSTATE) {
                // workflowstatus without data
                if (arg1 != null) {
                    // arg1 belegt
                    res = arg1;
                } else {
                    // arg2 belegt
                    res = arg2;
                }
            } else if (action == CONST_CALCULATE_ACTION_MULSTATE) {
                // MulStatus ohne Daten
                res = new Double(0);
            } else if (action == CONST_CALCULATE_ACTION_STATE) {
                // MulStatus ohne Daten
                if (arg1 != null) {
                    // arg1 belegt
                    res = new Double(0);
                } else {
                    // arg2 belegt
                    res = new Double(100);
                }
            } else if (action == CONST_CALCULATE_ACTION_MUL) {
                // Status ohne Daten ist 0
                res = new Double(0);
            } else if (arg1 != null) {
                // arg1 belegt
                res = arg1;
            } else {
                // arg2 belegt oder keines
                res = arg2;
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("calc null: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("calc: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
        }

        return res;
    }

    /**
     * <h4>FeatureDomain:</h4>
     *     Tools - Calculation
     * <h4>FeatureDescription:</h4>
     *     compare the values value1 and value2<br>
     *     if one of these is null, this will LT than the other<br>
     *     if both are null, they are equal<br>
     * <h4>FeatureResult:</h4>
     *   <ul>
     *     <li>returnValue -1/0/+1
     *   </ul> 
     * <h4>FeatureKeywords:</h4>
     *     Calculator
     * @param arg1 - value1 for comparison
     * @param arg2 - value1 for comparison
     * @return - result of the comparison (LT,EQU, GT - -1/0/+1)
     * @throws IllegalAccessException - if Class unknown or classes differ
     */
    public static int compareValues(Object arg1, Object arg2) throws IllegalAccessException {
        int result = CONST_COMPARE_EQ;
        
        // test ob beide belegt
        if (arg1 != null && arg2 != null) {
            if (Timestamp.class.isInstance(arg1)) {
                arg1 = new Date(((Timestamp) arg1).getTime());
            }
            if (Timestamp.class.isInstance(arg2)) {
                arg2 = new Date(((Timestamp) arg2).getTime());
            }
            // check classes
            if (!arg1.getClass().isInstance(arg2)) {
                // class differ!!
                throw new IllegalAccessException("cant compare arg1 (" + arg1.getClass() + "):" 
                                + " with arg2:" + arg2.getClass());
            }
            
            // branch for classes
            if (String.class.isInstance(arg1)) {
                return ((String) arg1).compareTo((String) arg2);
            } else if (Integer.class.isInstance(arg1)) {
                return ((Integer) arg1).compareTo((Integer) arg2);
            } else if (Float.class.isInstance(arg1)) {
                return ((Float) arg1).compareTo((Float) arg2);
            } else if (Double.class.isInstance(arg1)) {
                return ((Double) arg1).compareTo((Double) arg2);
            } else if (Date.class.isInstance(arg1)) {
                return ((Date) arg1).compareTo((Date) arg2);
            } else {
                // unknown class
                throw new IllegalAccessException("cant compare arg1 (" + arg1.getClass() + "):" 
                                + " with arg2:" + arg2.getClass() + ": unknwon class");
            }

        } else if (arg1 == null) {
            // arg1 is null
            result = CONST_COMPARE_LT;
        } else {
            // arg2 is null
            result = CONST_COMPARE_GT;
        }

        return result;
    }
}
