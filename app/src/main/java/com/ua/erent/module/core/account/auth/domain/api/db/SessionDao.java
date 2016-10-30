package com.ua.erent.module.core.account.auth.domain.api.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * <p>
 *     Base dao for session
 * </p>
 * Created by Максим on 10/30/2016.
 */

public final class SessionDao extends BaseDaoImpl<SessionPO, Integer> {

    public SessionDao(Class<SessionPO> dataClass) throws SQLException {
        super(dataClass);
    }

    public SessionDao(ConnectionSource connectionSource, Class<SessionPO> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public SessionDao(ConnectionSource connectionSource, DatabaseTableConfig<SessionPO> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

}
