package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.RenderTask;
import net.jmecn.scene.Result;

public class Csg extends RenderTask {

    Result scene(float x, float y) {
        return scene2d.scene(x, y);
    }

    float trace(float ox, float oy, float dx, float dy) {
        float t = 0.001f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            Result r = scene(ox + dx * t, oy + dy * t);
            if (r.sd < EPSILON)
                return r.emissive;
            t += r.sd;
        }
        return 0.0f;
    }

    float sample(float x, float y) {
        float sum = 0.0f;
        for (int i = 0; i < N; i++) {
            float a = TWO_PI * (i + rand() / RAND_MAX) / N;
            sum += trace(x, y, cosf(a), sinf(a));
        }
        return sum / N;
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
