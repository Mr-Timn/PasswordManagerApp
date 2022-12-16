package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewPasswords extends AppCompatActivity {
    StoredData data;
    LinearLayout ll;
    ClipboardManager clipboard;
    TextView search;
    ArrayList<String> passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passwords);

        ll = findViewById(R.id.noteslayout);
        data = new StoredData(getApplicationContext(), getIntent());
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        search = findViewById(R.id.searchpassword);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                loadPasswords();
                return false;
            }
        });

        TextView sp = new TextView(getApplicationContext());
        sp.setText("----------------------------------------");
        sp.setTextSize(15);
        sp.setGravity(Gravity.CENTER);
        ll.addView(sp);

        passwords = data.getAllPasswords();
        loadPasswords();
    }

    private void loadPasswords() {
        ll.removeAllViews();
        for (int i = 0; i < passwords.size(); i++) {
            String pass = passwords.get(i);
            String tu = pass.substring(0, pass.indexOf(StoredData.DATA_DELIM));
            String tp = pass.substring(pass.indexOf(StoredData.DATA_DELIM) + StoredData.DATA_DELIM.length());

            if (search.getText().toString().length() != 0 && !tu.contains(search.getText().toString())) continue;

            Log.e("APP_DEBUG", "[LOADED] " + tu + " " + tp);

            TextView tv = new TextView(getApplicationContext());
            tv.setText(tu);
            tv.setTextSize(30);
            tv.setGravity(Gravity.CENTER);
            ll.addView(tv);

            LinearLayout bll = new LinearLayout(getApplicationContext());
            bll.setOrientation(LinearLayout.HORIZONTAL);
            bll.setGravity(Gravity.CENTER);

            Button b = new Button(getApplicationContext());
            b.setText("Edit");
            int finalI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editpass(finalI);
                }
            });
            bll.addView(b);

            Button copyuser = new Button(getApplicationContext());
            copyuser.setText("Copy Username");
            copyuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clipdata = ClipData.newPlainText("username", tu);
                    clipboard.setPrimaryClip(clipdata);
                }
            });
            bll.addView(copyuser);
            Button copypass = new Button(getApplicationContext());
            copypass.setText("Copy Password");
            copypass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clipdata = ClipData.newPlainText("password", tp);
                    clipboard.setPrimaryClip(clipdata);
                }
            });
            bll.addView(copypass);

            ll.addView(bll);

            TextView sp2 = new TextView(getApplicationContext());
            sp2.setText("----------------------------------------");
            sp2.setTextSize(15);
            sp2.setGravity(Gravity.CENTER);
            ll.addView(sp2);
        }
    }

    private void editpass(int i) {
        Intent vp = new Intent(getApplicationContext(), EditPassword.class);
        vp.putExtra(MainActivity.RESULT_TAG_USER, data.username);
        vp.putExtra(MainActivity.RESULT_TAG_PASS, data.password);
        vp.putExtra(MainActivity.RESULT_TAG_INDPASS, i);
        startActivity(vp);
    }
}