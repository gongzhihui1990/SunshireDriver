package net.iaf.framework.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 处理SharePreferences的工具方法
 *
 * @author Bob
 */
public class SharePrefsHelper {
    private Context context;
    private String prefName;
    private int mode;

    /**
     * 构造方法
     *
     * @param prefName
     */
    public SharePrefsHelper(Context context, String prefName) {
        this.context = context;
        this.prefName = prefName;
        this.mode = Context.MODE_PRIVATE;
    }

    /**
     * Constructor
     *
     * @param context
     * @param prefName sharedPreference文件的名称
     * @param mode     参考Context的mode(MODE_PRIVATE MODE_WORLD_READABLE
     *                 MODE_WORLD_WRITEABLE )
     */
    public SharePrefsHelper(Context context, String prefName, int mode) {
        this.context = context;
        this.prefName = prefName;
        this.mode = mode;
    }

    public void clearAllValues() {
        synchronized (this) {
            getEditor().clear().commit();
        }
    }

    public String getString(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public boolean setString(String key, String value) {
        return getEditor().putString(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public boolean setInt(String key, int value) {
        return getEditor().putInt(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public boolean setLong(String key, long value) {
        return getEditor().putLong(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public boolean setFloat(String key, float value) {
        return getEditor().putFloat(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public boolean setBoolean(String key, boolean value) {
        return getEditor().putBoolean(key, value).commit();
    }

    // =========================================================================

    /**
     * 获取SharedPreferences对象
     *
     * @return
     */
    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(prefName, mode);
    }

    /**
     * 获取SharedPreferences.Editor对象
     *
     * @return
     */
    public SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }
}
