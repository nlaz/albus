package xyz.nlaz.albus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private ArrayList<Moment> objects;
    private TextView titleView;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        titleView = (TextView) findViewById(R.id.title);
        nextButton = (Button) findViewById(R.id.next_button);

        Bundle bundle = getIntent().getExtras();
        objects = bundle.getParcelableArrayList("moments");

        nextButton.setOnClickListener(nextListener);

        renderCard();
    }

    void renderCard() {
        Moment item = objects.remove(0);
        titleView.setText(item.getTitle());
    }

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (objects.size() == 0) { finish(); }
            else { renderCard(); }
        }
    };
}
