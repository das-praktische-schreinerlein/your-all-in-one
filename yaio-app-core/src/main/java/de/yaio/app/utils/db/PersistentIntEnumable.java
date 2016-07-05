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
package de.yaio.app.utils.db;

import java.io.Serializable;

/**
 * Interface to support mapping of enums to specific Integer-values in hibernate
 */
public interface PersistentIntEnumable extends Serializable {

    /**
     * Returns the internal value of each enum to be persisted in hibernate.
     * @return The internal string value.
     */
    Integer getValue();

    /**
     * Returns the enum item from the given value.
     * @param value value.
     * @return enum.
     */
    Enum getEnumFromValue(Integer value);

    /**
     * A helper class to find corespondent enum by a value.
     */
    class EnumableHelper {
        /**
         * Method to find corespondent enum by a provide value, if value can't match will throw an exception.
         * This will be used when database field has restricted values and not allow undefined values (constraint).
         *
         * @param e     The enum class instance
         * @param value The value to be matched
         * @return Enum which matched the value.
         * @throws IllegalArgumentException Thrown when the value can not be match to a enum.
         */
        public static Enum getEnumFromValue(Enum e, Integer value) {
            if (e == null) {
                throw new IllegalStateException("Enum object cannot be null");
            }

            Enum aE = getEnumFromValue(e, value, null);

            if (aE != null) {
                return aE;
            } else {
                throw new IllegalStateException("Invalid value [" + value + "] for enum class [" + e.getClass() + "]");
            }
        }

        /**
         * Method to find corespondent enum by a provide value, if value can't match will throw an exception.
         * This will be used when database field has restricted values and not allow undefined values (constraint).
         *
         * @param e           The enum class instance
         * @param value       The value to be matched
         * @param defaultEnum The default Enum will be returned if null detected.
         * @return Enum which matched the value, otherwise return the defaultEnum provided
         */
        public static Enum getEnumFromValue(Enum e, Integer value, Enum defaultEnum) {
            if (e == null) {
                throw new IllegalStateException("Enum object cannot be null");
            }
            Enum[] enums = e.getClass().getEnumConstants();

            for (Enum aE : enums) {
                if (!PersistentIntEnumable.class.isAssignableFrom(aE.getClass())) {
                    throw new IllegalArgumentException("Enum Must implement PersistentIntEnumable!");
                }
                final PersistentIntEnumable ge = (PersistentIntEnumable) aE;

                if (("" + ge.getValue()).equals(("" + value))) {
                    return aE;
                }
            }

            return defaultEnum;
        }
    }
}
