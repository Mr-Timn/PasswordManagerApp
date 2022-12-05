package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StoredData {
    static { System.loadLibrary("main"); }

    private native String createSecureHash(String Password, String Salt);
    private native String encryptData(String Data, String Key, String Encryption);
    private native String decryptData(String Data, String Key, String Encryption);

    public static final String ALLCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`1234567890 -=[];',./~!@#$%^&*(){}|:<>?";
    public static final String ID_PASS = "/pass/";
    public static final String ID_NOTE = "/note/";
    public static final String DATA_DELIM = "\\//\\\\//\\";

    static final String PREF_ID = "temppref12";
    static final String TAG_USER = "tempuser";
    static final String TAG_DATA = "tempdata_";

    public SharedPreferences sharedprefs;
    public Set<String> userdata;
    public String username = "", password = "";

    private ArrayList<String> user_array, pass_array, salt_array;
    private Context appcontext;
    private Intent appintent;

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
        String dtag = newdatatag(id); Log.e(MainActivity.LOG_TAG, dtag);
        String edata = encryptData(data, password,"AES256"); Log.e(MainActivity.LOG_TAG, "ENCR = " + edata);
        sharedprefs.edit().putString(dtag, edata).apply();
    }
    public void editData(String data, String id, int i) {
        String dtag = getdatatag(id, i); Log.e(MainActivity.LOG_TAG, dtag);
        String edata = encryptData(data, password,"AES256"); Log.e(MainActivity.LOG_TAG, "ENCR = " + edata);
        sharedprefs.edit().putString(dtag, edata).apply();
    }
    public void removeData(String id, int i) {
        String dtag = getdatatag(id, i);
        sharedprefs.edit().remove(dtag).apply();
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

    public static String randomPassword(int len) {
        String p = "";

        Random rand = new Random();
        for (int i = 0; i < len; i++) {
            p += ALLCHARS.charAt(rand.nextInt(ALLCHARS.length()));
        }

        return p;
    }

    public static Boolean hasNumeric(String p) {
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) > '0' && p.charAt(i) < '9') return true;
        }
        return false;
    }
    public static Boolean hasLower(String p) {
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) > 'a' && p.charAt(i) < 'z') return true;
        }
        return false;
    }
    public static Boolean hasUpper(String p) {
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) > 'A' && p.charAt(i) < 'Z') return true;
        }
        return false;
    }
    public static Boolean hasSpecial(String p) {
        String spchar = "`-=[]\\;',./~!@#$%^&*()_+{}|:\"><?";
        for (int i = 0; i < p.length(); i++) {
            if (spchar.indexOf(p.charAt(i)) != -1) return true;
        }
        return false;
    }
    public static int passwordStrength(String p) {
        int plen = p.length();
        Boolean hassp = hasSpecial(p);
        Boolean hasup = hasUpper(p);
        Boolean haslo = hasLower(p);
        Boolean hasnu = hasNumeric(p);

        if (hassp && hasup && haslo && hasnu) {
            if (plen <= 6) return 0;
            else if (plen <= 10) return 1;
            else if (plen <= 12) return 2;
            else if (plen <= 15) return 3;
            else return 4;
        } else if (hasup && haslo && hasnu) {
            if (plen <= 6) return 0;
            else if (plen <= 10) return 1;
            else if (plen <= 13) return 2;
            else if (plen <= 16) return 3;
            else return 4;
        } else if (hasup && haslo) {
            if (plen <= 6) return 0;
            else if (plen <= 11) return 1;
            else if (plen <= 14) return 2;
            else if (plen <= 17) return 3;
            return 4;
        } else if (!hasnu) {
            if (plen <= 8) return 0;
            else if (plen <= 13) return 1;
            else if (plen <= 17) return 2;
            else return 3;
        } else {
            if (plen <= 11) return 0;
            else if (plen <= 18) return 1;
            else return 2;
        }
    }
    public static void checkPasswordStrength(EditText p, TextView t) {
        int vstr = StoredData.passwordStrength(p.getText().toString());
        switch (vstr) {
            case 0: t.setText("Very Weak");  break;
            case 1: t.setText("Weak"); break;
            case 2: t.setText("Good"); break;
            case 3: t.setText("Great"); break;
            case 4: t.setText("Excellent"); break;
        }
    }
    public static void trackPasswordStrength(EditText p, TextView t) {
        p.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkPasswordStrength(p, t);
                return false;
            }
        });
    }
}
