package tv.onsign.rc.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tv.onsign.rc.model.RemoteControl;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbControls.mDBHelper";
    public static final String TABLE_NAME = "controls";
    public static final String COLUMN_ID_CONTROL = "id_control";
    public static final String COLUMN_NAME = "mName";
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

    public boolean insertControl(String name, String date, Integer id, List<Integer> listIdButtons) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_ID_CONTROL, id);

        int index = 0;

        for(int i = 0; i < listIdButtons.size(); i ++){
            index ++;
            contentValues.put("id_0" + String.valueOf(index), listIdButtons.get(i));
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
                List<Integer> idButtons = new ArrayList<Integer>();

                for(int j = 1; j <= 4; j ++){
                    if(!controls.isNull(controls.getColumnIndex("id_0" + String.valueOf(j)))){
                        idButtons.add(controls.getInt(controls.getColumnIndex("id_0" + String.valueOf(j))));
                    }
                }

                remoteControls[i] = new RemoteControl(
                        controls.getString(controls.getColumnIndex("mName")),
                        controls.getString(controls.getColumnIndex("date")),
                        controls.getInt(controls.getColumnIndex("id_control")),
                        idButtons);
                i++;
            }while(controls.moveToNext());
        }

        return remoteControls;
    }

    public void deleteControl(Integer id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM controls WHERE id_control = " + id;
        database.execSQL(deleteQuery);
    }

    public boolean controlExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor controls = db.rawQuery("select * from controls where id_control = " + id, null);
        return (controls.getCount() > 0);
    }

    public int numberOfEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public boolean updateNameControl(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        db.update(TABLE_NAME, contentValues, "id_control = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public void update() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.execSQL("vacuum");
        db.close();
    }

    public RemoteControl getControl(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor controls = db.rawQuery("select * from controls where id_control = " + id, null);
        if (controls.getCount() == 0) return null;
        controls.moveToFirst();
        List<Integer> idButtons = new ArrayList<Integer>();

        for(int j = 1; j <= 4; j ++){
            if(!controls.isNull(controls.getColumnIndex("id_0" + String.valueOf(j)))){
                idButtons.add(controls.getInt(controls.getColumnIndex("id_0" + String.valueOf(j))));
            }
        }

        RemoteControl rc = new RemoteControl(
                controls.getString(controls.getColumnIndex("mName")),
                controls.getString(controls.getColumnIndex("date")),
                controls.getInt(controls.getColumnIndex("id_control")),
                idButtons);
        return rc;
    }
}