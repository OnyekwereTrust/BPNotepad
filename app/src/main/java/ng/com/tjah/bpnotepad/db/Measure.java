package ng.com.tjah.bpnotepad.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "MEASURE".
 */
@Entity
public class Measure {

    @Id(autoincrement = true)
    private Long id;
    private short bp_sys;
    private short bp_dia;
    private short bp_pul;
    private long time;
    private long date;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Measure() {
    }

    public Measure(Long id) {
        this.id = id;
    }

    @Generated
    public Measure(Long id, short bp_sys, short bp_dia, short bp_pul, long time, long date) {
        this.id = id;
        this.bp_sys = bp_sys;
        this.bp_dia = bp_dia;
        this.bp_pul = bp_pul;
        this.time = time;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getBp_sys() {
        return bp_sys;
    }

    public void setBp_sys(short bp_sys) {
        this.bp_sys = bp_sys;
    }

    public short getBp_dia() {
        return bp_dia;
    }

    public void setBp_dia(short bp_dia) {
        this.bp_dia = bp_dia;
    }

    public short getBp_pul() {
        return bp_pul;
    }

    public void setBp_pul(short bp_pul) {
        this.bp_pul = bp_pul;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}