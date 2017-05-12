package com.sunshireshuttle.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sunshireshuttle.driver.model.DirverToken;

import net.iaf.framework.util.Loger;

@SuppressLint("CommitPrefEdits")
public class MySPEdit {
    private static SharedPreferences sPreferences;
    private static MySPEdit _instancePublic = null;
    private SharedPreferences.Editor editor;

    @SuppressLint("WorldWriteableFiles")
    private MySPEdit(Context context) {
        sPreferences = context.getSharedPreferences("MySharedPreferencesEdit", Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        editor = sPreferences.edit();
    }

    /**
     * @param context
     * @return
     */
    public static MySPEdit getInstance(Context context) {
        if (_instancePublic == null) {
            _instancePublic = new MySPEdit(context);
        }
        return _instancePublic;
    }

    public String getName() {
        return sPreferences.getString("LOG_NAME", "");
    }

    public void setName(String name) {
        editor.putString("LOG_NAME", name).commit();
    }

    public String getPassword() {
        return sPreferences.getString("LOG_PASSWORD", "");
    }

    public void setPassword(String password) {
        editor.putString("LOG_PASSWORD", password).commit();
    }

    public DirverToken getDirverToken() {
        String tokenStr = sPreferences.getString("DirverToken", "");
        Loger.d("tokenStr:" + tokenStr);
        if (!TextUtils.isEmpty(tokenStr)) {
            try {
                return new Gson().fromJson(tokenStr, DirverToken.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setDirverToken(DirverToken token) {
        if (token != null) {
            editor.putString("DirverToken", new Gson().toJson(token)).commit();
            Loger.d("tokenStr:" + token.toString());
        } else {
            editor.putString("DirverToken", "").commit();
        }
    }

    public void logout() {
        setPassword("");
        setDirverToken(null);
        SunDriverApplication.getInstance().dirverToken = null;
    }
}
