package net.jmecn.algo;

import static net.jmecn.FMath.*;
import static net.jmecn.scene.ShapeSDF.*;

import net.jmecn.Renderer;

public class Base extends Renderer {

    protected float trace(float ox, float oy, float dx, float dy) {
        float t = 0.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float sd = circleSDF(ox + dx * t, oy + dy * t, 0.5f, 0.5f, 0.1f);
            if (sd < EPSILON)
                return 2.0f;
            t += sd;
        }
        return 0.0f;
    }

    protected float sample(float x, float y) {
        float sum = 0.0f;
        for (int i = 0; i < samples; i++) {
            float a = monteCarloMethod(i);
            sum += trace(x, y, cosf(a), sinf(a));
        }
        return sum / samples;
    }

    @Override
    public void run() {
        int p = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++, p += 3) {
                components[p] = components[p+1] = components[p+2] = (byte)(fminf(sample((float)x / width, (float)y / height) * 255.0f, 255.0f));
            }
        }
    }
}
