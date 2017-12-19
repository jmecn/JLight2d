package net.jmecn.renderer;

import static net.jmecn.math.FMath.*;

import net.jmecn.math.Color;
import net.jmecn.math.Vector2f;
import net.jmecn.scene.Result;

public class RayTracing2Reflection extends RayTracing1Csg {

    protected void gradient(float x, float y, Vector2f n) {
        n.x = (scene(x + EPSILON, y).sd - scene(x - EPSILON, y).sd) * (0.5f / EPSILON);
        n.y = (scene(x, y + EPSILON).sd - scene(x, y - EPSILON).sd) * (0.5f / EPSILON);
    }

    protected void reflect(float ix, float iy, float nx, float ny, Vector2f r) {
        float idotn2 = (ix * nx + iy * ny) * 2.0f;
        r.x = ix - idotn2 * nx;
        r.y = iy - idotn2 * ny;
    }
    
    protected Color trace(float ox, float oy, float dx, float dy, int depth) {
        float t = 0.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float x = ox + dx * t, y = oy + dy * t;
            Result r = scene(x, y);
            if (r.sd < EPSILON) {
                // float sum = r.emissive;
                Color sum = new Color(r.emissive);
                if (depth < MAX_DEPTH && r.reflectivity > 0.0f) {
                    float nx, ny, rx, ry;
                    
                    Vector2f normal = new Vector2f();
                    gradient(x, y, normal);
                    nx = normal.x;
                    ny = normal.y;
                    
                    Vector2f reflect = new Vector2f();
                    reflect(dx, dy, nx, ny, reflect);
                    rx = reflect.x;
                    ry = reflect.y;
                    // sum += r.reflectivity * trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1);
                    sum.addLocal( trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1).multLocal(r.reflectivity ));
                }
                return sum;
            }
            t += r.sd;
        }
        // return 0.0f;
        return new Color(0f);
    }

    @Override
    protected Color sample(float x, float y) {
        Color sum = new Color(0);
        for (int i = 0; i < samples; i++) {
            float a = monteCarloMethod(i);
            // sum += trace(x, y, cosf(a), sinf(a));
            sum.addLocal( trace(x, y, cosf(a), sinf(a), 0) );
        }
        return sum.multLocal(1f / samples);
    }

}
