package de.pogs.rl.utils;


public class SpecialMath {
    public static float modulus(float x, float n) {
        float r = x % n;
        if (r < 0) {
            r += n;
        }
        return r;
    }
    public static float angleDifferenceSmaller(float angle1, float angle2, float angleBase) {
        angle1 = angle1 + angleBase / 2f;
        angle2 = angle2 + angleBase / 2f;
        float diff1 = angle1 - angle2;
        float diff2 = 0;
        if (diff1 < 0) {
            diff2 = angleBase + diff1;
        } else {
            diff2 = diff1 - angleBase;
        }
        diff1 %= angleBase;
        diff2 %= angleBase;
        if (Math.abs(diff1) > Math.abs(diff2)) {
            return diff2;
        }
        return diff1;
    }
    public static Vector2 angleToVector(float angle) {
        return new Vector2(((float) Math.cos((angle + 90) * Math.PI / 180)),
        ((float) Math.sin((angle + 90) * (Math.PI / 180))));
    }


    public static class Vector2 {
        public float x;
        public float y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 mul(float scalar) {
            return new Vector2(x * scalar, y * scalar);
        }

        public Vector2 add(Vector2 other) {
            return new Vector2(x + other.x, y + other.y);
        }

        public Vector2 sub(Vector2 other) {
            return new Vector2(x - other.x, y - other.y);
        }

        public float dst2(Vector2 other) {
            return (float) (Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }

        public Vector2 nor() {
            return new Vector2(x / magn(), y / magn());
        }

        public float magn() {
            return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

        public float dst(Vector2 other) {
            return this.sub(other).magn();
        }

        public float toAngle() {
            return (float) Math.acos(nor().x * 180 / Math.PI);
        }
    }
}
