package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;
import static net.jmecn.scene.ShapeSDF.ngonSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A capsule
 * 
 * @author yanmaoyuan
 *
 */
public class TestNgon extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.5f, -0.2f, 0.1f), new Color( 10.0f), 0.0f, 0.0f, Color.BLACK );
        Result b = new Result(   ngonSDF(x, y, 0.5f, 0.5f, 0.25f, 5.0f), Color.BLACK, 0.2f, 1.5f, new Color( 4.0f, 4.0f, 1.0f) );
        return unionOp(a, b);
    }
}
