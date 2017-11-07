package com.sr7d.myposts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView rv;
    FirePage firePage;

    private Button btnLog,btnSign;
    private EditText editTextEmail,editTextPassword;
    private ProgressDialog progressDialog;
    LinearLayout layoutLogin;
    private static final String TAG = "Login_Activity";
    FirebaseAuth.AuthStateListener mAuthListener;

    int totalPage,totalItem;
    int currentPage = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    checkUser();
                    if (layoutLogin.getVisibility() == View.VISIBLE) {
                        layoutLogin.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    } else {
                        layoutLogin.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                    return true;
                case R.id.navigation_next:
                    if (currentPage != totalPage) {
                        currentPage = currentPage + 1;
                        fireCall();
                    }
                    return true;
                case R.id.navigation_prev:
                    if (currentPage != 0) {
                        currentPage--;
                        fireCall();
                    }

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLog = (Button)findViewById(R.id.buttonLogin);
        btnSign = (Button)findViewById(R.id.buttonSignUp);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        editTextEmail = (EditText)findViewById(R.id.editTextEmail1);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword1);
        progressDialog = new ProgressDialog(this);
        layoutLogin = (LinearLayout)findViewById(R.id.layoutLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        rv = (RecyclerView) findViewById(R.id.mRecylcerID);
        rv.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference();

        fireCall();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private  void userLogin(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),"Email or Password can not be empty",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Login User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // start the activity
                            layoutLogin.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                        }
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    android.util.Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    android.util.Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }
    private void registerUser(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
//        String confirm = editTextPassword1.getText().toString();

        if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),"Email or Password can not be empty",Toast.LENGTH_SHORT).show();
            return;
        }
//        else if(password.equals(confirm)) {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registered Succeccfully", Toast.LENGTH_SHORT).show();
                                finish();
                                layoutLogin.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cant register.please try again", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Not Registered or check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
//        }

    }
    public void checkUser(){
        if (firebaseAuth.getCurrentUser() != null) {

            //proile activity
            String name = firebaseAuth.getCurrentUser().getEmail();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(name)
                    .setMessage("Already LoggedIn from this Account.")
                    .setCancelable(false)
                    .setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.signOut();
                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            layoutLogin.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                        }
                    })
                    .show();
        }
        if (firebaseAuth.getCurrentUser() == null) {
            Snackbar.make(layoutLogin.getRootView(), "Sign In for best result", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
    }
    public void fireCall(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                totalItem = ((int) dataSnapshot.getChildrenCount());
                totalPage = totalItem / 5;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                totalItem = ((int) dataSnapshot.getChildrenCount());
                totalPage = totalItem / 5;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        firePage = new FirePage(this,rv,currentPage);
        firePage.refreshData();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
