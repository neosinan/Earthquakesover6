package com.example.android.RecentEarthquakesTurkey;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by cspr on 16.10.2017.
 */

public class Earthquake {
    private String mMagnitude;
    private String mLocation1;
    private String mLocation2;
    private String mDate;
    private int mColor;
    private String mURL;

    public Earthquake(String magnitude,String location1,String location2, String date,String URLString){
        mMagnitude=magnitude;
        mLocation1 = location1;
        mLocation2 = location2;
        mDate=date;
        mURL=URLString;
    }

    public int getMagnitudeColor(Context context, String colorOfMag) {
        int colorInt = 0;
        Double tempMagnitude = Double.parseDouble(colorOfMag);
       if (tempMagnitude<2){
           colorInt =  ContextCompat.getColor(context, R.color.magnitude1);
       }else if (tempMagnitude<3){
           colorInt =  ContextCompat.getColor(context, R.color.magnitude2);
       }else if (tempMagnitude<4){
           colorInt =  ContextCompat.getColor(context, R.color.magnitude3);
       }else if (tempMagnitude<5){
           colorInt =  ContextCompat.getColor(context, R.color.magnitude4);
       }else if (tempMagnitude<6){
            colorInt =  ContextCompat.getColor(context, R.color.magnitude5);
        }else if (tempMagnitude<7){
            colorInt =  ContextCompat.getColor(context, R.color.magnitude6);
        }else if (tempMagnitude<8){
            colorInt =  ContextCompat.getColor(context, R.color.magnitude7);
        }else if (tempMagnitude<9){
            colorInt =  ContextCompat.getColor(context, R.color.magnitude8);
        }
        return colorInt;
    }

    public String getmURL(){
        return mURL;
    }

    public String getmMagnitude(){return mMagnitude;};

    public String getmLocation1(){return mLocation1;};

    public String getmLocation2(){return mLocation2;};

    public String getmDate(){return mDate;};
}
