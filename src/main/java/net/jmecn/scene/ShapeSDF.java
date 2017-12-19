package net.jmecn.scene;

import static net.jmecn.math.FMath.*;

/**
 * Use SDF(signed distance function) to define 2d Shapes.
 * 
 * @author yanmaoyuan
 *
 */
public final class ShapeSDF {
    
    public static float circleSDF(float x, float y, float cx, float cy, float r) {
        float ux = x - cx, uy = y - cy;
        return sqrtf(ux * ux + uy * uy) - r;
    }

    public static float boxSDF(float x, float y, float cx, float cy, float theta, float sx, float sy) {
        float costheta = cosf(theta), sintheta = sinf(theta);
        float dx = fabsf((x - cx) * costheta + (y - cy) * sintheta) - sx;
        float dy = fabsf((y - cy) * costheta - (x - cx) * sintheta) - sy;
        float ax = fmaxf(dx, 0.0f), ay = fmaxf(dy, 0.0f);
        return fminf(fmaxf(dx, dy), 0.0f) + sqrtf(ax * ax + ay * ay);
    }
    
    public static float planeSDF(float x, float y, float px, float py, float nx, float ny) {
        return (x - px) * nx + (y - py) * ny;
    }
    
    public static float segmentSDF(float x, float y, float ax, float ay, float bx, float by) {
        float vx = x - ax, vy = y - ay, ux = bx - ax, uy = by - ay;
        float t = fmaxf(fminf((vx * ux + vy * uy) / (ux * ux + uy * uy), 1.0f), 0.0f);
        float dx = vx - ux * t, dy = vy - uy * t;
        return sqrtf(dx * dx + dy * dy);
    }

    public static float capsuleSDF(float x, float y, float ax, float ay, float bx, float by, float r) {
        // return segmentSDF(x, y, ax, ay, bx, by) - r;
        float vx = x - ax, vy = y - ay, ux = bx - ax, uy = by - ay;
        float t = fmaxf(fminf((vx * ux + vy * uy) / (ux * ux + uy * uy), 1.0f), 0.0f);
        float dx = vx - ux * t, dy = vy - uy * t;
        return sqrtf(dx * dx + dy * dy) - r;
    }
    
    public static float triangleSDF(float x, float y, float ax, float ay, float bx, float by, float cx, float cy) {
        float d = fminf(fminf(
            segmentSDF(x, y, ax, ay, bx, by),
            segmentSDF(x, y, bx, by, cx, cy)),
            segmentSDF(x, y, cx, cy, ax, ay));
        return (bx - ax) * (y - ay) > (by - ay) * (x - ax) && 
               (cx - bx) * (y - by) > (cy - by) * (x - bx) && 
               (ax - cx) * (y - cy) > (ay - cy) * (x - cx) ? -d : d;
    }
    
    public static float ngonSDF(float x, float y, float cx, float cy, float r, float n) {
        float ux = x - cx, uy = y - cy, a = TWO_PI / n;
        float t = fmodf(atan2f(uy, ux) + TWO_PI, a), s = sqrtf(ux * ux + uy * uy);
        return planeSDF(s * cosf(t), s * sinf(t), r, 0.0f, cosf(a * 0.5f), sinf(a * 0.5f));
    }
}
