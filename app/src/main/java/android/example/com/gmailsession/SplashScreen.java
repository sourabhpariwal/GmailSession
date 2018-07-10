package android.example.com.gmailsession;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.gmailsession.LoginActivity;
import android.example.com.gmailsession.MainActivity;
import android.example.com.gmailsession.R;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progbar);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreference", MODE_PRIVATE);
        if (sharedPreferences.getString(LoginActivity.USERID, null) != null) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else

        {
            progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progress < 100) {
                        progress += 1;
                        progressBar.setProgress(progress);
                        progressBar.getMax();
                        try {
                            // Sleep for 200 milliseconds.
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);


        }
    }
}


