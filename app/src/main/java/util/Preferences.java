package util;


import android.content.Context;

public class Preferences {

    private static final String USER = "USER";
    private static final String PASS = "PASS";

    public static String getUser(Context context){
        try{
            return context.getSharedPreferences("USER_PREFERENCES", 0).getString(USER, null);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
        return null;
    }

    public static String getPass(Context context){
        try{
            return context.getSharedPreferences("USER_PREFERENCES", 0).getString(PASS, null);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
        return null;
    }





}
