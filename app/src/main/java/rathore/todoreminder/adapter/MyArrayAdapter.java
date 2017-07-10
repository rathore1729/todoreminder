package rathore.todoreminder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import rathore.todoreminder.R;
import rathore.todoreminder.activity.MainActivity;
import rathore.todoreminder.pojo.ItemPojo;

public class MyArrayAdapter extends ArrayAdapter {
    private Context context;
    private int layoutSource;
    private ArrayList<ItemPojo> arrayList;
    private LayoutInflater inflater;

    public MyArrayAdapter(Context context,int layoutSource, ArrayList<ItemPojo> arrayList)
    {
        super(context,layoutSource,arrayList);
        this.context = context;
        this.layoutSource = layoutSource;
        this.arrayList = arrayList;

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(layoutSource,null);

        TextView title = (TextView) view.findViewById(R.id.textViewTitle);
        TextView time = (TextView) view.findViewById(R.id.textViewTime);
        TextView date = (TextView) view.findViewById(R.id.textViewDate);
        TextView ch = (TextView) view.findViewById(R.id.textViewCh);

        ItemPojo item = arrayList.get(position);

        title.setText(item.getTitle());
        time.setText(item.getTime());
        date.setText(item.getDate());
        ch.setText(item.getCh());
        GradientDrawable back = (GradientDrawable) ch.getBackground();
        back.setColor(item.getBack());

        ch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item selected is at Position : " + (position + 1), Toast.LENGTH_SHORT).show();
        }
        });

        return view;
    }
}