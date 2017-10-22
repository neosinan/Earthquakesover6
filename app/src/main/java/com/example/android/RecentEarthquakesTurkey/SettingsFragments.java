package com.example.android.RecentEarthquakesTurkey;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by cspr on 22.10.2017.
 */

public class SettingsFragments extends android.support.v4.app.Fragment {

    EditText editText;
    Button button;


    public SettingsFragments() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_settings, container, false);
        editText= (EditText) rootView.findViewById(R.id.edit_queryy);
        button=(Button) rootView.findViewById(R.id.query_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }


}
