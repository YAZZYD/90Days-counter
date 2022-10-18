package dz.iyb.recoverydaycount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
//Class to ensure communication with data base
public class DataBaseOperator extends SQLiteOpenHelper {
    public static final String TABLE_NAME="QUIT_DATE"; //table name
    public static final String DATEOFQUIT="DATEOFQUIT"; //column name

    public DataBaseOperator(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) { //when data base is created
        String CreateTable="CREATE TABLE "+TABLE_NAME+"( "+DATEOFQUIT+" TEXT)";
        DB.execSQL(CreateTable); //create table
    }
    public boolean addData(String date){ //add data
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(DATEOFQUIT,date);
        long insert = DB.insert(TABLE_NAME, null, value);
        DB.close();
        return insert!=-1;
    }
    public String getData(){ //get data
        SQLiteDatabase DB=this.getReadableDatabase();
        String Query="SELECT * FROM "+TABLE_NAME;
        Cursor cursor = DB.rawQuery(Query, null);
        if(cursor.moveToFirst()) {
            String date=cursor.getString(0);
            cursor.close();
            DB.close();
            return date;
        }else{
            cursor.close();
            DB.close();
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
