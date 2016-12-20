package xyz.nlaz.albus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Moment;

/**
 * ReviewActivity - Controls the logic for the Review
 * screen which is launched on app start. This activity pulls
 * any memory items from the SQLite database and setups up the views
 * to control the logic for reviewing those items.
 */

public class ReviewActivity extends AppCompatActivity {

    private List<Moment> objects;
    private TextView titleView;
    private TextView notesView;
    private Button nextButton;
    private RelativeLayout cardView;
    private LinearLayout reportView;
    private TextView reportText;
    private ProgressBar progressBar;
    private Button emptyViewButton;
    private RelativeLayout activityLayout;
    private RelativeLayout emptyViewLayout;

    private int itemTotal;
    private int itemCount;
    private static int REVIEW_LIMIT = 10;
    private static boolean DEBUG = true;

    //to read the moments from firebase copy this code
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        mAuth = FirebaseAuth.getInstance();

        activityLayout = (RelativeLayout) findViewById(R.id.review_layout);
        emptyViewLayout = (RelativeLayout) findViewById(R.id.emptyView);
        emptyViewButton = (Button) findViewById(R.id.create_moment_button);

        titleView = (TextView) findViewById(R.id.item_title);
        notesView = (TextView) findViewById(R.id.item_notes);
        nextButton = (Button) findViewById(R.id.next_button);
        cardView = (RelativeLayout) findViewById(R.id.item_card);
        reportView = (LinearLayout) findViewById(R.id.report_view);
        reportText = (TextView) findViewById(R.id.report_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        objects = new ArrayList<>();
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        int lastSave = prefs.getInt("lastUpdate", -1);
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        if (!DEBUG && lastSave != -1 && lastSave == dayOfYear) {
            Toast.makeText(this, "You've already reviewed today. New items tomorrow!", Toast.LENGTH_SHORT).show();
        } else {
            /* Fetch memory items from db */
            final String user_id = mAuth.getCurrentUser().getUid();
            Firebase.setAndroidContext(this);
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("Moments");
            mRef.addValueEventListener(firebaseListener);
        }

        nextButton.setOnClickListener(nextListener);
        cardView.setOnClickListener(cardListener);
        emptyViewButton.setOnClickListener(emptyViewListener);

        if (objects.size() > 0) {
            showEmptyLayout(false);
            resetView();
        } else {
            showEmptyLayout(true);
        }
    }

    protected void resetView() {
        itemCount = 0;
        itemTotal = objects.size();
        cardView.setVisibility(View.VISIBLE);
        reportView.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
        renderView();
    }
    /**
     * renderView - Updates card view states depending
     * on whether there are any objects left to review
     */
    void renderView() {
        double progress = (itemTotal > 0) ? ((double) itemCount / itemTotal) * 100 : 0;
        progressBar.setProgress((int) progress);
        if ( objects.isEmpty() ) {
            cardView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            reportView.setVisibility(View.VISIBLE);

            reportText.setText(String.format("Yay! You reviewed %d notes today.", itemTotal));
        } else {
            Moment item = objects.remove(0);
            titleView.setText(item.getTitle());
            if (item.getDescription().isEmpty()) {
                notesView.setText("Oops there's nothing here yet. Edit item to add description.");
            } else {
                notesView.setText(item.getDescription());
            }
            item.incrementReviewCount();
            //TODO Increment review count in firebase
//            dbHelper.updateMoment(item.getId(), item);
        }
    }

    void saveTodayDate() {
        Calendar today = Calendar.getInstance();
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("lastUpdate", today.get(Calendar.DAY_OF_YEAR));
        editor.apply();
    }

    List<Moment> generateDailyStack( List<Moment> allMoments) {
        Collections.sort(allMoments, momentComparator);
        saveTodayDate();
        return allMoments.subList(0, Math.min(allMoments.size(), REVIEW_LIMIT));
    }

    private Comparator<Moment> momentComparator = new Comparator<Moment>() {
        @Override
        public int compare(Moment lhs, Moment rhs) {
            return lhs.getReviewCount().compareTo(rhs.getReviewCount());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.list_button:
                //Toast.makeText(this, "Moments list selected", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(this, MomentsActivity.class);
                startActivity(addIntent);
                break;
            case R.id.settings:
                //Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.logout:
                mAuth.signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(this, SplashActivity.class);
                startActivity(logout);
                break;
        }
        return true;
    }

    void showEmptyLayout(boolean visible) {
        if (visible) {
            emptyViewLayout.setVisibility(View.VISIBLE);
            activityLayout.setVisibility(View.GONE);
        } else {
            emptyViewLayout.setVisibility(View.GONE);
            activityLayout.setVisibility(View.VISIBLE);
        }
    }

    void toggleView(View v) {
        if (v.getVisibility() == View.GONE){
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    /* View Listeners */
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemCount++;
            renderView();
        }
    };

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (notesView.getText().length() > 0) {
                toggleView(notesView);
            }
        }
    };

    private View.OnClickListener emptyViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(ReviewActivity.this, CreateMomentActivity.class);
            startActivity(i);
        }
    };

    /* Firebase Listeners */
    private ValueEventListener firebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Moment> data = new ArrayList<Moment>();
            for(DataSnapshot obj : dataSnapshot.getChildren()){
                Moment m = obj.getValue(Moment.class);
                m.setKey(obj.getKey());
                data.add(m);
            }

            objects = generateDailyStack(data);
            if (objects.size() > 0) {
                showEmptyLayout(false);
                resetView();
            } else {
                showEmptyLayout(true);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("Database Error", databaseError.toString());
        }
    };
}
