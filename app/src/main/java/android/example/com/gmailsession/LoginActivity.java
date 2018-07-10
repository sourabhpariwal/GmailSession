package android.example.com.gmailsession;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    LinearLayout linearLayout;
    ImageView imageView;
    TextView name, mail;
    Button sign_out;
    SignInButton signInButton;
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient googleApiClient;
    int req_code = 100;

    static String USERID = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);

        linearLayout = findViewById(R.id.profile);
        imageView = findViewById(R.id.iv);
        name = findViewById(R.id.tv_name);
        mail = findViewById(R.id.tv_mail);
        sign_out = findViewById(R.id.btn_sign_out);
        signInButton = findViewById(R.id.btn_login);
        linearLayout.setVisibility(View.GONE);

        signInButton.setOnClickListener(this);
        sign_out.setOnClickListener(this);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_login):
                login();
                break;
            case (R.id.btn_sign_out):
                sign_out();
                break;
        }

    }

    private void sign_out() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void login() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, req_code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == req_code && data != null) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(signInResult);

        }
    }

    private void handleResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            GoogleSignInAccount account = signInResult.getSignInAccount();
            try {
                String name = account.getDisplayName();
                String email = account.getEmail();
                Uri uri = account.getPhotoUrl();

                this.name.setText(name);

                editor = sharedPreferences.edit();
                editor.putString(USERID, email);
                editor.apply();


                Toast.makeText(LoginActivity.this, "Values has been Stored ", Toast.LENGTH_LONG).show();
                String id = sharedPreferences.getString(USERID, null);
                System.out.println(id);



                mail.setText(email);
                if (uri == null) {
                    imageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(this).load(uri.toString()).into(imageView);
                }

                updateUI(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            linearLayout.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
