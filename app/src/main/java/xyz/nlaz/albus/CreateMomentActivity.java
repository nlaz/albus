package xyz.nlaz.albus;

import android.app.Activity;
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

import static xyz.nlaz.albus.MomentsActivity.REQUEST_CODE_NEW;

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
    private Firebase mRef;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        Bundle bundle = getIntent().getExtras();

        Firebase.setAndroidContext(this);
        moment = new Moment();
        mAuth = FirebaseAuth.getInstance();

        mRef = new Firebase("https://albus-22d13.firebaseio.com/Users/");

        titleInput = (EditText) findViewById(R.id.title);
        descriptionInput = (EditText) findViewById(R.id.description);
        saveButton = (Button) findViewById(R.id.button);

        if (bundle != null){
            isEditView = true;
            moment = bundle.getParcelable("moment");
            Toast.makeText(this, Integer.toString( moment.getId() ), Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent();
                i.putExtra("moment", moment);
                setResult(MomentsActivity.RESULT_CODE_DELETE, i);
                finish();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_CODE_NEW);
                break;
            case R.id.logout:
                mAuth.signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(this, SplashActivity.class);
                startActivityForResult(logout, REQUEST_CODE_NEW);
                break;
        }
        return true;
    }

    /* Click Listener */

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (titleInput.getText().length() == 0) {
                Toast.makeText(v.getContext(), "You must enter a title", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent i = new Intent();
                int resultCode = isEditView ? MomentsActivity.RESULT_CODE_UPDATE : Activity.RESULT_OK;
                moment.setTitle(titleInput.getText().toString());
                moment.setDescription(descriptionInput.getText().toString());
                String user_id = mAuth.getCurrentUser().getUid();
                Firebase mRefUser = mRef.child(user_id);
                Firebase index = mRefUser.child("Moments");
                //figure out how many moments are under a specific user
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String key = mDatabase.child("/Users/" + user_id + "/Moments").push().getKey();
                Firebase moments = index.child(key);
                Moments newMoment = new Moments(titleInput.getText().toString(),descriptionInput.getText().toString(),0);
                index.child(key).setValue(newMoment);

                i.putExtra("moment", moment);
                setResult(resultCode, i);
                DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(); //Getting root reference
                DatabaseReference myRef = myRef1.child("message"); //Write your child reference if any
                myRef.setValue("Hello, World!");
                finish();
                startActivity(new Intent(CreateMomentActivity.this, ReviewActivity.class));
            }
        }
    };
}
