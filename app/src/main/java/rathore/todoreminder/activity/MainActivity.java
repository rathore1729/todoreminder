package rathore.todoreminder.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.Inflater;

import rathore.todoreminder.R;
import rathore.todoreminder.adapter.MyArrayAdapter;
import rathore.todoreminder.databse.MyOpenHelper;
import rathore.todoreminder.pojo.ItemPojo;
import rathore.todoreminder.tables.ToDoTable;

public class MainActivity extends AppCompatActivity {

    ArrayList<ItemPojo> arrItems = new ArrayList<>();
    MyArrayAdapter arrayAdapter;

    ListView list;
    ImageButton date,time,submit,editCancel;
    EditText text;
    TextView textDate,textTime;

    String txt, dt="", tm="";
    int hour,minute,curYear,curMonth,curDay;
    int flag=0,timeFlag=0,dateFlag=0,editFlag=0;
    private String alarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        methodListeners();

        arrayAdapter = new MyArrayAdapter(this, R.layout.listitem, arrItems);
        fetchItems();
        if(flag==0) addFooter();
        list.setAdapter(arrayAdapter);
        Intent i = getIntent();
        try{
        if(i.getIntExtra("flag",0)==1)
            showReminder(i.getIntExtra("ID",0),i.getIntExtra("position",0));
            Toast.makeText(this, i.getIntExtra("ID",0)+ " & " + i.getIntExtra("position",0), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){}
    }

    private void init()
    {
        list = (ListView) findViewById(R.id.list);
        text = (EditText) findViewById(R.id.text);
        date = (ImageButton) findViewById(R.id.date);
        time = (ImageButton) findViewById(R.id.time);
        submit = (ImageButton) findViewById(R.id.submit);
        editCancel = (ImageButton) findViewById(R.id.editCancel);
        textDate = (TextView) findViewById(R.id.textDate);
        textTime = (TextView) findViewById(R.id.textTime);
    }

