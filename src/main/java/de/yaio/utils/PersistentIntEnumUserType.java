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
package de.yaio.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * abstract class to support mapping of enums to specific Integer-values in hibernate
 * @param <T>
 */
public abstract class PersistentIntEnumUserType<T extends PersistentIntEnumable>  implements UserType, Serializable {
    private static final long serialVersionUID = -8993020929647717601L;
    private T FIRST_ENUM_ITEM;

    protected PersistentIntEnumUserType(Class enumClass) {
        if(enumClass.getEnumConstants()[0] instanceof PersistentIntEnumable) {
            FIRST_ENUM_ITEM = (T)enumClass.getEnumConstants()[0];
        } else {
            throw new IllegalStateException("The class " + enumClass + " MUST implement PersistentIntEnumable interface!");
        }
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        int code = rs.getInt(names[0]);
        if (rs.wasNull()) {
            return null;
        }

        Enum enumValue = FIRST_ENUM_ITEM.getEnumFromValue(code);
        if (enumValue != null) {
            return enumValue;
        }

        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " value:" + code);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.INTEGER);
        } else {
            st.setInt(index, ((PersistentIntEnumable) value).getValue());
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    @Override
    public abstract Class<T> returnedClass();

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.INTEGER};
    }
}