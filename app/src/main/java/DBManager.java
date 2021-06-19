import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DBHelper dbHelper;
    private String TBNAME;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(FlightItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flightID", item.getFlightID());
        values.put("dtime", item.getDtime());
        values.put("atime", item.getAtime());
        values.put("dplace", item.getDplace());
        values.put("aplace", item.getAplace());
        values.put("price", item.getPrice());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void addAll(List<FlightItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (FlightItem item : list) {
            ContentValues values = new ContentValues();
            values.put("flightID", item.getFlightID());
            values.put("dtime", item.getDtime());
            values.put("atime", item.getAtime());
            values.put("dplace", item.getDplace());
            values.put("aplace", item.getAplace());
            values.put("price", item.getPrice());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void update(FlightItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flightID", item.getFlightID());
        values.put("dtime", item.getDtime());
        values.put("atime", item.getAtime());
        values.put("dplace", item.getDplace());
        values.put("aplace", item.getAplace());
        values.put("price", item.getPrice());
        db.update(TBNAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public List<FlightItem> listAll(){
        List<FlightItem> rateList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<FlightItem>();
            while(cursor.moveToNext()){
                FlightItem item = new FlightItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setFlightID(cursor.getString(cursor.getColumnIndex("fLIGHT_ID")));
                item.setDtime(cursor.getString(cursor.getColumnIndex("D_TIME")));
                item.setAtime(cursor.getString(cursor.getColumnIndex("A_TIME")));
                item.setDplace(cursor.getString(cursor.getColumnIndex("D_PLACE")));
                item.setAplace(cursor.getString(cursor.getColumnIndex("A_PLACE")));
                item.setPrice(cursor.getFloat(cursor.getColumnIndex("PRICE")));

                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;

    }

    public FlightItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        FlightItem flightItem = null;
        if(cursor!=null && cursor.moveToFirst()){
            flightItem = new FlightItem();
            flightItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            flightItem.setFlightID(cursor.getString(cursor.getColumnIndex("fLIGHT_ID")));
            flightItem.setDtime(cursor.getString(cursor.getColumnIndex("D_TIME")));
            flightItem.setAtime(cursor.getString(cursor.getColumnIndex("A_TIME")));
            flightItem.setDplace(cursor.getString(cursor.getColumnIndex("D_PLACE")));
            flightItem.setAplace(cursor.getString(cursor.getColumnIndex("A_PLACE")));
            flightItem.setPrice(cursor.getFloat(cursor.getColumnIndex("PRICE")));
        }
        db.close();
        return flightItem;
    }
}