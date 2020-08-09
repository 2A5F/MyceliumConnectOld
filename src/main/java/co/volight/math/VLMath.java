package co.volight.math;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;

public class VLMath {
    public static final Vector3f North = new Vector3f(0, 0, -1);
    public static final Vector3f South = new Vector3f(0, 0, 1);
    public static final Vector3f West = new Vector3f(-1, 0, 0);
    public static final Vector3f East = new Vector3f(1, 0, 0);
    public static final Vector3f Up = new Vector3f(0, 1, 0);
    public static final Vector3f Down = new Vector3f(0, -1, 0);

    public static float RGBi2f(int v) {
        return v / 255f;
    }
    public static Vector3f RGBi2f(int r, int g, int b) {
        return new Vector3f(r / 255f, g / 255f, b / 255f);
    }
    public static Vector4f RGBi2f(int r, int g, int b, int a) {
        return new Vector4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static float ReMap(float value, float low1, float high1, float low2, float high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }
    public static double ReMap(double value, double low1, double high1, double low2, double high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }
    public static int ReMap(int value, int low1, int high1, int low2, int high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }
    public static long ReMap(long value, long low1, long high1, long low2, long high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }

    public static float Limit(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }
    public static double Limit(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
    public static int Limit(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
    public static long Limit(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }
}
