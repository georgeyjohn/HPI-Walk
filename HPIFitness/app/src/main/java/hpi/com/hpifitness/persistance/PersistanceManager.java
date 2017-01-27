package hpi.com.hpifitness.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import hpi.com.hpifitness.utils.JSONHelper;

/**
 * Created by Georgey on 26-01-2017.
 */

public class PersistanceManager {
    public static final String PREFS_NAME = "hpi_fitness";

    private SharedPreferences sPref;
    private SharedPreferences.Editor editor;

    private Context context;

    public PersistanceManager(Context cxt) {
        context = cxt;
        sPref = context.getSharedPreferences(PREFS_NAME, 0);
        editor = sPref.edit();
    }

    public Object getValue(String key, Class<?> type) {
        return JSONHelper.Deserialize(sPref.getString(key, null), type);
    }

    public String getValue(String key) {
        return sPref.getString(key, null);
    }

    public void setValue(String key, String name) {
        editor.putString(key, name);
        editor.commit();
    }

    public int getInteger(String key) {
        return sPref.getInt(key, 0);
    }

    public void setInteger(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sPref.getBoolean(key, false);
    }

    public void setBoolean(String key, Boolean name) {
        editor.putBoolean(key, name);
        editor.commit();
    }
}
