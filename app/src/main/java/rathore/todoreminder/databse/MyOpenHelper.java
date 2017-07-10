package rathore.todoreminder.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import rathore.todoreminder.tables.ToDoTable;

/**
 * Created by Rathore on 24-Jun-17.
 */

public class MyOpenHelper extends SQLiteOpenHelper{
    private Context context;
    
    public MyOpenHelper(Context context){
        super(context,"MyDB.db",null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "Database created", Toast.LENGTH_SHORT).show();
        ToDoTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "Database Upgraded", Toast.LENGTH_SHORT).show();
        ToDoTable.upgradeTable(db);
    }
}
