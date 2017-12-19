package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * Two lighting circles at center.
 * 
 * @author yanmaoyuan
 *
 */
public class TestUnion extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.4f, 0.5f, 0.20f), Color.WHITE );
        Result b = new Result( circleSDF(x, y, 0.6f, 0.5f, 0.20f), new Color(0.8f));
        return unionOp(a, b);
    }
}
