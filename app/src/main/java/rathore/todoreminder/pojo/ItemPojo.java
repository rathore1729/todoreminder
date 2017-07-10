package rathore.todoreminder.pojo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class ItemPojo {
    private String title, time, date;
    private String ch;
    private int backColor,id;

    public ItemPojo(String title,String time,String date,String ch)
    {
        this.title = title;
        this.time = time;
        this.date = date;
        this.ch = ch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCh() {
        return ch;
    }

    public void setCh() {
        ch = getTitle().toUpperCase().charAt(0)+"";
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setBack()
    {
        int rd,grn,bl;
        rd = (int) (Math.random()*255);
        grn = (int) (Math.random()*255);
        bl = (int) (Math.random()*255);
        backColor = Color.rgb(rd,grn,bl);
    }

    public void setBack(int r, int g,int b)
    {
        backColor = Color.rgb(r,g,b);
    }

    public void setBack(int color){
        this.backColor = color;
    }

    public int getBack()
    {
        return backColor;
    }

    @Override
    public String toString() {
        return "ChatPojo{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", ch=" + ch +
                '}';
    }
}
