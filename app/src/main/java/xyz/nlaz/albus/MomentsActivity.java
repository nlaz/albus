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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MomentsActivity extends AppCompatActivity {

    private ListView listView;
    private String collectionId;
    private ArrayList<Moment> objects;
    private ViewAdapter adapter;
    private TextView emptyView;
    private Collection collection;
    private FirebaseDatabase database;
    private DatabaseReference collectionRef;
    private DatabaseReference momentsRef;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        emptyView = (TextView) findViewById(R.id.emptyView);

        listView.setEmptyView(emptyView);

        dbHelper = new SQLiteHelper(this);
        objects = dbHelper.getAllMoments();

        adapter = new ViewAdapter(this, R.layout.list_item, objects);
        listView.setAdapter(adapter);

        /* Commented out Firebase code */
//        Bundle bundle = getIntent().getExtras();

//        collection = bundle.getParcelable("collection");
//        collectionId = bundle.getString("collectionId");

//        database = FirebaseDatabase.getInstance();
//        collectionRef = database.getReference().child("collections").child(collectionId);

//        collectionRef.setValue(collection);
//        objects = collection.getMoments();

//        momentsRef = collectionRef.child("moments");
//        momentsRef.addChildEventListener(childListener);
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
                startActivityForResult(addIntent, 1);
                break;
            case R.id.review_button:
                if (objects.size() > 0) {
                    Toast.makeText(this, "Review selected", Toast.LENGTH_SHORT).show();
                    Intent reviewIntent = new Intent(this, ReviewActivity.class);
                    reviewIntent.putParcelableArrayListExtra("moments", objects);
                    startActivity(reviewIntent);
                } else {
                    Toast.makeText(this, "No items available", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String newTitle = data.getStringExtra("title");
                String newDescription = data.getStringExtra("description");
                Moment newMoment = new Moment(newTitle, newDescription);

                objects.add(newMoment);
                dbHelper.insertMoment(newMoment);
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(this, "Oops something bad happened!", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(MomentsActivity.this, CreateMomentActivity.class);
                    intent.putExtra()
                }
            });
            return view;
        }
    }

    /* Event Listener */
    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    };

    /* DB Listener */
    ChildEventListener childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Moment newMoment = dataSnapshot.getValue(Moment.class);
            objects.add(newMoment);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "MomentsActivity - onChildAdded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //TODO
            Toast.makeText(getApplicationContext(), "MomentsActivity - onChildChanged", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            //TODO
            Toast.makeText(MomentsActivity.this, "MomentsActivity - onChildRemoved", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //TODO
            Toast.makeText(MomentsActivity.this, "MomentsActivity - onChildMoved", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("CollectionsActivity", "MomentsActivity - loadPost:onCancelled", databaseError.toException());
        }
    };
}
