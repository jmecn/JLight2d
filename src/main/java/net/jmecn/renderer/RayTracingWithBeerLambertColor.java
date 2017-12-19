package net.jmecn.renderer;

import static net.jmecn.FMath.*;
import static net.jmecn.scene.ShapeSDF.*;

import net.jmecn.Renderer;

public class RayTracingWithBeerLambertColor extends Renderer {

    class Color {
        float r, g, b;
        
        public Color(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    
    final Color BLACK = new Color(0, 0, 0);
    
    Color colorAdd(Color a, Color b) {
        return new Color(a.r + b.r, a.g + b.g, a.b + b.b );
    }
    
    Color colorMultiply(Color a, Color b) {
        Color c = new Color( a.r * b.r, a.g * b.g, a.b * b.b );
        return c;
    }

    Color colorScale(Color a, float s) {
        Color c = new Color( a.r * s, a.g * s, a.b * s );
        return c;
    }
    
    class Result {
        float sd;
        float reflectivity;
        float eta;
        Color emissive;
        Color absorption;
        
        public Result(float sd, float reflectivity, float eta, Color emissive, Color absorption) {
            this.sd = sd;
            this.reflectivity = reflectivity;
            this.eta = eta;
            this.emissive = emissive;
            this.absorption = absorption;
        }
    }
    
    Result unionOp(Result a, Result b) {
        return a.sd < b.sd ? a : b;
    }

    Result intersectOp(Result a, Result b) {
        Result r = a.sd > b.sd ? b : a;
        r.sd = a.sd > b.sd ? a.sd : b.sd;
        return r;
    }

    Result subtractOp(Result a, Result b) {
        Result r = a;
        r.sd = (a.sd > -b.sd) ? a.sd : -b.sd;
        return r;
    }

    Result complementOp(Result a) {
        a.sd = -a.sd;
        return a;
    }
    
    Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.5f, -0.2f, 0.1f), 0.0f, 0.0f, new Color( 10.0f, 10.0f, 10.0f ), BLACK );
        Result b = new Result(   ngonSDF(x, y, 0.5f, 0.5f, 0.25f, 5.0f), 0.0f, 1.5f, BLACK, new Color( 4.0f, 4.0f, 1.0f) );
        return unionOp(a, b);
    }

    void gradient(float x, float y, Vector2f n) {
        n.x = (scene(x + EPSILON, y).sd - scene(x - EPSILON, y).sd) * (0.5f / EPSILON);
        n.y = (scene(x, y + EPSILON).sd - scene(x, y - EPSILON).sd) * (0.5f / EPSILON);
    }

    void reflect(float ix, float iy, float nx, float ny, Vector2f r) {
        float idotn2 = (ix * nx + iy * ny) * 2.0f;
        r.x = ix - idotn2 * nx;
        r.y = iy - idotn2 * ny;
    }
    
    boolean refract(float ix, float iy, float nx, float ny, float eta, Vector2f r) {
        float idotn = ix * nx + iy * ny;
        float k = 1.0f - eta * eta * (1.0f - idotn * idotn);
        if (k < 0.0f)
            return false; // Total internal reflection
        float a = eta * idotn + sqrtf(k);
        r.x = eta * ix - a * nx;
        r.y = eta * iy - a * ny;
        return true;
    }
    
    float fresnel(float cosi, float cost, float etai, float etat) {
        float rs = (etat * cosi - etai * cost) / (etat * cosi + etai * cost);
        float rp = (etai * cosi - etat * cost) / (etai * cosi + etat * cost);
        return (rs * rs + rp * rp) * 0.5f;
    }

    float schlick(float cosi, float cost, float etai, float etat) {
        float r0 = (etai - etat) / (etai + etat);
        r0 *= r0;
        float a = 1.0f - (etai < etat ? cosi : cost);
        float aa = a * a;
        return r0 + (1.0f - r0) * aa * aa * a;
    }
    
    Color beerLambert(Color a, float d) {
        Color c = new Color( expf(-a.r * d), expf(-a.g * d), expf(-a.b * d) );
        return c;
    }
    
    Color trace(float ox, float oy, float dx, float dy, int depth) {
        float t = 1e-3f;
        float sign = scene(ox, oy).sd > 0.0f ? 1.0f : -1.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            float x = ox + dx * t, y = oy + dy * t;
            Result r = scene(x, y);
            if (r.sd * sign < EPSILON) {
                Color sum = r.emissive;
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
                            
                            sum = colorAdd(sum, colorScale(trace(x - nx * BIAS, y - ny * BIAS, rx, ry, depth + 1), 1.0f - refl));
                        } else {
                            refl = 1.0f; // Total internal reflection
                        }
                    }
                    if (refl > 0.0f) {
                        Vector2f reflect = new Vector2f();
                        reflect(dx, dy, nx, ny, reflect);
                        rx = reflect.x;
                        ry = reflect.y;
                        sum = colorAdd(sum, colorScale(trace(x + nx * BIAS, y + ny * BIAS, rx, ry, depth + 1), refl));
                    }
                }
                return colorMultiply(sum, beerLambert(r.absorption, t));
            }
            t += r.sd * sign;
        }
        Color black = BLACK;
        return black;
    }
    
    Color sample(float x, float y) {
        Color sum = BLACK;
        for (int i = 0; i < samples; i++) {
            float a = TWO_PI * (i + (float)rand() / RAND_MAX) / samples;
            sum = colorAdd(sum, trace(x, y, cosf(a), sinf(a), 0));
        }
        return colorScale(sum, 1.0f / samples);
    }

    @Override
    public Void call() {
        int p = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++, p += 3) {
                Color c = sample((float)x / width, (float)y / height);
                components[p] = (byte)(fminf(c.r * 255.0f, 255.0f));
                components[p+1] = (byte)(fminf(c.g * 255.0f, 255.0f));
                components[p+2] = (byte)(fminf(c.b * 255.0f, 255.0f));
            }
        }
        return null;
    }
}
