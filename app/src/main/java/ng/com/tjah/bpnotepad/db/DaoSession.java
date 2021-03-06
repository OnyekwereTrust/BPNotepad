package ng.com.tjah.bpnotepad.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import ng.com.tjah.bpnotepad.db.Measure;

import ng.com.tjah.bpnotepad.db.MeasureDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig measureDaoConfig;

    private final MeasureDao measureDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        measureDaoConfig = daoConfigMap.get(MeasureDao.class).clone();
        measureDaoConfig.initIdentityScope(type);

        measureDao = new MeasureDao(measureDaoConfig, this);

        registerDao(Measure.class, measureDao);
    }
    
    public void clear() {
        measureDaoConfig.clearIdentityScope();
    }

    public MeasureDao getMeasureDao() {
        return measureDao;
    }

}