    private void methodListeners()
    {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReminder();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeypad();
                if(flag!=0 && editFlag==0)
                    itemMenu(position);
                else if(editFlag!=0)
                    Toast.makeText(MainActivity.this, "COMPLETE RUNNING EDIT OPERATION FIRST", Toast.LENGTH_SHORT).show();
            }
        });

        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                homeScreen();
                editFlag = 0;
            }
        });
    }

    private void timeDialog()
    {
        hideKeypad();
        if(hour==0){
        final Calendar calender = Calendar.getInstance();
        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);}
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {

                timeFlag=0;
                if(i<hour)
                    timeFlag=1;
                else if(hour==i)
                    if(i1<=minute)
                        timeFlag=1;

                String noon= " AM";
                hour=i;minute=i1;
                alarmTime = hour + ":" + minute + ":00";

                if(dateFlag==1 && timeFlag==1)
                    errorTime();
                else
                {
                    if(i>=12)
                    {
                        i=i-12;
                        noon = " PM";
                    }

                    if(i==0)
                        i=12;

                    tm = i + ":" + i1 +noon;
                    if(i<=9)
                        tm = "0" + i + ":" + i1 +noon;
                    if(i1<=9)
                        tm = i + ":" + "0" + i1 +noon;
                    if(i<=9 && i1<=9)
                        tm = "0" + i + ":" + "0" + i1 +noon;

                    textTime.setText(tm);
                    textTime.setError(null);
                    textTime.setHintTextColor(Color.rgb(130,218,255));
                }
            }
        },hour,minute,false);
        dialog.show();
    }

    private void dateDialog()
    {
        hideKeypad();
        if(curYear==0){
        Calendar calender = Calendar.getInstance();
        curYear = calender.get(Calendar.YEAR);
        curMonth = calender.get(Calendar.MONTH);
        curDay = calender.get(Calendar.DAY_OF_MONTH);}
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mnth, int dy)
            {

                dateFlag=0;
                if(yr<=curYear && mnth<=curMonth && dy<=curDay)
                {
                    if(dy==curDay)
                    {
                        dateFlag = 1;
                        if(timeFlag == 1)
                            errorTime();
                    }
                    else
                    {
                        errorDate();
                        return;
                    }
                }


                if(dy<10)
                    dt = "0" + dy + "/" + (mnth+1) + "/" + yr;
                if((mnth+1)<10)
                    dt = dy + "/0" + (mnth+1) + "/" + yr;
                if(dy<10 && (mnth+1)<10)
                    dt = "0" + dy + "/0" + (mnth+1) + "/" + yr;
                textDate.setText(dt);
                textDate.setError(null);
                textDate.setHintTextColor(Color.rgb(130,218,255));
            }
        },curYear,curMonth,curDay);
        dialog.show();
    }



    private void addReminder()
    {
        hideKeypad();
        int validation = 0 ;

        dt = textDate.getText().toString();
        if(dt.equals(""))
        {
            Toast.makeText(this, "Please Enter Date.", Toast.LENGTH_SHORT).show();
            textDate.setText("");
            textDate.setHint("--- Select Time --- ");
            textDate.setHintTextColor(Color.rgb(255,97,97));
            textDate.setError("Please Enter Date");
        }
        else
            validation++;

        tm = textTime.getText().toString();
        if(tm.equals(""))
        {
            Toast.makeText(this, "Please Enter Time.", Toast.LENGTH_SHORT).show();
            textTime.setText("");
            textTime.setHint("--- Select Time --- ");
            textTime.setHintTextColor(Color.rgb(255,97,97));
            textTime.setError("Please Enter Time");
        }
        else
            validation++;

        txt = text.getText().toString().trim();
        if(txt.equals(""))
        {
            text.setText("");
            text.setHint("It seems empty Friend!!!");
            text.setError("Please enter a valid reminder.");
        }
        else
            validation++;

        if(validation==3)
        {
            if(flag==0)
            {
                arrItems.remove(0);
            }

            String ch = txt.toUpperCase().charAt(0)+"";
            ItemPojo item = new ItemPojo(txt,tm,dt,ch);

            if(editFlag==0) {
                item.setBack();
                ContentValues cv = new ContentValues();
                cv.put(ToDoTable.REMIND,txt);
                cv.put(ToDoTable.TIME,tm);
                cv.put(ToDoTable.DATE,dt);
                cv.put(ToDoTable.COLOR,item.getBack());

                if(ToDoTable.insert(new MyOpenHelper(this).getWritableDatabase(),cv)>0.0){
                arrItems.add(item);
                Intent i = new Intent();

                Cursor cursor = ToDoTable.select(new MyOpenHelper(MainActivity.this).getReadableDatabase(),null);
                cursor.moveToLast();
                int id = cursor.getInt(0);
                    i.putExtra("ID",id);
                    i.putExtra("position",arrItems.size()-1);
                    i.setAction("myalarm");
                    i.putExtra("msg",txt);
                    Toast.makeText(this, id + " & yet " + (arrItems.size()-1) , Toast.LENGTH_SHORT).show();

                PendingIntent pendingIntent =  PendingIntent.getBroadcast(this, id, i, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Date date = null;
                try {
                        date = sdf.parse(dt + " " + hour + ":" + minute + ":00");
                        Toast.makeText(this, "Alarm set on  : " + dt + " at " + tm, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Toast.makeText(this, "Error in setting alarm", Toast.LENGTH_SHORT).show();
                    }
                    homeScreen();

                    if (date != null) {
                        alarmManager.set(AlarmManager.RTC, date.getTime(), pendingIntent);
                    }
                Toast.makeText(this, "NEW REMINDER ADDED", Toast.LENGTH_SHORT).show();
                flag++;
                }
            }
            else
                editReminder(editFlag-1,item);

            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void itemMenu(final int position)
    {
        AlertDialog dialog;
        String items[] = {"EDIT","DELETE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.edit);
        builder.setTitle("Edit or Delete existing Reminder");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchItems();
                if(i==0)
                {
                    editFlag = position + 1;
                    editScreen(position);
                }
                else
                    deleteItem(position);
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void errorTime()
    {
        textTime.setText("");
        textTime.setHintTextColor(Color.rgb(255,97,97));
        textTime.setHint("--- Select Date --- ");
        textTime.setError("INVALID TIME.");
        Toast.makeText(MainActivity.this, "THAT FALLS IN THE PAST", Toast.LENGTH_SHORT).show();
        timeFlag = 0;
    }

    private void errorDate()
    {
        textDate.setText("");
        textDate.setHintTextColor(Color.rgb(255,97,97));
        textDate.setHint("--- Select Date --- ");
        textDate.setError("INVALID TIME.");
        Toast.makeText(MainActivity.this, "THAT FALLS IN THE PAST", Toast.LENGTH_SHORT).show();
        dateFlag=0;
    }

    public void addFooter()
    {
        ItemPojo item = new ItemPojo("Remind me to add a reminder","","","R");
        item.setBack(70,46,250);
        arrItems.add(item);
    }

    public void deleteItem(final int position)
    {
        AlertDialog dialogDeleteConfirm;
        AlertDialog.Builder builderDeleteConfirm = new AlertDialog.Builder(this);
        builderDeleteConfirm.setIcon(R.mipmap.warning);
        builderDeleteConfirm.setTitle("Warning!!!");
        builderDeleteConfirm.setMessage("Sure to delete Item?");

        builderDeleteConfirm.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ItemPojo item = arrItems.get(position);
                if(ToDoTable.delete(new MyOpenHelper(MainActivity.this).getWritableDatabase(),ToDoTable.ID + " = '" + item.getId() + "'")>0){
                    Toast.makeText(MainActivity.this, "Reminder Deleted", Toast.LENGTH_SHORT).show();
                    arrItems.remove(position);
                    flag--;
                    if(flag==0)
                        addFooter();
                    arrayAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(MainActivity.this, "Delete Operation failed!!!", Toast.LENGTH_SHORT).show();
            }
        });

        builderDeleteConfirm.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        dialogDeleteConfirm = builderDeleteConfirm.create();
        dialogDeleteConfirm.show();
    }

    public void editReminder(final int position, final ItemPojo item)
    {
        AlertDialog editConfirmDialog;
        AlertDialog.Builder editConfirmBuilder = new AlertDialog.Builder(this);
        editConfirmBuilder.setIcon(R.mipmap.warning);
        editConfirmBuilder.setTitle("Sure to edit your Reminder?");

        editConfirmBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setId(arrItems.get(position).getId());
                item.setBack(arrItems.get(position).getBack());
                ContentValues cv = new ContentValues();
                cv.put(ToDoTable.ID,item.getId());
                cv.put(ToDoTable.REMIND,item.getTitle());
                cv.put(ToDoTable.TIME,item.getTime());
                cv.put(ToDoTable.DATE,item.getDate());
                cv.put(ToDoTable.COLOR,item.getBack());
                arrItems.remove(position);
                arrItems.add(position,item);
                arrayAdapter.notifyDataSetChanged();
                if(ToDoTable.update(new MyOpenHelper(MainActivity.this).getWritableDatabase(),cv,ToDoTable.ID + " = '" + item.getId() + "'")>0){
                    Toast.makeText(MainActivity.this, "Reminder edited successfully", Toast.LENGTH_SHORT).show();
                    homeScreen();
                    editFlag = 0;
                }
                else
                    Toast.makeText(MainActivity.this, "Edit operation failed!!!", Toast.LENGTH_SHORT).show();
            }
        });

        editConfirmBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        editConfirmDialog=editConfirmBuilder.create();
        editConfirmDialog.show();
    }

    public void fetchItems(){
        arrItems.clear();
        flag = 0;
        Cursor cursor = ToDoTable.select(new MyOpenHelper(this).getReadableDatabase(),null);
        if(cursor!=null)
            while(cursor.moveToNext()){
                ItemPojo item = new ItemPojo(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(1).toUpperCase().charAt(0)+"");
                item.setBack(cursor.getInt(4));
                item.setId(cursor.getInt(0));
                arrItems.add(item);
                flag++;
            }
        cursor.close();
        arrayAdapter.notifyDataSetChanged();
    }

    public void editScreen(int position)
    {
        text.setText(arrItems.get(position).getTitle());
        text.setSelection(text.length());
        textTime.setText(arrItems.get(position).getTime());

        String[] times = textTime.getText().toString().substring(0,5).split(":");
        hour = Integer.parseInt(times[0]);
        minute = Integer.parseInt(times[1]);
        textDate.setText(arrItems.get(position).getDate());

        String[] dates = textDate.getText().toString().split("/");
        curDay = Integer.parseInt(dates[0]);
        curMonth = Integer.parseInt(dates[1])-1;
        curYear = Integer.parseInt(dates[2]);

        text.setError(null);
        textTime.setError(null);
        textDate.setError(null);
        RelativeLayout editRel = (RelativeLayout) findViewById(R.id.editRel) ;
        editRel.setVisibility(View.VISIBLE);
    }

    public void homeScreen()
    {
        text.setText("");
        text.setHint("When and What to Remind?");
        textDate.setText("");
        textDate.setHint("--- Select Date --- ");
        textDate.setError(null);
        textDate.setHintTextColor(Color.rgb(130,218,255));
        textTime.setText("");
        textTime.setHint("--- Select Time --- ");
        textTime.setError(null);
        textTime.setHintTextColor(Color.rgb(130,218,255));
        RelativeLayout editRel = (RelativeLayout) findViewById(R.id.editRel) ;
        editRel.setVisibility(View.GONE);
        timeFlag=0;dateFlag=0;
        curYear=0;curMonth=0;curDay=0;
        hour=0;minute=0;
    }

    public void hideKeypad(){
        InputMethodManager inMeth = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inMeth.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    public void showReminder(final int id, final int position){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ItemPojo item = arrItems.get(position);
        builder.setTitle(item.getTitle());
        builder.setMessage( "On : " + item.getDate() + "\nAt  : " + item.getTime());
        builder.setPositiveButton("Reschedule", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editFlag = position + 1;
                text.setText(arrItems.get(position).getTitle());
                text.setSelection(text.length());
                RelativeLayout editRel = (RelativeLayout) findViewById(R.id.editRel) ;
                editRel.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ToDoTable.delete(new MyOpenHelper(MainActivity.this).getWritableDatabase(), ToDoTable.ID + " = '" + id + "'")>0) {
                    arrItems.remove(position);
                    flag--;
                    if (flag == 0)
                        addFooter();
                    arrayAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(MainActivity.this, "Delete operation failed!!!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
