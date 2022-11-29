package com.example.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StoredData {
    static { System.loadLibrary("main"); }

    private native String createSecureHash(String Password, String Salt);
    private native String encryptData(String Data, String Key, String Encryption);
    private native String decryptData(String Data, String Key, String Encryption);

    static final String PREF_ID = "pref";
    static final String TAG_USER = "user";

    public SharedPreferences sharedprefs;
    public Set<String> userdata;

    private ArrayList<String> user_array, pass_array, salt_array;
    private Context appcontext;

    public StoredData(Context context) {
        appcontext = context;
        sharedprefs = appcontext.getSharedPreferences(PREF_ID, 0);
        userdata = sharedprefs.getStringSet(TAG_USER, null);

        user_array = new ArrayList<String>();
        pass_array = new ArrayList<String>();
        salt_array = new ArrayList<String>();

        if (userdata == null) {
            userdata = new HashSet<String>();
        } else {
            for (String s : userdata) {
                int sp = s.indexOf(" ");
                String tuser = s.substring(0, sp);
                String tpass = s.substring(sp + 1);
                String tsalt = tpass.substring(tpass.indexOf("__") + 2);
                Log.d(MainActivity.LOG_TAG, "Loaded user: " + tuser + " " + tpass + " " + tsalt);
                user_array.add(tuser);
                pass_array.add(tpass);
                salt_array.add(tsalt);
            }
        }
    }

    public Boolean addUser(String username, String password) {
        if (user_array.contains(username)) {
            Toast.makeText(appcontext, "Username already exists", (int)3).show();
            return false;
        }

        String passhash = createSecureHash(password, "");
        String newdata = username + " " + passhash;
        userdata.add(newdata);
        sharedprefs.edit().putStringSet(TAG_USER, userdata).apply();

        return true;
    }

    public int getUserIndex(String username) {
        for (int i = 0; i < user_array.size(); i++) {
            if (username.equals(user_array.get(i))) return i;
        }
        return -1;
    }

    public Boolean verifyUser(String username, String password) {
        int uid = getUserIndex(username);
        Log.d(MainActivity.LOG_TAG, "uid = " + uid);
        if (uid != -1) {
            String guesshash = createSecureHash(password, salt_array.get(uid));
            if (guesshash.equals(pass_array.get(uid))) {
                Log.d(MainActivity.LOG_TAG, "CORRECT PASSWORD");
                return true;
            } else {
                Log.e(MainActivity.LOG_TAG, "INCORRECT PASSWORD");
                Log.e(MainActivity.LOG_TAG, pass_array.get(uid));
                Log.e(MainActivity.LOG_TAG, guesshash);
            }
        }
        return false;
    }

    public Boolean hasUsers() {
        return !(userdata == null || userdata.size() == 0);
    }

    public void saveData(String username, String password, String data, String tag) {
        int uid = getUserIndex(username);
        if (uid != -1) {
            String edata = encryptData( data, password,"AES256"); Log.d(MainActivity.LOG_TAG, edata);
            //String ddata = decryptData(edata, password,"AES256"); Log.d(MainActivity.LOG_TAG, ddata);
        }
    }

    public void getData(String username, String password) {
        int uid = getUserIndex(username);
        if (uid != -1) {

        }
    }
}
