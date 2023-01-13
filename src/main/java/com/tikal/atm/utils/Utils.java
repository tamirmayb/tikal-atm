package com.tikal.atm.utils;

public class Utils {
    public static float roundFloatStr(String num) {
        return (float) (Math.round(Float.parseFloat(num) * 100.0) / 100.0);
    }

    public static float roundFloat(float num) {
        return (float) (Math.round(num * 100.0) / 100.0);
    }
}
