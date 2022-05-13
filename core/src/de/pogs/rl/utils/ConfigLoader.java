package de.pogs.rl.utils;

import java.io.File;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.JsonWriter.OutputType;


public class ConfigLoader {

    private static boolean loaded = false;
    private static double saveInterval = 10 * 1000;
    private static double lastSave = TimeUtils.millis();

    private static JsonReader json = new JsonReader();
    private static FileHandle config =
            Gdx.files.absolute(OsUtils.getUserConfigDirectory() + "/RocketLauncher/storage.rl");
    private static FileHandle defaultConfig = Gdx.files.internal("config.json");
    public static JsonValue data;

    static {
        System.out.println(OsUtils.getUserConfigDirectory());
        readStorage();
    }

    private static void createDefaultStorage() {
        config.writeString(defaultConfig.readString(), false);
        readStorage();
    }

    private static void readStorage() {
        try {
            data = json.parse(config);
            if(data.get("preferences") == null && json.parse(defaultConfig).get("preferences") != null) {
                throw new Exception();
            }
        } catch (Exception e) {
            createDefaultStorage();
            return;
        }
        loaded = true;
    }

    public static void update() {
        if (!loaded)
            return;
        if ((TimeUtils.millis() - lastSave) >= saveInterval) {
            save();
            lastSave = TimeUtils.millis();
        }
    }


    public static void setValue(String newValue, String... navs) {
        if (!loaded)
            return;
        JsonValue value = null;
        try {
            for (int i = 0; i < navs.length; i++) {
                String nav = navs[i];
                if (i == 0) {
                    data.get(nav).set(newValue);
                    break;
                }
                if (i == 0) {
                    value = data.get(nav);
                } else {
                    value = value.get(nav);
                }
            }
        } catch (Exception e) {
            System.out.println("dd");
            return;
        }
    }

    public static void setValue(Integer newValue, String... navs) {
        if (!loaded)
            return;
        JsonValue value = null;
        try {
            for (int i = 0; i < navs.length; i++) {
                String nav = navs[i];
                System.out.println(i);
                if (navs.length == 1) {
                    if (data.get(nav) == null) {
                        data.addChild(nav, new JsonValue(""));
                    }
                    data.get(nav).set(Integer.toString(newValue));
                    break;
                }
                if (i == 0) {
                    if (data.get(nav) == null) {
                        data.addChild(nav, new JsonValue(""));
                    }
                    value = data.get(nav);
                } else {
                    if (value.get(nav) == null) {
                        value.addChild(nav, new JsonValue(""));
                    }
                    value = value.get(nav);
                }
            }
        } catch (Exception e) {
        }
        System.out.println(data.prettyPrint(OutputType.json, 1));
    }


    public static String getValue(String... navs) {
        if (!loaded)
            return null;
        JsonValue value = null;

        try {
            for (int i = 0; i < navs.length; i++) {
                String nav = navs[i];
                if (i == 0) {
                    return data.getString(nav);
                }
                if (i == 0) {
                    value = data.get(nav);
                } else {
                    value = value.get(nav);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    public static Integer getValueInt(String... navs) {
        if (!loaded)
            return null;
        JsonValue value = null;
        try {
            for (int i = 0; i < navs.length; i++) {
                String nav = navs[i];
                if (i == 0) {
                    return data.getInt(nav);
                }
                if (i == 0) {
                    value = data.get(nav);
                } else {
                    value = value.get(nav);
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public static void save() {
        // file.writeString("test", false);
        config.writeString(data.prettyPrint(OutputType.json, 1), false);
    }
}
