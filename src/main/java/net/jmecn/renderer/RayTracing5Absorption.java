package net.jmecn.renderer;

import static net.jmecn.math.FMath.*;

import net.jmecn.math.Color;
import net.jmecn.math.Vector2f;
import net.jmecn.scene.Result;

public class RayTracing5Absorption extends RayTracing4Fresnel {

    protected float beerLambert(float a, float d) {
        return expf(-a * d);
    }
    
    protected Color beerLambert(Color a, float d) {
        Color c = new Color( expf(-a.r * d), expf(-a.g * d), expf(-a.b * d) );
        return c;
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
                    
                    // normalize
                    nx = normal.x;
                    ny = normal.y;
                    float s = 1.0f / (nx * nx + ny * ny);
                    
                    nx = normal.x * sign * s;
                    ny = normal.y * sign * s;
                    
                    if (r.eta > 0.0f) {
                        Vector2f refraction = new Vector2f();
                        if (refract(dx, dy, nx, ny, sign < 0.0f ? r.eta : 1.0f / r.eta, refraction)) {
                            rx = refraction.x;
                            ry = refraction.y;
                            
                            float cosi = -(dx * nx + dy * ny);
                            float cost = -(rx * nx + ry * ny);
                            refl = sign < 0.0f ? fresnel(cosi, cost, r.eta, 1.0f) : fresnel(cosi, cost, 1.0f, r.eta);
                            // refl = sign < 0.0f ? schlick(cosi, cost, r.eta, 1.0f) : schlick(cosi, cost, 1.0f, r.eta);
                            
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
                        sum.addLocal( trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1).multLocal(r.reflectivity ));
                    }
                }
                // return sum;
                // return sum * beerLambert(r.absorption, t);
                return sum.multLocal( beerLambert(r.absorption, t) );
            }
            t += r.sd * sign;
        }
        // return 0.0f;
        return new Color(0.0f);
    }
}
