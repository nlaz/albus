package xyz.nlaz.albus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Moment> objects;
    private ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        objects = new ArrayList<Moment>();
        adapter = new ViewAdapter(this, R.layout.list_item, objects);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_button:
                Toast.makeText(this, "Add selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CreateItemActivity.class);
                startActivityForResult(intent, 1);
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
                Toast.makeText(this, newTitle + ": " + newDescription, Toast.LENGTH_SHORT).show();
                objects.add(newMoment);
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

        public ViewAdapter(Context context, int resource, ArrayList<Moment> objects) {
            super(context, resource, objects);

            this.context = context;
            this.resource = resource;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(resource, parent, false);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(this.objects.get(position).getTitle());
            return view;
        }
    }
}
