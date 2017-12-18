package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.planeSDF;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A Plane.
 * 
 * @author yanmaoyuan
 *
 */
public class TestPlane extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        return new Result(planeSDF(x, y, 0.0f, 0.5f, 0.0f, 1.0f), 0.8f);
    }
}
