package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.RenderTask;
import net.jmecn.scene.Result;

public class Reflection extends RenderTask {

    class Vec2 {
        float x, y;
    }
    
    Result scene(float x, float y) {
        return scene2d.scene(x, y);
    }

    void gradient(float x, float y, Vec2 n) {
        n.x = (scene(x + EPSILON, y).sd - scene(x - EPSILON, y).sd) * (0.5f / EPSILON);
        n.y = (scene(x, y + EPSILON).sd - scene(x, y - EPSILON).sd) * (0.5f / EPSILON);
    }

    void reflect(float ix, float iy, float nx, float ny, Vec2 r) {
        float idotn2 = (ix * nx + iy * ny) * 2.0f;
        r.x = ix - idotn2 * nx;
        r.y = iy - idotn2 * ny;
    }
    
    float trace(float ox, float oy, float dx, float dy, int depth) {
        float t = 0.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float x = ox + dx * t, y = oy + dy * t;
            Result r = scene(x, y);
            if (r.sd < EPSILON) {
                float sum = r.emissive;
                if (depth < MAX_DEPTH && r.reflectivity > 0.0f) {
                    float nx, ny, rx, ry;
                    
                    Vec2 normal = new Vec2();
                    gradient(x, y, normal);
                    nx = normal.x;
                    ny = normal.y;
                    
                    Vec2 reflect = new Vec2();
                    reflect(dx, dy, nx, ny, reflect);
                    rx = reflect.x;
                    ry = reflect.y;
                    
                    sum += r.reflectivity * trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1);
                }
                return sum;
            }
            t += r.sd;
        }
        return 0.0f;
    }

    float sample(float x, float y) {
        float sum = 0.0f;
        for (int i = 0; i < N; i++) {
            float a = TWO_PI * (i + rand() / RAND_MAX) / N;
            sum += trace(x, y, cosf(a), sinf(a), 0);
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
