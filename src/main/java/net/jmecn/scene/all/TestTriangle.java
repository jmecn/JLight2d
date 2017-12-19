package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.triangleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A triangle
 * 
 * @author yanmaoyuan
 *
 */
public class TestTriangle extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        return new Result( triangleSDF(x, y, 0.5f, 0.2f, 0.8f, 0.8f, 0.3f, 0.6f), new Color(1.0f) );
    }
}
