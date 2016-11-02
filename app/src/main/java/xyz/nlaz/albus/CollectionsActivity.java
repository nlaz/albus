package xyz.nlaz.albus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity {

    private GridView gridView;
    private GridAdapter adapter;
    private ArrayList<Collection> collections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collections_layout);

        collections = new ArrayList<Collection>();
        gridView = (GridView) findViewById(R.id.grid_view);

        Collection test = new Collection("Moments Test");
        collections.add(new Collection("Test 1"));
        collections.add(new Collection("Test 2"));
        collections.add(new Collection("Test 3"));
        collections.add(new Collection("Test 4"));

        ArrayList<Moment> fakeData = new ArrayList<Moment>();
        fakeData.add(new Moment("Just a Friend", "You say hes just a friend - Biz Markie"));
        fakeData.add(new Moment("Can I Kick it?", "A tribe called quest"));
        fakeData.add(new Moment("C.R.E.A.M", "Wu-Tang Clan"));

        test.setMoments(fakeData);

        collections.add(test);

        adapter = new GridAdapter(this, R.layout.collection_item, collections);
        gridView.setAdapter(adapter);
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
                intent.putExtra("collection", collections.get(position) );
                startActivity(intent);
            }
        };
    }
}
