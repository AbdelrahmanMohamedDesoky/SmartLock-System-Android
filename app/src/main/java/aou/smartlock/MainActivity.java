package aou.smartlock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
        String email = settings.getString("Email","NONE");
        String password = settings.getString("Password","NONE");
        if (!(email.equals("NONE") || password.equals("NONE"))){
            if(Functions.login(email,password,this)){
                Intent eKeyActivity = new Intent(this,ControlPanelActivity.class);
                startActivity(eKeyActivity);
            } else {
                SharedPreferences newSettings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
                SharedPreferences.Editor editor = newSettings.edit();
                editor.remove("Email");
                editor.remove("Password");
                editor.apply();
                Toast.makeText(this,"Data Change Detected, Please relogin to save new data",Toast.LENGTH_LONG).show();
                Intent mainActivity = new Intent(this,MainActivity.class);
                startActivity(mainActivity);
            }
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void goToUrl(View view) {
        String url = "http://192.168.1.2/smartlock/index.php";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void startEKeyActivity(View view){
        Intent eKeyActivity = new Intent(this,EKeyActivity.class);
        startActivity(eKeyActivity);
    }

    public void login(View view){
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if(Functions.login(emailString,passwordString,this)){
            // Save Login Details State :D
            saveData(emailString,passwordString);
            Intent eKeyActivity = new Intent(this,ControlPanelActivity.class);
            startActivity(eKeyActivity);
        }
    }

    public void saveData(String email,String password){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Email",email);
        editor.putString("Password",password);
        editor.apply(); // save the data.
    }
}
