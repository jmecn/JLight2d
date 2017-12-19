package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.capsuleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A capsule
 * 
 * @author yanmaoyuan
 *
 */
public class TestCapsule extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        return new Result(  capsuleSDF(x, y, 0.4f, 0.4f, 0.6f, 0.6f, 0.1f), new Color(1.0f) );
    }
}
