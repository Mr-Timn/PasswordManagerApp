package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
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

    static final String PREF_ID = "temppref12";
    static final String TAG_USER = "tempuser";
    static final String TAG_DATA = "tempdata_";

    public static final String ID_PASS = "/pass/";
    public static final String ID_NOTE = "/note/";
    public static final String DATA_DELIM = "\\//\\\\//\\";

    public SharedPreferences sharedprefs;
    public Set<String> userdata;

    private ArrayList<String> user_array, pass_array, salt_array;
    private Context appcontext;
    private Intent appintent;
    public String username = "", password = "";

    public StoredData(Context context, Intent intent) {
        appcontext = context;
        appintent = intent;

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

        if (appintent != null) {
            username = appintent.getStringExtra(MainActivity.RESULT_TAG_USER);
            password = appintent.getStringExtra(MainActivity.RESULT_TAG_PASS);
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

    public String newdatatag(String id) {
        int i = 0;
        String ttag;
        do {
            ttag = getdatatag(id, i++);
        } while (!sharedprefs.getString(ttag, "").equals(""));
        return ttag;
    }
    public String getdatatag(String id, int i) {
        return TAG_DATA + username + id + i;
    }

    public void saveData(String data, String id) {
        String ntag = newdatatag(id); Log.e(MainActivity.LOG_TAG, ntag);
        String edata = encryptData(data, password,"AES256"); Log.e(MainActivity.LOG_TAG, "ENCR = " + edata);
        sharedprefs.edit().putString(ntag, edata).apply();
    }
    public void editData(String data, String id, int i) {
        String ntag = getdatatag(id, i); Log.e(MainActivity.LOG_TAG, ntag);
        String edata = encryptData(data, password,"AES256"); Log.e(MainActivity.LOG_TAG, "ENCR = " + edata);
        sharedprefs.edit().putString(ntag, edata).apply();
    }

    public String getData(String id, int i) {
        String d = sharedprefs.getString(getdatatag(id, i), ""); Log.e(MainActivity.LOG_TAG, d);
        if (d.equals("")) return "";
        return decryptData(d, password,"AES256");
    }
    public ArrayList<String> getAllPasswords() {
        return getAllData(ID_PASS);
    }
    public ArrayList<String> getAllData(String id) {
        ArrayList<String> data = new ArrayList<String>();

        int uid = getUserIndex(username);
        if (uid != -1) {
            int i = 0;
            while (true) {
                String d = getData(id, i++);
                if (!d.equals("")) data.add(d);
                else break;
            }
        }

        return data;
    }
}
