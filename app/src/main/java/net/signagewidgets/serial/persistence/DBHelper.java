package net.signagewidgets.serial.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.util.Logging;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static Logging sLogging = new Logging(DBHelper.class);

    public static final String DATABASE_NAME = "dbControls.db";
    public static final String TABLE_NAME = "controls";
    public static final String COLUMN_ID_CONTROL = "id_control";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID_01 = "id_01";
    public static final String COLUMN_ID_02 = "id_02";
    public static final String COLUMN_ID_03 = "id_03";
    public static final String COLUMN_ID_04 = "id_04";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME +
                        "( "
                        + COLUMN_ID_CONTROL + " integer not null, "
                        + COLUMN_NAME + " text not null, "
                        + COLUMN_DATE + " text not null, "
                        + COLUMN_ID_01 + " integer, "
                        + COLUMN_ID_02 + " integer, "
                        + COLUMN_ID_03 + " integer, "
                        + COLUMN_ID_04 + " integer);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS controls");
        onCreate(db);
    }

    public boolean insertControl(String name, String date, Long id, List<Long> listIdButtons) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_ID_CONTROL, id);

        int indice = 0;

        for(int i = 0; i < listIdButtons.size(); i ++){
            indice ++;
            contentValues.put("id_0" + String.valueOf(indice), listIdButtons.get(i));
        }

        db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return true;
    }

    public RemoteControl[] getAllControls(){

        int numberOfControls = numberOfEntries();

        RemoteControl [] remoteControls;

        remoteControls = new RemoteControl[numberOfControls];

        if(numberOfControls > 0){

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor controls = db.rawQuery("select * from controls", null);

            int i = 0;

            controls.moveToFirst();

            do{
                List<Long> idButtons = new ArrayList<Long>();

                for(int j = 1; j <= 4; j ++){

                    if(!controls.isNull(controls.getColumnIndex("id_0" + String.valueOf(j)))){
                        idButtons.add(controls.getLong(controls.getColumnIndex("id_0" + String.valueOf(j))));
                    }
                }

                remoteControls[i] = new RemoteControl(
                        controls.getString(controls.getColumnIndex("name")),
                        controls.getString(controls.getColumnIndex("date")),
                        controls.getLong(controls.getColumnIndex("id_control")),
                        idButtons);
                i++;
            }while(controls.moveToNext());
        }

        return remoteControls;
    }

    public void deleteControl(Integer id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM control WHERE id = " + id;

        database.execSQL(deleteQuery);
    }

    public int numberOfEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public boolean updateNameControl(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public void update() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.execSQL("vacuum");
        db.close();
    }

}