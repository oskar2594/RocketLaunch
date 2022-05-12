package de.pogs.rl.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ConfigLoader {

    private static JsonReader json  = new JsonReader();;
    private static FileHandle file = Gdx.files.internal("config.json");
    public static JsonValue data = json.parse(file);

    public ConfigLoader() {
        System.out.println(data);
    }

    public void setValue(String newValue, String ...navs) {
        JsonValue value = null;
        for (int i = 0; i < navs.length; i++) {
            String nav = navs[i];
            if(i == 0) {
                data.set(newValue);
                break;
            }
            if(i == 0) {
                value = data.get(nav);
            } else {
                value = value.get(nav);
            }
        }
    }

    public void setValue(Integer newValue, String ...navs) {
        JsonValue value = null;
        for (int i = 0; i < navs.length; i++) {
            String nav = navs[i];
            if(i == 0) {
                data.set(Integer.toString(newValue));
                break;
            }
            if(i == 0) {
                value = data.get(nav);
            } else {
                value = value.get(nav);
            }
        }
    }


    public String getValue(String ...navs) {
        JsonValue value = null;
        
        for (int i = 0; i < navs.length; i++) {
            String nav = navs[i];
            if(i == 0) {
                return data.getString(nav);
            }
            if(i == 0) {
                value = data.get(nav);
            } else {
                value = value.get(nav);
            }
        }
        return null;
    }
    
    
    public Integer getValueInt(String ...navs) {
        JsonValue value = null;
        
        for (int i = 0; i < navs.length; i++) {
            String nav = navs[i];
            if(i == 0) {
                return data.getInt(nav);
            }
            if(i == 0) {
                value = data.get(nav);
            } else {
                value = value.get(nav);
            }
        }
        return -1;
    }
    
    public void save() {
        file.writeString(json.toString(), false);
    }
}
