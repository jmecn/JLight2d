package net.jmecn.renderer;

import static net.jmecn.math.FMath.*;

import net.jmecn.math.Color;
import net.jmecn.math.Vector2f;
import net.jmecn.scene.Result;

public class RayTracing3Refraction extends RayTracing2Reflection {

    // int refract(float ix, float iy, float nx, float ny, float eta, float *rx, float *ry)
    protected boolean refract(float ix, float iy, float nx, float ny, float eta, Vector2f r) {
        float idotn = ix * nx + iy * ny;
        float k = 1.0f - eta * eta * (1.0f - idotn * idotn);
        if (k < 0.0f) {
            return false; // Total internal reflection
        }
        float a = eta * idotn + sqrtf(k);
        r.x = eta * ix - a * nx;
        r.y = eta * iy - a * ny;
        return true;
    }
    
    @Override
    protected Color trace(float ox, float oy, float dx, float dy, int depth) {
        float t = 1e-3f;
        float sign = scene(ox, oy).sd > 0.0f ? 1.0f : -1.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float x = ox + dx * t, y = oy + dy * t;
            Result r = scene(x, y);
            if (r.sd * sign < EPSILON) {
                // float sum = r.emissive;
                Color sum = new Color(r.emissive);
                if (depth < MAX_DEPTH && (r.reflectivity > 0.0f || r.eta > 0.0f)) {
                    float nx, ny, rx, ry, refl = r.reflectivity;;
                    
                    Vector2f normal = new Vector2f();
                    gradient(x, y, normal);
                    nx = normal.x * sign;
                    ny = normal.y * sign;
                    
                    if (r.eta > 0.0f) {
                        Vector2f refraction = new Vector2f();
                        // if (refract(dx, dy, nx, ny, sign < 0.0f ? r.eta : 1.0f / r.eta, &rx, &ry)) {
                        if (refract(dx, dy, nx, ny, sign < 0.0f ? r.eta : 1.0f / r.eta, refraction)) {
                            rx = refraction.x;
                            ry = refraction.y;
                            
                            // sum += (1.0f - refl) * trace(x - nx * BIAS, y - ny * BIAS, rx, ry, depth + 1);
                            sum.addLocal( trace(x - nx * BIAS, y - ny * BIAS, rx, ry, depth + 1).multLocal(1.0f - refl) );
                        } else {
                            refl = 1.0f; // Total internal reflection
                        }
                    }
                    if (refl > 0.0f) {
                        Vector2f reflect = new Vector2f();
                        reflect(dx, dy, nx, ny, reflect);
                        rx = reflect.x;
                        ry = reflect.y;
                        // sum += r.reflectivity * trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1);
                        sum.addLocal( trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1).multLocal(r.reflectivity) );
                    }
                }
                return sum;
            }
            t += r.sd * sign;
        }
        // return 0f;
        return new Color(0);
    }
}
