package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.boxSDF;
import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A light circle at left-top corner, and a glass box at center.
 * 
 * @author yanmaoyuan
 *
 */
public class TestGlassBox extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, -0.2f, -0.2f, 0.1f), new Color(10.0f), 0.0f, 0.0f );
        Result b = new Result(    boxSDF(x, y, 0.5f, 0.5f, 0.0f, 0.3f, 0.2f), Color.BLACK, 0.2f, 1.5f, new Color(1.0f, 4.0f, 4.0f));
        return unionOp(a, b);
    }
}