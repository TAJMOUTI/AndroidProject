package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ExpandableListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotebookActivity extends AppCompatActivity {

    // MEMBER DATA
    final private String TAG = "NotebookActivity";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String USER_ID;
    private ArrayList<Map> notebook;                                // the complete notebook with all meals, come from the database
    private List<String> timeOfDailyMeals;                          // groups for the expandable list view
    private HashMap<String, List<String>> ingredientsOfDailyMeal;   // items per group for the expandable list view
    private CalendarView calendarView;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        // INITIALIZE VARIABLES
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        USER_ID = user.getUid();
        notebook = new ArrayList<Map>();
        timeOfDailyMeals = new ArrayList<String>();
        ingredientsOfDailyMeal = new HashMap<String, List<String>>();
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        expandableListView = findViewById(R.id.expandableListView);
        adapter = new ExpandableListAdapter(getApplicationContext(), timeOfDailyMeals, ingredientsOfDailyMeal);
        expandableListView.setAdapter(adapter);


        // get back notebook of this user
        db.collection("users").document(USER_ID).collection("notebook")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            notebook.add(document.getData());
                        }
                        // sort notebook by date;
                        notebook.sort(new DateSorter());
                        // once we have the note book, we get back today meals
                        Calendar cal = Calendar.getInstance();
                        Log.d(TAG, "onComplete: today's date : " + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH));
                        getDailyMeals(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        Log.d(TAG, "onComplete: notebook = " + notebook.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        // manage the calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                getDailyMeals(year, month, dayOfMonth);
            }
        });
    }

    /**
     * get the data of meals for a selected date
     * this function actualize these two variables : timeOfDailyMeals, ingredientsOfDailyMeal
     * @param year : the year selected
     * @param month : the month selected [0-11]
     * @param dayOfMonth : the day selected
     */
    public void getDailyMeals(int year, int month, int dayOfMonth) {
        // first reset data for daily meals
        timeOfDailyMeals.clear();
        ingredientsOfDailyMeal.clear();
        // loop through the notebook
        for(int i = 0; i < notebook.size(); i++) {
            // get the current elements
            Map e = (Map) notebook.get(i);
            // get the Timestamp of the current element
            Timestamp timestamp = (Timestamp) e.get("date");
            // convert the timestamp into a calendar date
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp.toDate());
            // check if years, month, dayOfMonth are equals
            if(cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month && cal.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                // if equal we store the data
                String timeOfMeal = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "h " + String.valueOf(cal.get(Calendar.MINUTE));
                timeOfDailyMeals.add(timeOfMeal);
                ingredientsOfDailyMeal.put(timeOfMeal, (List) e.get("list"));
                Log.d(TAG, "getDailyMeals: time of daily meals : " + timeOfDailyMeals);
                Log.d(TAG, "getDailyMeals: ingredients of daly meals : " + ingredientsOfDailyMeal);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * comparator for Map, by Timestamp
     * Map object must have a field "date" of type Timestamp
     * used to sort from recent to old
     */
    public class DateSorter implements Comparator<Map>
    {
        @Override
        public int compare(Map m1, Map m2) {
            Timestamp date1 = (Timestamp) m1.get("date"), date2 = (Timestamp) m2.get("date");
            return date2.compareTo(date1);
        }
    }
}