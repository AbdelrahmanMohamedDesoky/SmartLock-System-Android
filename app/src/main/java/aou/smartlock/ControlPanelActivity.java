package aou.smartlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ControlPanelActivity extends AppCompatActivity {
    TextView greeting_txtView;
    EditText deviceId_editText;
    public boolean wannaContinue = false;
    Button btn_startUnlockService;
    Button btn_startRemoteUnlock;
    Button btn_addDeviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Functions.response = "";
        Functions.deviceId = "";
        Functions.addDeviceResponse = "";
        greeting_txtView = (TextView) findViewById(R.id.greeting_txtview);
        deviceId_editText = (EditText) findViewById(R.id.deviceId_editText);
        btn_startUnlockService = (Button) findViewById(R.id.btn_startAutoService);
        btn_startRemoteUnlock = (Button) findViewById(R.id.btn_requestRemote);
        btn_addDeviceId = (Button) findViewById(R.id.btn_addDeviceId);

        greeting_txtView.setText("Welcome ! " + Functions.retrieveData(getEmail(),getPassword(),"name",this));

        // Device ID FOUND !
        if(Functions.retrieveData(getEmail(),getPassword(),"deviceId",this).length() > 5){

            deviceId_editText.setText(Functions.retrieveData(getEmail(),getPassword(),"deviceId",this));
            deviceId_editText.setEnabled(false);
            btn_addDeviceId.setEnabled(false);
        } else { // Device Id not found
            deviceId_editText.setHint("Enter Device Id");
            deviceId_editText.setText("");
            btn_addDeviceId.setEnabled(true);
            btn_startUnlockService.setEnabled(false);
            btn_startRemoteUnlock.setEnabled(false);
        }
    }

    public void addDeviceId(View view){
        if(Functions.addDevice(getEmail(),deviceId_editText.getText().toString(), this).length() > 5){
            Toast.makeText(this,"Device Id has been successfully added please relog",Toast.LENGTH_LONG).show();
            logout(view);
        } else {
            Toast.makeText(this,"Wrong Device ID, Check and try again",Toast.LENGTH_LONG).show();
        }
    }
    public void deleteData(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("Email");
        editor.remove("Password");
        editor.apply();
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }

    public void logout(View view){
        deleteData();
        // TODO  stop service
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }

    private String getEmail(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
        String email = settings.getString("Email","NONE");
        return email;
    }

    private String getPassword(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("SmartLockData",MODE_PRIVATE);
        String password = settings.getString("Password","NONE");
        return password;
    }

    public void remoteUnlock(View view) {
        Functions.remoteUnlock(getEmail(),deviceId_editText.getText().toString(),this);
    }

    public void startAutoUnlock(View view){
        Button startServiceButton = (Button) findViewById(R.id.btn_startAutoService);
        if(startServiceButton.getText().equals("Start Auto Unlock ! ")){
            // loop on Functions.checkLockStatus();
            wannaContinue = true;
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    while(wannaContinue){
                        if(!wannaContinue)break;
                        Functions.DoUnlockStuff(getEmail(),getPassword(),deviceId_editText.getText().toString());
                        try {
                            if(!wannaContinue)break;
                            Thread.sleep(1000);
                            if(!wannaContinue)break;
                        } catch (InterruptedException e) {

                        }
                    }
                }
            });
            thread.start();
            startServiceButton.setText("Stop Auto Unlock ! ");
        }
        else if(startServiceButton.getText().equals("Stop Auto Unlock ! ")){
            // stop the loop on Functions.checkLockStatus();
            startServiceButton.setText("Start Auto Unlock ! ");
            wannaContinue = false;
        }
    }
}
