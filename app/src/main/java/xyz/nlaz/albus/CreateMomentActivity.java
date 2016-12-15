package xyz.nlaz.albus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Moment;

/**
 * CreateMomentActivity - Controls the logic for new moments.
 * Allows the user to create or update new items through a
 * form.
 */

public class CreateMomentActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText descriptionInput;
    private Button saveButton;
    private boolean isEditView = false;
    private Moment moment;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        Bundle bundle = getIntent().getExtras();

        Firebase.setAndroidContext(this);
        moment = new Moment();
        mAuth = FirebaseAuth.getInstance();

        final String user_id = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("Moments");

        titleInput = (EditText) findViewById(R.id.title);
        descriptionInput = (EditText) findViewById(R.id.description);
        saveButton = (Button) findViewById(R.id.button);

        if (bundle != null){
            isEditView = true;
            moment = bundle.getParcelable("moment");
            titleInput.setText(moment.getTitle());
            descriptionInput.setText(moment.getDescription());
        }

        saveButton.setOnClickListener(saveListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isEditView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.create_moment_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_item:
                deleteMoment(moment);
                finish();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                break;
        }
        return true;
    }

    /* Firebase Helpers */
    private void updateMoment(Moment m){
        mRef.child(m.getKey()).setValue(m);

        Toast.makeText(this, "Update Item: " + m.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void createMoment(Moment m) {
        String key = mRef.push().getKey();
        m.setKey(key);
        mRef.child(key).setValue(m);

        Toast.makeText(this, "New Item: " + m.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void deleteMoment(Moment m) {
        mRef.child(m.getKey()).removeValue();

        Toast.makeText(this, "Delete Item: " + m.getTitle(), Toast.LENGTH_SHORT).show();
    }

    /* Click Listener */

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (titleInput.getText().length() == 0) {
                Toast.makeText(v.getContext(), "You must enter a title", Toast.LENGTH_SHORT).show();
                return;
            } else {
                moment.setTitle( titleInput.getText().toString() );
                moment.setDescription( descriptionInput.getText().toString() );

                if (isEditView) {
                    updateMoment(moment);
                } else {
                    createMoment(moment);
                }
                finish();
            }
        }
    };
}
