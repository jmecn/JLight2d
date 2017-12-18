package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.RenderTask;
import net.jmecn.scene.Result;

public class Refraction extends RenderTask {

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
    
    boolean refract(float ix, float iy, float nx, float ny, float eta, Vec2 r) {
        float idotn = ix * nx + iy * ny;
        float k = 1.0f - eta * eta * (1.0f - idotn * idotn);
        if (k < 0.0f)
            return false; // Total internal reflection
        float a = eta * idotn + sqrtf(k);
        r.x = eta * ix - a * nx;
        r.y = eta * iy - a * ny;
        return true;
    }
    
    float trace(float ox, float oy, float dx, float dy, int depth) {
        float t = 1e-3f;
        float sign = scene(ox, oy).sd > 0.0f ? 1.0f : -1.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float x = ox + dx * t, y = oy + dy * t;
            Result r = scene(x, y);
            if (r.sd * sign < EPSILON) {
                float sum = r.emissive;
                if (depth < MAX_DEPTH && (r.reflectivity > 0.0f || r.eta > 0.0f)) {
                    float nx, ny, rx, ry, refl = r.reflectivity;;
                    
                    Vec2 normal = new Vec2();
                    gradient(x, y, normal);
                    nx = normal.x * sign;
                    ny = normal.y * sign;
                    
                    if (r.eta > 0.0f) {
                        Vec2 refraction = new Vec2();
                        if (refract(dx, dy, nx, ny, sign < 0.0f ? r.eta : 1.0f / r.eta, refraction)) {
                            rx = refraction.x;
                            ry = refraction.y;
                            sum += (1.0f - refl) * trace(x - nx * BIAS, y - ny * BIAS, rx, ry, depth + 1);
                        } else {
                            refl = 1.0f; // Total internal reflection
                        }
                    }
                    if (refl > 0.0f) {
                        Vec2 reflect = new Vec2();
                        reflect(dx, dy, nx, ny, reflect);
                        rx = reflect.x;
                        ry = reflect.y;
                        sum += r.reflectivity * trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1);
                    }
                }
                return sum;
            }
            t += r.sd * sign;
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
