package net.jmecn.algo;

import static net.jmecn.FMath.*;

import net.jmecn.scene.Result;

public class Csg extends Base {

    protected Result scene(float x, float y) {
        return scene2d.scene(x, y);
    }

    @Override
    protected float trace(float ox, float oy, float dx, float dy) {
        float t = 0.001f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            Result r = scene(ox + dx * t, oy + dy * t);
            if (r.sd < EPSILON)
                return r.emissive;
            t += r.sd;
        }
        return 0.0f;
    }
}
