package xyz.nlaz.albus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import db.SQLiteHelper;
import models.Moment;

/**
 * MomentsActivity - Controls the logic for the Moments
 * ListView. This activity displays memory items in a list
 * format and allows user to update and remove items.
 */

public class MomentsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Moment> objects;
    private ViewAdapter adapter;
    private TextView emptyView;
    private SQLiteHelper dbHelper;

    public static final int REQUEST_CODE_EDIT = 1;
    public static final int REQUEST_CODE_NEW = 2;
    public static final int RESULT_CODE_DELETE = 3;
    public static final int RESULT_CODE_UPDATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_activity);
        listView = (ListView) findViewById(R.id.listview);
        emptyView = (TextView) findViewById(R.id.emptyView);

        listView.setEmptyView(emptyView);

        dbHelper = new SQLiteHelper(this);
        objects = dbHelper.getAllMoments();

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
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW:
                if (resultCode == Activity.RESULT_OK) {
                    Moment newMoment = data.getParcelableExtra("moment");
                    int id = dbHelper.insertMoment(newMoment);
                    newMoment.setId(id);
                    objects.add(newMoment);
                    Toast.makeText(this, "New Item: " + newMoment.getId(), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_EDIT:
                if (resultCode == RESULT_CODE_UPDATE) {
                    Toast.makeText(this, "Update Item", Toast.LENGTH_SHORT).show();
                    Moment moment = data.getParcelableExtra("moment");
                    dbHelper.updateMoment(moment.getId(), moment);
                    updateMomentInList(moment);
                } else if (resultCode == RESULT_CODE_DELETE) {
                    Moment moment = data.getParcelableExtra("moment");
                    Toast.makeText(this, "Delete Item: " + moment.getId(), Toast.LENGTH_SHORT).show();
                    dbHelper.deleteMomentById(moment.getId());
                    removeMomentInList(moment);
                }
                break;
            default:
                Toast.makeText(this, "I'm not sure what to do :(", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /* Helper Methods */
    private void updateMomentInList(Moment m) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getId().equals(m.getId())) {
                objects.set(i, m);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void removeMomentInList(Moment m) {
        for (Moment moment: objects) {
            if (moment.getId().equals(m.getId())) {
                objects.remove(moment);
                adapter.notifyDataSetChanged();
                return;
            }
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
}
