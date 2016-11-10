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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity {

    private GridView gridView;
    private GridAdapter adapter;
    private ArrayList<String> collectionIds;
    private ArrayList<Collection> collections;
    private ArrayList<DatabaseReference> objectRefs;
    private FirebaseDatabase database;
    private DatabaseReference collectionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collections_layout);

        collections = new ArrayList<Collection>();
        collectionIds = new ArrayList<String>();
        gridView = (GridView) findViewById(R.id.grid_view);

//        Collection test = new Collection();
//        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("Pensieve", MODE_PRIVATE, null);

        //DB Test
//        database = FirebaseDatabase.getInstance();
//        collectionsRef = database.getReference().child("collections");
//        collectionsRef.addChildEventListener(childListener);
//        collectionsRef.addValueEventListener(postListener);


        adapter = new GridAdapter(this, R.layout.collection_item, collections);
        gridView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.collection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_button:
                Toast.makeText(this, "Add button selected!", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(this, CreateCollectionActivity.class);
                startActivityForResult(addIntent, 1);
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
                Collection newCollection = new Collection(newTitle);
                ArrayList<Moment> moments = new ArrayList<>();

                moments.add(new Moment("Riff Raff", "test"));
                moments.add(new Moment("Diplo", "test"));

                newCollection.setMoments(moments);

                DatabaseReference newCollectionRef = collectionsRef.push();
                newCollectionRef.setValue(newCollection);
            } else {
                Toast.makeText(this, "Oops something bad happened!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /* Collection Adapter */
    private class GridAdapter extends ArrayAdapter<Collection> {

        private Context context;
        private int resource;
        private List<Collection> objects;

        GridAdapter(Context context, int resource, ArrayList<Collection> objects) {
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

            TextView title = (TextView) view.findViewById(R.id.collection_title);
            title.setText(objects.get(position).getName());

            view.setOnClickListener(getCollectionListener(position));

            return view;
        }
    }

    /* Click Listeners */
    private View.OnClickListener getCollectionListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollectionsActivity.this, "Selected: " + collections.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CollectionsActivity.this, MomentsActivity.class);
                intent.putExtra("collectionId", collectionIds.get(position));
                intent.putExtra("collection", collections.get(position) );
                startActivity(intent);
            }
        };
    }

    /* DB Listener */
    ChildEventListener childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Collection newCollection = dataSnapshot.getValue(Collection.class);
            collections.add(newCollection);
            collectionIds.add(dataSnapshot.getKey());
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "onChildAdded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //TODO
            Collection child = dataSnapshot.getValue(Collection.class);
            String id = dataSnapshot.getKey();
            collections.set(collectionIds.indexOf(id), child);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "onChildChanged", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            //TODO
            Toast.makeText(CollectionsActivity.this, "onChildRemoved", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //TODO
            Toast.makeText(CollectionsActivity.this, "onChildMoved", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("CollectionsActivity", "loadPost:onCancelled", databaseError.toException());
        }
    };
}
