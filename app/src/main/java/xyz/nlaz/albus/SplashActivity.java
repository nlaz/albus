package xyz.nlaz.albus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private TextView signInPrompt;
    private Button signUpButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mAuth = FirebaseAuth.getInstance();

        signInPrompt = (TextView) findViewById(R.id.signInPrompt);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        signInPrompt.setOnClickListener(signInListener);
        signUpButton.setOnClickListener(signUpListener);
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /* Event Listeners */
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener(){
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
            if(firebaseAuth.getCurrentUser() != null) {
                /*TODO Add a if statement here checking if the user has memories currently stored in the database. IF they do, go directly to the ReviewMemories Activity, if not, go to Welcome Activity.*/
                startActivity(new Intent(SplashActivity.this, MomentsActivity.class));
            }
        }
    };

    private View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    };

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SplashActivity.this, AccountActivity.class));
        }
    };

}
