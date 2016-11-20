package xyz.nlaz.albus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.SQLiteHelper;
import models.Moment;

public class DailyReviewActivity extends AppCompatActivity {

    private List<Moment> objects;
    private TextView titleView;
    private TextView notesView;
    private Button nextButton;
    private RelativeLayout cardView;
    private LinearLayout reportView;
    private TextView reportText;
    private TextView progressText;
    private int itemTotal;
    private int itemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        titleView = (TextView) findViewById(R.id.item_title);
        notesView = (TextView) findViewById(R.id.item_notes);
        nextButton = (Button) findViewById(R.id.next_button);
        cardView = (RelativeLayout) findViewById(R.id.item_card);
        reportView = (LinearLayout) findViewById(R.id.report_view);
        reportText = (TextView) findViewById(R.id.report_text);
        progressText = (TextView) findViewById(R.id.review_progress);

        objects = new ArrayList<>();

        SQLiteHelper dbHelper = new SQLiteHelper(this);
        objects = generateDailyStack( dbHelper.getAllMoments() );

        itemCount = 0;
        itemTotal = objects.size();

        nextButton.setOnClickListener(nextListener);
        cardView.setOnClickListener(cardListener);

        renderView();
    }

    void renderView() {
        progressText.setText(itemCount + "/" + itemTotal);
        if ( objects.isEmpty() ) {
            cardView.setVisibility(View.GONE);
            reportView.setVisibility(View.VISIBLE);
            reportText.setText(String.format("Yay! You reviewed %d moments today.", itemTotal));

            nextButton.setVisibility(View.GONE);
        } else {
            Moment item = objects.remove(0);
            titleView.setText(item.getTitle());
            if (item.getDescription().isEmpty()) {
                notesView.setText("Oops! There's nothing here...");
            } else {
                notesView.setText(item.getDescription());
            }
        }
    }

    List<Moment> generateDailyStack( ArrayList<Moment> allMoments) {
        Collections.shuffle(allMoments);
        return allMoments.subList(0, Math.min(allMoments.size(), 3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daily_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.list_button:
                Toast.makeText(this, "Moments list selected", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(this, MomentsActivity.class);
                startActivity(addIntent);
                break;
        }
        return true;
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
}
