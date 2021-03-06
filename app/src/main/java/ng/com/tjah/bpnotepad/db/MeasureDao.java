package ng.com.tjah.bpnotepad.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEASURE".
*/
public class MeasureDao extends AbstractDao<Measure, Long> {

    public static final String TABLENAME = "MEASURE";

    /**
     * Properties of entity Measure.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bp_sys = new Property(1, short.class, "bp_sys", false, "BP_SYS");
        public final static Property Bp_dia = new Property(2, short.class, "bp_dia", false, "BP_DIA");
        public final static Property Bp_pul = new Property(3, short.class, "bp_pul", false, "BP_PUL");
        public final static Property Time = new Property(4, long.class, "time", false, "TIME");
        public final static Property Date = new Property(5, long.class, "date", false, "DATE");
    }


    public MeasureDao(DaoConfig config) {
        super(config);
    }
    
    public MeasureDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEASURE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BP_SYS\" INTEGER NOT NULL ," + // 1: bp_sys
                "\"BP_DIA\" INTEGER NOT NULL ," + // 2: bp_dia
                "\"BP_PUL\" INTEGER NOT NULL ," + // 3: bp_pul
                "\"TIME\" INTEGER NOT NULL ," + // 4: time
                "\"DATE\" INTEGER NOT NULL );"); // 5: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEASURE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Measure entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getBp_sys());
        stmt.bindLong(3, entity.getBp_dia());
        stmt.bindLong(4, entity.getBp_pul());
        stmt.bindLong(5, entity.getTime());
        stmt.bindLong(6, entity.getDate());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Measure entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getBp_sys());
        stmt.bindLong(3, entity.getBp_dia());
        stmt.bindLong(4, entity.getBp_pul());
        stmt.bindLong(5, entity.getTime());
        stmt.bindLong(6, entity.getDate());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Measure readEntity(Cursor cursor, int offset) {
        Measure entity = new Measure( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1), // bp_sys
            cursor.getShort(offset + 2), // bp_dia
            cursor.getShort(offset + 3), // bp_pul
            cursor.getLong(offset + 4), // time
            cursor.getLong(offset + 5) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Measure entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBp_sys(cursor.getShort(offset + 1));
        entity.setBp_dia(cursor.getShort(offset + 2));
        entity.setBp_pul(cursor.getShort(offset + 3));
        entity.setTime(cursor.getLong(offset + 4));
        entity.setDate(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Measure entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Measure entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Measure entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
