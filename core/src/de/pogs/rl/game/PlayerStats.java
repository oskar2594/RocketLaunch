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
package de.pogs.rl.game;

import de.pogs.rl.utils.ConfigLoader;

/**
 * Spieler Level
 */
public class PlayerStats {

    private static int expPerLevel = 100;
    private static int expMorePerLevel = 20;

    private static int exp = 0;
    private static int level = 0;
    private static int accessExp = 0;
    private static int needExp = expPerLevel;
    private static int highscore;

    private static boolean first = true;

    /**
     * Highscore aktualisieren
     */
    public static void update() {
        if (first) {
            first = false;
            exp = 0;
            highscore = ConfigLoader.getValueInt("highscore");
        }
        updateLevelData();
        ConfigLoader.setValue(highscore, "highscore");
    }

    /**
     * Playerscore speichern, wenn er aktuellen Highscore übertrifft
     */
    public static void saveHighscore() {
        if (exp > highscore) {
            highscore = exp;
            ConfigLoader.setValue(highscore, "highscore");
        }
    }

    /**
     * Aktuellen Playerscore zurücksetzen
     */
    public static void reset() {
        exp = 0;
        PlayerStats.updateLevelData();
    }

    /**
     * Fortschritt im aktuellen Level in Prozent
     * @return Fortschritt
     */
    public static float getCurrentLevelPercentage() {
        if (accessExp == 0 || needExp == 0)
            return 0;
        return (float) accessExp / (float) needExp;
    }

    /**
     * Exp hinzufügen
     * @param e Anzahl Exp
     */
    public static void addExp(int e) {
        if (e < 0)
            return;
        exp += e;
        saveHighscore();
    }

    /**
     * Exp abziehen
     * @param e Anzahl Exp
     */
    public static void remExp(int e) {
        if (e < 0)
            return;
        if (e > exp) {
            exp = 0;
            return;
        }
        exp -= e;
    }

    /**
     * Daten aktualisieren:
     * Level, Exp Überfluss, Benöitgte Exp
     */
    static void updateLevelData() {
        int l = 0;
        int aExp = 0;
        int nExp = expPerLevel;
        int globalNeed = expPerLevel;
        for (int i = 0; i < exp; i++) {
            if (i >= globalNeed) {
                aExp = 0;
                l++;
                globalNeed += expPerLevel + l * expMorePerLevel;
                nExp = expPerLevel + l * expMorePerLevel;
            }
            aExp++;
        }
        level = l;
        accessExp = aExp;
        needExp = nExp;
    }

    /**
     * Level aus aktuellen Exp Stand generieren
     * @param exp Exp Stand
     * @return Level
     */
    public static int getLevelFromExp(int exp) {
        int l = 0;
        int globalNeed = expPerLevel;
        for (int i = 0; i < exp; i++) {
            if (i >= globalNeed) {
                l++;
                globalNeed += expPerLevel + l * expMorePerLevel;
            }
        }
        return l;
    }

    /**
     * Aktuelles Level abfragen
     * @return Level
     */
    public static int getLevel() {
        return level;
    }

    /**
     * Aktuelle Exp abfragen
     * @return Exp
     */
    public static int getExp() {
        return exp;
    }

    /**
     * Exp Überschuss abfragen
     * @return Exp
     */
    public static int getAccessExp() {
        return accessExp;
    }

    /**
     * Benötigte Exp zum nächsten Level
     * @return Exp
     */
    public static int getNeedExp() {
        return needExp;
    }

    /**
     * Aktuellen Highscore
     * @return Highscore
     */
    public static int getHighscore() {
        return highscore;
    }

}
