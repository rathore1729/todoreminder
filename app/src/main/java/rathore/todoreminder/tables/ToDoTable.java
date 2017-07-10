package rathore.todoreminder.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ToDoTable{
    public static final String ID = "ID";
    public static final String TABLE_NAME= "ToDoTable";
    public static final String REMIND = "Remind";
    public static final String TIME = "Time";
    public static final String DATE = "Date";
    public static final String COLOR = "Color";
    public static final String query = "CREATE TABLE `ToDoTable` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `Remind` TEXT, `Time` TEXT, `Date` TEXT, 'Color' INTEGER)";

    public static void createTable(SQLiteDatabase db){db.execSQL(query);}

    public static void upgradeTable(SQLiteDatabase db){
        db.execSQL("drop table if exists " + TABLE_NAME);
        createTable(db);
    }

    public static Cursor select(SQLiteDatabase db,String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insertOrThrow(TABLE_NAME,null,cv);
    }

    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }

    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }
}