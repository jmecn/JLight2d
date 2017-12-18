package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;
import static net.jmecn.scene.ShapeSDF.planeSDF;

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
        Result a = new Result(   circleSDF(x, y, 0.5f, 0.5f, 0.2f), 1.0f );
        Result b = new Result(    planeSDF(x, y, 0.0f, 0.5f, 0.0f, 1.0f), 0.8f );
        // Result c = new Result(  capsuleSDF(x, y, 0.4f, 0.4f, 0.6f, 0.6f, 0.1f), 1.0f );
        // Result d = new Result(      boxSDF(x, y, 0.5f, 0.5f, TWO_PI / 16.0f, 0.3f, 0.1f), 1.0f );
        // Result e = new Result(      boxSDF(x, y, 0.5f, 0.5f, TWO_PI / 16.0f, 0.3f, 0.1f) - 0.1f, 1.0f );
        // Result f = new Result( triangleSDF(x, y, 0.5f, 0.2f, 0.8f, 0.8f, 0.3f, 0.6f), 1.0f );
        // Result g = new Result( triangleSDF(x, y, 0.5f, 0.2f, 0.8f, 0.8f, 0.3f, 0.6f) - 0.1f, 1.0f );
        // return a;
        // return b;
        return intersectOp(a, b);
        // return c;
        // return d;
        // return e;
        // return f;
        // return g;
    }
}
