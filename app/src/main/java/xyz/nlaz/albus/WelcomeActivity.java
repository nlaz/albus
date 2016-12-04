package xyz.nlaz.albus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Nick on 12/3/2016.
 */
public class WelcomeActivity extends AppCompatActivity {
    private Button mCreateMemoryBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mCreateMemoryBtn = (Button) findViewById(R.id.createMemory);

        mCreateMemoryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            startActivity(new Intent(WelcomeActivity.this, CreateMomentActivity.class));
            }
        });
    }
}
