package xyz.nlaz.albus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import models.Moment;

/**
 * MomentsActivity - Controls the logic for the Moments
 * ListView. This activity displays memory items in a list
 * format and allows user to update and remove items.
 */

public class MomentsActivity extends AppCompatActivity {

    private ArrayList<Moment> objects;
    private ViewAdapter adapter;
    private ListView listView;
    private TextView emptyView;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    public static final int REQUEST_CODE_EDIT = 1;
    public static final int REQUEST_CODE_NEW = 2;
    public static final int RESULT_CODE_DELETE = 3;
    public static final int RESULT_CODE_UPDATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_activity);
        objects = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);
        emptyView = (TextView) findViewById(R.id.emptyView);
        mAuth = FirebaseAuth.getInstance();
        listView.setEmptyView(emptyView);

        final String user_id = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("Moments");
        mRef.addValueEventListener(valueEventListener);

        adapter = new ViewAdapter(this, R.layout.moments_item, objects);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_button:
                Toast.makeText(this, "Add selected", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(this, CreateMomentActivity.class);
                startActivityForResult(addIntent, REQUEST_CODE_NEW);
                break;
            case android.R.id.home:
                Toast.makeText(this, "Review selected", Toast.LENGTH_SHORT).show();
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
                Intent logout = new Intent(this, LoginActivity.class);
                startActivityForResult(logout, REQUEST_CODE_NEW);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW:
                if (resultCode == Activity.RESULT_OK) {
                    // New Moment Created
                    Moment moment = data.getParcelableExtra("moment");
                    String key = mRef.push().getKey();
                    moment.setKey(key);
                    mRef.child(key).setValue(moment);

                    Toast.makeText(this, "New Item: " + moment.getKey(), Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_EDIT:
                if (resultCode == RESULT_CODE_UPDATE) {
                    // Existing Moment Edited
                    Moment moment = data.getParcelableExtra("moment");
                    mRef.child(moment.getKey()).setValue(moment);

                    Toast.makeText(this, "Update Item", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CODE_DELETE) {
                    // Existing Moment Deleted
                    Moment moment = data.getParcelableExtra("moment");
                    mRef.child(moment.getKey()).removeValue();

                    Toast.makeText(this, "Delete Item: " + moment.getKey(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "I'm not sure what to do :(", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /* Array Adapter */
    private class ViewAdapter extends ArrayAdapter<Moment> {

        private Context context;
        private int resource;
        private List<Moment> objects;

        ViewAdapter(Context context, int resource, ArrayList<Moment> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.objects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(resource, parent, false);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(this.objects.get(position).getTitle());
            view.setOnLongClickListener( getLongClickListener(position) );
            return view;
        }
    }

    /* Event Listener */
    private View.OnLongClickListener getLongClickListener(final int position) {
       return new View.OnLongClickListener() {

           @Override
           public boolean onLongClick(View v) {
               Intent intent = new Intent(MomentsActivity.this, CreateMomentActivity.class);
               intent.putExtra("moment", objects.get(position));
               startActivityForResult(intent, REQUEST_CODE_EDIT);
               return false;
           }
       };
    }

    /* Firebase Listener */
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            objects.clear();
            for(DataSnapshot mydata : dataSnapshot.getChildren()){
                Moment m = mydata.getValue(Moment.class);
                m.setKey(mydata.getKey());
                objects.add(m);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("DatabaseError", databaseError.toString());
        }
    };
}
