package com.example.android.RecentEarthquakesTurkey;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class adjustment extends Fragment {

    EditText editText;
    Button button;
    ToggleButton toggleButton;
    Spinner spinner;

    //Setup constants
    private static int ONE_MONTH = 1;
    private static int TWO_MONTHS = 2;
    private static int THREE_MOTHS = 3;
    private static int SIX_MONTHS = 6;
    public int mMonths=0;



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
        spinner=(Spinner) rootView.findViewById(R.id.spin);

        toggleButton.setChecked(getBoolean());
        editText.setText(getPref());

        spinnersetup();

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
                    // find your fragment which you wanna refresh
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

    private void spinnersetup(){
        //Create Adaptor for Spinner
        ArrayAdapter monthsSpinnerAdaptor=ArrayAdapter.createFromResource(getActivity(),R.array.array_months,android.R.layout.simple_spinner_item);

        //Specify layout Style
        monthsSpinnerAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //ToDo set where it is last selected/ has some problems
        spinner.setSelection(getMonthsItem());

        //Apply Adaptor
        spinner.setAdapter(monthsSpinnerAdaptor);

        //set itemclick listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.OneMonth))) {
                        storeMonths(ONE_MONTH);
                    } else if (selection.equals(getString(R.string.TwoMonths))) {
                        storeMonths(TWO_MONTHS);
                    } else if(selection.equals(getString(R.string.ThreeMoths))) {
                        storeMonths(THREE_MOTHS);
                    }else if (selection.equals(getString(R.string.SixMonths))){
                        storeMonths(SIX_MONTHS);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                storeMonths(THREE_MOTHS);
            }
        });
    }

    private int getMonthsItem(){
        int value=getMonths();
        int returnValue=0;
        if (value==1){
            returnValue=0;
        } else if (value==2){
            returnValue=1;
        } else if (value==3){
            returnValue=2;
        } else if (value==6){
            returnValue=3;
        }
        return returnValue;
    }

    //This 6 method saves and gets the shared prefrence
    private void storeInfo(String info){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("Mag",info);
        mEditor.apply();
    }
    private void storeMonths(int info){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("Month",info);
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
    private int getMonths(){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        int stra= mSharedPreferences.getInt("Month",3);
        return stra;
    }

}
