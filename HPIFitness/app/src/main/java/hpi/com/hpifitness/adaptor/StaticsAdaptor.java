package hpi.com.hpifitness.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hpi.com.hpifitness.R;
import hpi.com.hpifitness.entity.Walk;

/**
 * Created by Georgey on 26-01-2017.
 */


public class StaticsAdaptor extends ArrayAdapter<Walk> {

    private Context mContext;
    private ArrayList<Walk> mArrayListWalk;


    public StaticsAdaptor(Context context, ArrayList<Walk> object) {
        super(context, R.layout.list_walk, object);
        this.mContext = context;
        this.mArrayListWalk = object;
    }

    @Override
    public int getCount() {
        return mArrayListWalk.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        UI ui;

        if (null == convertView) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_walk, null);

            ui = new UI();


            ui.date = (TextView) rowView.findViewById(R.id.tv_date);
            ui.distance = (TextView) rowView.findViewById(R.id.tv_distance);
            ui.milestone = (TextView) rowView.findViewById(R.id.tv_milestone);

            rowView.setTag(ui);
        } else {
            ui = (UI) rowView.getTag();
        }

        final Walk walk = mArrayListWalk.get(position);
        String date;
        try {
            date = walk.getDate().split(" ")[1] +" "+walk.getDate().split(" ")[2]+" " + walk.getDate().split(" ")[5];
        }
        catch (Exception ex){
            date = walk.getDate();
        }
        ui.date.setText(date);
        ui.distance.setText(String.format("%.2f", Float.valueOf(walk.getDistance()) * 3.28) + " Foot");
        ui.milestone.setText(walk.getMilestone() + " Milestone");

        return rowView;
    }

    static class UI {
        TextView date, distance, milestone;
    }
}
