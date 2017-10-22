/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.RecentEarthquakesTurkey;

import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;



public class EarthquakeActivity extends AppCompatActivity {

    //Log tag to determine where the errors occured
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    Boolean mOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        EarthquakeFragment earthquakeFragment=new EarthquakeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_Earthquake, earthquakeFragment,earthquakeFragment.getTag()).commit();
    }



    //that is how menu button is infleted
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mOpen==false){
            //respond to menu item selection
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                adjustment adjustment = new adjustment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.settings_frag, adjustment, adjustment.getTag()).commit();
                RelativeLayout rea = (RelativeLayout) findViewById(R.id.settings_frag);
                rea.setVisibility(View.VISIBLE);
                mOpen=true;
            }
        }else {
            //this makes settings fragments disapper
            RelativeLayout rea = (RelativeLayout) findViewById(R.id.settings_frag);
            rea.setVisibility(View.GONE);
            mOpen=false;
        }
        return super.onOptionsItemSelected(item);
    }



}
