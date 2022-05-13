package de.pogs.rl.game;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.ConfigLoader;

public class PlayerStats {

    private static int expPerLevel = 100; 
    private static int expMorePerLevel = 20;

    private static int exp = 0;
    private static int level = 0;
    private static int accessExp = 0;
    private static int needExp = expPerLevel;

    private static boolean first = true;

    public static void update() {
        if(first) {
            first = false;
            exp = ConfigLoader.getValueInt("exp");
        }
        updateLevelData();
        ConfigLoader.setValue(exp, "exp");
    }

    public static float getCurrentLevelPercentag() {
        if(accessExp == 0 || needExp == 0) return 0;
        return (float) accessExp / (float)needExp;
    }

    public static void addExp(int e) {
        if(e < 0) return;
        exp += e;
    }

    public static void remExp(int e) {
        if(e < 0) return;
        if(e > exp) {
            exp = 0;
            return;
        }
        exp -= e;
    }

    private static void updateLevelData() {
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

    public static int getLevel() {
        return level;
    }

    public static int getExp() {
        return exp;
    }

    public static int getAccessExp() {
        return accessExp;
    }

    public static int getNeedExp() {
        return needExp;
    }


}
