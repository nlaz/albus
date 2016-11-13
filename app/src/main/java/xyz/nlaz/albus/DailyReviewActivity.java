package xyz.nlaz.albus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DailyReviewActivity extends AppCompatActivity {

    private ArrayList<Moment> objects;
    private TextView titleView;
    private TextView notesView;
    private Button nextButton;
    private RelativeLayout cardView;
    private LinearLayout reportView;
    private TextView reportText;
    private int momentsReviewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        titleView = (TextView) findViewById(R.id.item_title);
        notesView = (TextView) findViewById(R.id.item_notes);
        nextButton = (Button) findViewById(R.id.next_button);
        cardView = (RelativeLayout) findViewById(R.id.item_card);
        reportView = (LinearLayout) findViewById(R.id.report_view);
        reportText = (TextView) findViewById(R.id.report_text);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            objects = bundle.getParcelableArrayList("moments");
        }
        objects = new ArrayList<>();
        momentsReviewed = objects.size();

        nextButton.setOnClickListener(nextListener);
        cardView.setOnClickListener(cardListener);

        renderCard();
    }

    void renderCard() {
        if(objects.size() > 0 ){
            Moment item = objects.remove(0);
            titleView.setText(item.getTitle());
            reportText.setText(item.getDescription());
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
    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (objects.isEmpty()) {
                cardView.setVisibility(View.GONE);
                reportView.setVisibility(View.VISIBLE);
                reportText.setText(String.format("You just reviewed %d moments.", momentsReviewed));

                nextButton.setText("FINISH");
                nextButton.setOnClickListener(finishListener);
            } else { renderCard(); }
        }
    };

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleView(notesView);
        }
    };
}
