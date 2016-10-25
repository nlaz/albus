package xyz.nlaz.albus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateItemActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText descriptionInput;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        titleInput = (EditText) findViewById(R.id.title);
        descriptionInput = (EditText) findViewById(R.id.description);
        saveButton = (Button) findViewById(R.id.button);

        saveButton.setOnClickListener(saveListener);
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (titleInput.getText().length() == 0) {
                Toast.makeText(v.getContext(), "You must enter a title", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent i = new Intent();
                i.putExtra("title", titleInput.getText().toString());
                i.putExtra("description", descriptionInput.getText().toString());
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    };
}
