package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A lighting circle at center.
 * 
 * @author yanmaoyuan
 *
 */
public class TestCircle extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        return new Result( circleSDF(x, y, 0.5f, 0.5f, 0.1f), new Color(1f));
    }
}