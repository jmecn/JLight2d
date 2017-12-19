package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;
import static net.jmecn.scene.ShapeSDF.planeSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A half circle.
 * 
 * @author yanmaoyuan
 *
 */
public class TestHalfCircle extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result a = new Result(   circleSDF(x, y, 0.5f, 0.5f, 0.2f), Color.WHITE );
        Result b = new Result(    planeSDF(x, y, 0.0f, 0.5f, 0.0f, 1.0f), new Color(0.8f) );
        return intersectOp(a, b);
    }
}
