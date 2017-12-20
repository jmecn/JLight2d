package net.jmecn.math;

import java.util.Random;

/**
 * Float math
 * @author yanmaoyuan
 *
 */
public final class FMath {
    
    public final static float TWO_PI = 6.28318530718f;
    
    public final static Random RAND = new Random();
    public final static int RAND_MAX = 0x7fff;
    public final static float INV_RAND_MAX = 1f / 0x7fff;
    
    public static float fabsf(float a) {
        return Math.abs(a);
    }

    public static float sqrtf(float a) {
        return (float) Math.sqrt(a);
    }

    public static float fmaxf(float a, float b) {
        return a > b ? a : b;
    }

    public static float fminf(float a, float b) {
        return a < b ? a : b;
    }

    public static float cosf(float a) {
        return (float) Math.cos(a);
    }

    public static float sinf(float a) {
        return (float) Math.sin(a);
    }

    public static float atan2f(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    public static float fmodf(float a, float b) {
        int c = (int) (a / b);
        return a - b * c;
    }

    public final static float expf(float a) {
        return (float) Math.exp(a);
    }
    
    public final static float rand() {
        return RAND.nextInt(RAND_MAX);
    }
}
