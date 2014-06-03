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

import java.util.Date;

import org.apache.log4j.Logger;

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
    public static int CONST_CALCULATE_ACTION_MIN = 1;
    public static int CONST_CALCULATE_ACTION_MAX = 2;
    public static int CONST_CALCULATE_ACTION_SUM = 3;
    public static int CONST_CALCULATE_ACTION_STATE = 4;
    public static int CONST_CALCULATE_ACTION_MUL = 5;
    public static int CONST_CALCULATE_ACTION_MULSTATE = 6;

    // einige Double-Vergleiche funktionieren wegen Double-Kommastelen nicht :-(
    public static double CONST_DOUBLE_NULL = 0.00001;
    public static double CONST_DOUBLE_100 = 99.99999;
    
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
     * @throws Exception
     */
    public static Object calculate(Object arg1, Object arg2, int action) throws Exception {
        Object res = null;

        // test ob beide belegt
        if (arg1 != null && arg2 != null) {
            // anhand Typ vergleichen
            if (Date.class.isInstance(arg1)) {
                // je nach Action
                if (action == CONST_CALCULATE_ACTION_MIN) {
                    // Minimum
                    if (((Date)arg1).before((Date)arg2)) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_MAX) {
                    // Maximum
                    if (((Date)arg1).after((Date)arg2)) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else {
                    // unbekannter Typ
                    throw new Exception("Action for Datatype java.util.Date not allowed: " + action);
                }
            } else if (Double.class.isInstance(arg1)) {
                // je nach Action
                if (action == CONST_CALCULATE_ACTION_MIN) {
                    // Minimum
                    if (((Double)arg1).doubleValue() < ((Double)arg2).doubleValue()) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_MAX) {
                    // Maximum
                    if (((Double)arg1).doubleValue() > ((Double)arg2).doubleValue()) {
                        res = arg1;
                    } else {
                        res = arg2;
                    }
                } else if (action == CONST_CALCULATE_ACTION_SUM) {
                    // Sum
                    res = ((Double)arg1).doubleValue() + ((Double)arg2).doubleValue();
                } else if (action == CONST_CALCULATE_ACTION_MUL) {
                    // Mul
                    res = ((Double)arg1).doubleValue() * ((Double)arg2).doubleValue();
                } else if (action == CONST_CALCULATE_ACTION_MULSTATE) {
                    // Mul
                    if (((Double)arg1).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn IstStand leer, dann Aufwand nehmen
                        res = new Double(0);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc set arg1=null: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    } else if (((Double)arg2).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn Aufwand leer, dann nichts
                        res = new Double(0);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc set arg2=null: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    } else {
                        // Stand und Aufwand mulitplizieren
                        res = ((Double)arg1).doubleValue() * ((Double)arg2).doubleValue() / 100;
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("calc MUL: " + action + " arg1:" + arg1 + " arg2:" + arg2 + " =" + res);
                        }
                    }
                } else if (action == CONST_CALCULATE_ACTION_STATE) {
                    // State
                    if (((Double)arg1).doubleValue() <= CONST_DOUBLE_NULL) {
                        // wenn Aufwand leer, dann nichts
                        res = new Double(0);
                    } else {
                        // Aufwand und hochgerechneten Aufwand dividieren
                        // 100/2 = x/1
                        res = 100/((Double)arg2).doubleValue() * ((Double)arg1).doubleValue();
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
            if (action == CONST_CALCULATE_ACTION_MULSTATE) {
                // MulStatus ohne Daten
                res = new Double (0);
            } else if (action == CONST_CALCULATE_ACTION_STATE) {
                // MulStatus ohne Daten
                if (arg1 != null) {
                    // arg1 belegt
                    res = new Double (0);
                } else {
                    // arg2 belegt
                    res = new Double (100);
                }
            } else if (action == CONST_CALCULATE_ACTION_MUL) {
                // Status ohne Daten ist 0
                res = new Double (0);
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


}
