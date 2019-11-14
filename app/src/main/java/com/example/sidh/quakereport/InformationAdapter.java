package com.example.sidh.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sidh on 1/30/2017.
 */
public class InformationAdapter extends ArrayAdapter<Information>
{
    public InformationAdapter(Context context, ArrayList<Information> information)
    {
        super(context,0,information);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.listitem,parent,false);
        }
        Information currentInfo=getItem(position);
        setData(currentInfo,view);
        return view;
    }
    private void setData(Information currentInfo,View view)
    {
        TextView magnitude=(TextView)view.findViewById(R.id.magnitude);
        GradientDrawable circle=(GradientDrawable)magnitude.getBackground();
        double mag=currentInfo.getMag();
        int magColor=magnitudeColor(mag);
        int color=ContextCompat.getColor(getContext(),magColor);
        circle.setColor(color);
        DecimalFormat format=new DecimalFormat("0.0");
        magnitude.setText(format.format(mag));
        TextView location=(TextView)view.findViewById(R.id.location);
        TextView place = (TextView) view.findViewById(R.id.place);
        String loc=(currentInfo.getLoc());
        int index=loc.indexOf(" of ");
        if(index!=-1)
        {
            location.setText(loc.substring(0, index + 3));
            place.setText(loc.substring(index + 4));
        }
        else
        {
            location.setText(" ");
            place.setText(loc);
        }
        TextView dateText=(TextView) view.findViewById(R.id.date);
        Date date=new Date(currentInfo.getTime());
        SimpleDateFormat dateFormat=new SimpleDateFormat("LLL dd,yyyy");
        dateText.setText(dateFormat.format(date));
        TextView time=(TextView)view.findViewById(R.id.time);
        SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
        time.setText(timeFormat.format(date));
    }
    private int magnitudeColor(double mag)
    {
        int magnitude=(int) Math.floor(mag);
        switch (magnitude)
        {
            case 1: return R.color.magnitude1;
            case 2: return R.color.magnitude2;
            case 3: return R.color.magnitude3;
            case 4: return R.color.magnitude4;
            case 5: return R.color.magnitude5;
            case 6: return R.color.magnitude6;
            case 7: return R.color.magnitude7;
            case 8: return R.color.magnitude8;
            case 9: return R.color.magnitude9;
            default: return R.color.magnitude10plus;
        }
    }
}
