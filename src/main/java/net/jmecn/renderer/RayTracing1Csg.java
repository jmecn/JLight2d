package net.jmecn.renderer;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;

public class RayTracing1Csg extends RayTracing0Base {

    protected Result scene(float x, float y) {
        return scene2d.scene(x, y);
    }

    @Override
    protected Color trace(float ox, float oy, float dx, float dy) {
        float t = 0.001f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            
            Result r = scene(ox + dx * t, oy + dy * t);
            
            if (r.sd < EPSILON)
                return r.emissive;// emissive
            
            // ray marching
            t += r.sd;
        }
        return Color.BLACK;
    }
}
