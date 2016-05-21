package com.checkpoint.andela.gmaps;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.checkpoint.andela.mytracker.adapters.LocationListAdapter;
import com.checkpoint.andela.mytracker.fragments.ListDatePicker;
import com.checkpoint.andela.mytracker.helpers.ActivityLauncher;
import com.checkpoint.andela.mytracker.helpers.DBManager;
import com.checkpoint.andela.mytracker.helpers.Setting;
import com.checkpoint.andela.mytracker.helpers.TrackerDbHelper;
import com.checkpoint.andela.mytracker.model.Places;
import com.checkpoint.andela.mytracker.model.TrackerModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class ListByLocation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private RecyclerView recyclerView;
   // private ListByDateAdapter listByDateAdapter;
    private LocationListAdapter dateListAdapter;
    private ArrayList<TrackerModel> trackerModelArrayList;
    private DateTime dateTime;
    private DBManager dbManager;
    private TextView textView;
    private ListView listView;
    private ArrayList<Places> placesArrayList;
    private TrackerDbHelper trackerDbHelper;
    private Places places;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_by_date_toolbar);
        setSupportActionBar(toolbar);
      //  getListByDate();
        getLocations();
        initializeComponents();
    }

   /* public void getListByDate() {

        trackerModelArrayList = new ArrayList<>();
        TrackerDbHelper trackerDbHelper = new TrackerDbHelper(this);
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
    }*/
    public void getLocations() {
        dateTime = new DateTime();
        places = new Places();
        trackerModelArrayList = new ArrayList<>();
        trackerDbHelper = new TrackerDbHelper(this);
        dbManager = new DBManager(trackerDbHelper);
        trackerModelArrayList = dbManager.listAll();
        placesArrayList = places.getPlaces(trackerModelArrayList);


    }

    public void initializeComponents() {
       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.list_date_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        listByDateAdapter = new ListByDateAdapter(this, trackerModels);
        recyclerView.setAdapter(listByDateAdapter);*/
        dateListAdapter = new LocationListAdapter(this, placesArrayList);
        listView = (ListView) findViewById(R.id.listview_by_date_note);
        listView.setAdapter(dateListAdapter);

        textView = (TextView) findViewById(R.id.head_date_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            ActivityLauncher.runIntent(this, Home.class);
            finish();
        }
        if (id == R.id.nav_settings) {
            ActivityLauncher.runIntent(this, PreferenceSettings.class);
        }
        if (id == R.id.nav_date) {
            dateTime = DateTime.now();
            ListDatePicker datePicker = new ListDatePicker();
            Bundle arguments = new Bundle();
            arguments.putString(ListDatePicker.DEFAULT, dateTime.toString());
            datePicker.setArguments(arguments);
            datePicker.setListDateListener(this);
            datePicker.show(getSupportFragmentManager(), "dialogpicker");

        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        trackerModelArrayList = dbManager.listByDate(dateTime, null);
        if (Setting.checkCurrentDate(dateTime, DateTime.now())) {
            textView.setText("Today");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEE, MMMM d, y");
        textView.setText(dateTime.toString(dateTimeFormatter));
        List<TrackerModel> modelList = dbManager.listByDate(dateTime, null);
        trackerModelArrayList.clear();
        dateListAdapter.notifyDataSetChanged();
        for (TrackerModel model: modelList) {
            int position = findPosition(model);
            if (position < 0) {
                trackerModelArrayList.add(model);
                //dateListAdapter.notifyDataSetChanged(trackerModelArrayList.size() - 1);
                dateListAdapter.notifyDataSetChanged();
            } else {
                trackerModelArrayList.set(position, model);
                dateListAdapter.notifyDataSetChanged();
               // dateListAdapter.notifyItemChanged(position);
            }
        }


    }

    private int findPosition(TrackerModel model) {
        for (int i = 0; i < trackerModelArrayList.size() - 1; i++) {
            if (model.getTracker_id() == (trackerModelArrayList.get(i).getTracker_id())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        ActivityLauncher.runIntent(this, Home.class);
        finish();
    }
}
