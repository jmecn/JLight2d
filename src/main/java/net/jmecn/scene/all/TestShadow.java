package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * Two lighting circle and one solid circle.
 * 
 * @author yanmaoyuan
 *
 */
public class TestShadow extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result r1 = new Result( circleSDF(x, y, 0.3f, 0.3f, 0.10f), new Color(2.0f) );
        Result r2 = new Result( circleSDF(x, y, 0.3f, 0.7f, 0.05f), new Color(0.8f) );
        Result r3 = new Result( circleSDF(x, y, 0.7f, 0.5f, 0.10f), Color.BLACK );
        return unionOp(unionOp(r1, r2), r3);
    }
}