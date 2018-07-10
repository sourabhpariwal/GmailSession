package android.example.com.gmailsession;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.gmailsession.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("sharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("key1","");
                editor.clear();
                editor.commit();

                startActivity(new Intent(MainActivity.this, SplashScreen.class));
                finish();
            }
        });
    }
}
