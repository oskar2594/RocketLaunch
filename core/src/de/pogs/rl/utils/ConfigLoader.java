/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
package de.pogs.rl.utils;

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
    private static FileHandle config = Gdx.files.absolute(OsUtils.getUserConfigDirectory() + "/RocketLauncher/storage.rl");
    private static String defaultConfig = "{\"highscore\": \"0\"}}";
    public static JsonValue data;

    static {
        readStorage();
    }

    private static void createDefaultStorage() {
        try {
            config.writeString(encrypt(defaultConfig), false);
        } catch (Exception e) {
            throw e;
        }
        readStorage();
    }

    private static void readStorage() {
        try {
            data = json.parse(decrypt(config.readString()));
            if(data.get("highscore") == null && json.parse(defaultConfig).get("highscore") != null) {
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
        config.writeString(encrypt(data.prettyPrint(OutputType.json, 1)), false);
    }

    private static String decrypt(String content) {
        return Crypt.decrypt(content, HWID.getHWID());
    }

    private static String encrypt(String content) {
        return Crypt.encrypt(content, HWID.getHWID());
    }
}
