package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A convex lens.
 * 
 * @author yanmaoyuan
 *
 */
public class TestConvexLens extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.5f, -0.5f, 0.05f), 20.0f, 0.0f, 0.0f );
        Result b = new Result( circleSDF(x, y, 0.5f, 0.2f, 0.35f), 0.0f, 0.2f, 1.5f, 2.0f );
        Result c = new Result( circleSDF(x, y, 0.5f, 0.8f, 0.35f), 0.0f, 0.2f, 1.5f, 2.0f );
        return unionOp(a, intersectOp(b, c));
    }

}
