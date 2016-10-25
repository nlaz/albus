package xyz.nlaz.albus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> data;
    private ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        data = new ArrayList<String>();
        adapter = new ViewAdapter(this, R.layout.list_item, data);

        listView.setAdapter(adapter);

        for(int i = 0; i < 10; i++) {
            data.add("Test " + i);
        }
    }

    private class ViewAdapter extends ArrayAdapter<String> {

        private Context context;
        private int resource;
        private List<String> objects;

        public ViewAdapter(Context context, int resource, ArrayList<String> objects) {
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
            title.setText(this.objects.get(position));
            return view;
        }
    }
}
