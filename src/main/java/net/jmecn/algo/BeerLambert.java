package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.scene.Result;

public class BeerLambert extends Fresnel {

    protected float beerLambert(float a, float d) {
        return expf(-a * d);
    }
    
    @Override
    protected float trace(float ox, float oy, float dx, float dy, int depth) {
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
                    
                    // normalize
                    nx = normal.x;
                    ny = normal.y;
                    float s = 1.0f / (nx * nx + ny * ny);
                    
                    nx = normal.x * sign * s;
                    ny = normal.y * sign * s;
                    
                    if (r.eta > 0.0f) {
                        Vec2 refraction = new Vec2();
                        if (refract(dx, dy, nx, ny, sign < 0.0f ? r.eta : 1.0f / r.eta, refraction)) {
                            rx = refraction.x;
                            ry = refraction.y;
                            
                            float cosi = -(dx * nx + dy * ny);
                            float cost = -(rx * nx + ry * ny);
                            refl = sign < 0.0f ? fresnel(cosi, cost, r.eta, 1.0f) : fresnel(cosi, cost, 1.0f, r.eta);
                            // refl = sign < 0.0f ? schlick(cosi, cost, r.eta, 1.0f) : schlick(cosi, cost, 1.0f, r.eta);
                            
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
                // return sum;
                return sum * beerLambert(r.absorption, t);
            }
            t += r.sd * sign;
        }
        return 0.0f;
    }
}
