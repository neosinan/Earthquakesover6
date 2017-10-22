package com.example.android.RecentEarthquakesTurkey;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class adjustment extends Fragment {

    EditText editText;
    Button button;
    ToggleButton toggleButton;


    public adjustment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_adjustment, container, false);

        editText = (EditText) rootView.findViewById(R.id.edit_queryy);
        button=(Button) rootView.findViewById(R.id.query_button);
        toggleButton=(ToggleButton) rootView.findViewById(R.id.toggleButton);

        toggleButton.setChecked(getBoolean());
        editText.setText(getPref());

        //Changes if The sharedpref about Magnitude
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeInfo(editText.getText().toString());
                EarthquakeFragment earthquakeFragment=new EarthquakeFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_Earthquake, earthquakeFragment,earthquakeFragment.getTag()).commit();

            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    storeBoolean(true);//this Changes if The sharedpref about Region
                    // find your fragment
                    EarthquakeFragment earthquakeFragment=new EarthquakeFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_Earthquake, earthquakeFragment,earthquakeFragment.getTag()).commit();
                }else {
                    storeBoolean(false);//Changes if The sharedpref about Region
                    EarthquakeFragment earthquakeFragment=new EarthquakeFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_Earthquake, earthquakeFragment,earthquakeFragment.getTag()).commit();

                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    //This 4 method saves and gets the shared prefrence
    private void storeInfo(String info){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("Mag",info);
        mEditor.apply();
    }

    private void storeBoolean(Boolean info){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("Region",info);
        mEditor.apply();
    }

    private String getPref(){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String stra= mSharedPreferences.getString("Mag","3");
        return stra;
    }
    private Boolean getBoolean(){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        Boolean stra= mSharedPreferences.getBoolean("Region",true);
        return stra;
    }
}
