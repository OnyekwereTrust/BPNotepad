package ng.com.tjah.greendaogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "ng.com.tjah.bpnotepad.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addMeasureEntity(schema);
    }

    // This is use to describe the columns of your table
    private static Entity addMeasureEntity(final Schema schema) {
        Entity hypertension = schema.addEntity("Measure");
        hypertension.addIdProperty().primaryKey().autoincrement();
        hypertension.addShortProperty("bp_sys").notNull();
        hypertension.addShortProperty("bp_dia").notNull();
        hypertension.addShortProperty("bp_pul").notNull();
        hypertension.addLongProperty("time").notNull();
        hypertension.addLongProperty("date").notNull();
        //grocery.addStringProperty("status");
        return hypertension;
    }

}
