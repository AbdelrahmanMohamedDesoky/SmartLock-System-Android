package aou.smartlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Functions {

    public static String remoteUnlockResponse = "";

    public static String response = "";

    public static String deviceId = "";

    public static String name = "";

    public static String addDeviceResponse = "";

    public static String serviceResposne = "";

    static public boolean login(String email, String password,Context caller){
        try {
            new GetMethodDemo().execute("http://192.168.1.2/smartlock/API/checkLoginDetails.php?email="+email+"&password="+password);
            while(response.length() < 1){

            }
            if(response.equals("LOGIN_OK")){
                Toast.makeText(caller,"Successfully Logged In",Toast.LENGTH_LONG).show();
                return true;
            }
            else if(response.equals("LOGIN_ERROR")){
                Toast.makeText(caller,"Invalid Email/Password",Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                Toast.makeText(caller,"Invalid Response from Server --> " + response,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception ex){
            Toast.makeText(caller,"Error while trying to Login, Error --> " + ex.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    static public String retrieveData(String email,String password, String type,Context caller){
        try {
            new GetMethodDemo().execute("http://192.168.1.2/smartlock/API/getAccountInfo.php?email="+email+"&password="+password+"&"+type);
            if (type.equals("name")){
                while(name.length() < 1){

                }
            }
            else if(type.equals("deviceId")){
                while(deviceId.length() < 1){

                }
            }
            switch(type){
                case "name":
                    String realName = name.substring(name.indexOf("NAME:")+5,name.length());
                    if(realName.length() > 1){
                        return realName;
                    } else {
                        return "Loading ...";
                    }
                case "deviceId":
                    String realDeviceId = deviceId.substring(deviceId.indexOf("deviceId:")+9,deviceId.length());
                    if(realDeviceId.length() > 1){
                        return realDeviceId;
                    } else {
                        return "NONE";
                    }
            }
        }
        catch (Exception e){
            Toast.makeText(caller,"Error while trying to Login, Error --> " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return "NO DATA";
    }
    static public String addDevice(String email,String deviceId,Context caller){
        try {
            new GetMethodDemo().execute("http://192.168.1.2/smartlock/API/AddDeviceId.php?email="+email+"&deviceId="+deviceId+"&addDeviceId");
            while(addDeviceResponse.length() < 1){

            }
            String realAddDeviceIdResponse = addDeviceResponse.substring(addDeviceResponse.indexOf("addDeviceId:")+12,addDeviceResponse.length());
            if(realAddDeviceIdResponse.length() > 1){
                return realAddDeviceIdResponse;
            } else {
                return "NONE";
            }
        } catch (Exception e){
            Toast.makeText(caller,"Error while trying to Login, Error --> " + e.getMessage(),Toast.LENGTH_LONG).show();
            return "NONE";
        }
    }
    static public boolean remoteUnlock(String email, String deviceId,Context caller){
        try {
            new GetMethodDemo().execute("http://192.168.1.2/smartlock/API/SetRemoteLockStatus.php?deviceId="+deviceId+"&status=OK_UNLOCK");
            while(remoteUnlockResponse.length() < 2){

            }
            if(remoteUnlockResponse.equals("OK")){
                Toast.makeText(caller,"Lock will be unlocked per your request",Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(caller,"Error Remote Unlock Request Failed Error : " + response,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Log.v("RemoteUnlockError",e.getLocalizedMessage() + " Response = " + response);
            return false;
        }
    }

    public static String getEmail(Context caller){
        SharedPreferences settings = caller.getSharedPreferences("SmartLockData",Context.MODE_PRIVATE);
        String email = settings.getString("Email","NONE");
        return email;
    }

    public static String getPassword(Context caller){
        SharedPreferences settings = caller.getSharedPreferences("SmartLockData",Context.MODE_PRIVATE);
        String password = settings.getString("Password","NONE");
        return password;
    }

    public static void DoUnlockStuff(String email,String password, String deviceId){
        new AutoService().execute("http://192.168.1.2/smartlock/API/checkLockStatus.php?deviceId="+deviceId);
        while(serviceResposne.length() < 2){

        }
        if(serviceResposne.equals("PENDING_UNLOCK")){
            new AutoService().execute("http://192.168.1.2/smartlock/API/ProvideDataToUnlock.php?mode=0&email="+email+"&password="+password+"&deviceId="+deviceId);
        }

    }


}
