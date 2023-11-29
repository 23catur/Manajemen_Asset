package com.example.asset2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;
    Context context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("Asset", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

        public void logout(){
        editor.clear();
        editor.apply();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}


//public class SessionManager {
//
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;
//    Context context;
//    int mode = 0;
//
//    private static final String pref_name = "crudpref";
//    private static final String is_login = "islogin";
//    public static final String kunci_nama = "keynama";
//    public static final String kunci_nik = "keynik";
//
//
//    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
//    private final SharedPreferences pref;
//    private final SharedPreferences.Editor editor;
//    private final Context _context;
//
//
//
//    public void setLogin(boolean isLoggedIn) {
//        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
//        editor.apply();
//    }
//
//    public boolean isLoggedIn() {
//        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
//    }
//
//    public SessionManager(Context context){
//        this.context = context;
//        pref = context.getSharedPreferences(pref_name, MODE_PRIVATE);
//        editor = pref.edit();
//    }
//
//    public void checkLogin2(){
//        if (!this.is_login()){
//            Intent i = new Intent(context, Login.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }else {
//            Intent i = new Intent(context, NavigasiActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
//    }
//
//    private boolean is_login() {
//        return pref.getBoolean(is_login,false);
//    }
//
//    public void logout(){
//        editor.clear();
//        editor.apply();
//        Intent i = new Intent(context, Login.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//    }
//
//    public boolean getSPSudahLogin(){
//        return pref.getBoolean(is_login, false);
//    }
//
//}
