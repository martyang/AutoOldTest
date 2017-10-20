package com.gionee.autoaging18month.fillappdata.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utility class of SharedPreference , easy to get and put data
 *
 * Author Viking Den <dengwj@gionee.com>
 * Version 1.0
 * Time 2016/8/8 0008 11:25
 */
public class Preference {

    public static String PREFERENCE_NAME = null;

    private Preference() {
        throw new AssertionError();
    }

    /**
     * initial shared preference name
     *
     * @param preferenceName name for the shared preference
     */
    public static void initName(String preferenceName){
        PREFERENCE_NAME = preferenceName ;
    }

    /**
     * According to PREFERENCE_NAME , return it's SharedPreference
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @return return a shared preference , if PREFERENCE_NAME is not null ,via Context.getSharedPreferences ; or
     *                  via PreferenceManager.getDefaultSharedPreferences
     */
    private static SharedPreferences getSharedPreference(Context context){
        if (PREFERENCE_NAME != null ){
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get a new Editor for these preferences, through which you can make
     * modifications to the data in the preferences and atomically commit those
     * changes back to the SharedPreferences object.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @return Returns a new instance of the {@link SharedPreferences.Editor} interface, allowing
     * you to modify the values in this SharedPreferences object.
     */
    private static SharedPreferences.Editor getEditor(Context context){
        return getSharedPreference(context).edit() ;
    }

    /**
     * Set a String value in the preferences
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = getSharedPreference(context);
        return sp.getString(key, defaultValue);
    }

    /**
     * Set a int value in the preferences
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = getSharedPreference(context);
        return sp.getInt(key, defaultValue);
    }

    /**
     * Set a long value in the preferences
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * Retrieve a long value from the preferences,default is -1.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sp = getSharedPreference(context);
        return sp.getLong(key, defaultValue);
    }

    /**
     * Set a float value in the preferences
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * Retrieve a float value from the preferences, default is -1
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences sp = getSharedPreference(context);
        return sp.getFloat(key, defaultValue);
    }

    /**
     * Set a boolean value in the preferences
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Retrieve a boolean value from the preferences, default is false
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = getSharedPreference(context);
        return sp.getBoolean(key, defaultValue);
    }
}