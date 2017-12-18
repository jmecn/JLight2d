package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.scene.Result;

public class Reflection extends Csg {

    protected class Vec2 {
        float x, y;
    }
    
    protected void gradient(float x, float y, Vec2 n) {
        n.x = (scene(x + EPSILON, y).sd - scene(x - EPSILON, y).sd) * (0.5f / EPSILON);
        n.y = (scene(x, y + EPSILON).sd - scene(x, y - EPSILON).sd) * (0.5f / EPSILON);
    }

    protected void reflect(float ix, float iy, float nx, float ny, Vec2 r) {
        float idotn2 = (ix * nx + iy * ny) * 2.0f;
        r.x = ix - idotn2 * nx;
        r.y = iy - idotn2 * ny;
    }
    
    protected float trace(float ox, float oy, float dx, float dy, int depth) {
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

    @Override
    protected float sample(float x, float y) {
        float sum = 0.0f;
        for (int i = 0; i < samples; i++) {
            float a = monteCarloMethod(i);
            // sum += trace(x, y, cosf(a), sinf(a));
            sum += trace(x, y, cosf(a), sinf(a), 0);
        }
        return sum / samples;
    }

}
