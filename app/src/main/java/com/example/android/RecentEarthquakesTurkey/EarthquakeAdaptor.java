package com.example.android.RecentEarthquakesTurkey;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cspr on 16.10.2017.
 */

public class EarthquakeAdaptor extends ArrayAdapter {
    private static final String LOG_TAG = Earthquake.class.getSimpleName();//Hangi classın adaptörü olduğunu söylemeyi UNUTMA


    public EarthquakeAdaptor( Context context, List<Earthquake> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //

        Earthquake currentEarthquake = (Earthquake) getItem(position);

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.mag);

        magnitudeTextView.setText( currentEarthquake.getmMagnitude());



        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor = currentEarthquake.getMagnitudeColor(getContext(),currentEarthquake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);



        //

        TextView locationTextView1 = (TextView) listItemView.findViewById(R.id.location1);

        locationTextView1.setText(currentEarthquake.getmLocation1());

        TextView locationTextView2 = (TextView) listItemView.findViewById(R.id.location2);

        locationTextView2.setText(currentEarthquake.getmLocation2());

        //

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        dateTextView.setText(currentEarthquake.getmDate());

        return listItemView;
    }
}
