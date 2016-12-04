package xyz.nlaz.albus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
    private EditText mNameField;
    private EditText mPasswordField;
    private EditText mEmailField;
    private  EditText mPasswordFieldConfirm;
    private Button mRegisterBtn;

    //Firebase stuffs
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Sign Up");
        setContentView(R.layout.account_activity);

        mNameField = (EditText) findViewById(R.id.nameRegister);
        mPasswordField = (EditText) findViewById(R.id.passwordRegister);
        mEmailField = (EditText) findViewById(R.id.emailRegister);
        mPasswordFieldConfirm = (EditText) findViewById(R.id.passwordRegisterConfirmation);
        mRegisterBtn = (Button) findViewById(R.id.registerButton);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://albus-22d13.firebaseio.com/Users");

        //More Firebase stuff
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            startRegister();
            }
        });
    }

    private void startRegister() {
        final String name = mNameField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String passwordConfirm = mPasswordFieldConfirm.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)){
            if(password.length() < 7){
                Toast.makeText(AccountActivity.this, "Your password must be over 7 characters", Toast.LENGTH_LONG).show();
            } else {
                if (passwordConfirm.equals(password)) {
                    mProgress.setMessage("Signing Up...");
                    mProgress.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user_id);
                                current_user_db.child("name").setValue(name);
                                current_user_db.child("image").setValue(email);
                                Firebase mRefUser = mRef.child(user_id);
                                Firebase mRefName = mRefUser.child("Name");
                                mRefName.setValue(name);
                                Firebase mRefEmail = mRefUser.child("Email");
                                mRefEmail.setValue(email);

                                mProgress.dismiss();

                                Intent loginIntent = new Intent(AccountActivity.this, SplashActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(AccountActivity.this, "Registration Failed, Check Internet Connectivity", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(AccountActivity.this, "Either your username or password is empty", Toast.LENGTH_LONG).show();
        }
    }
}
