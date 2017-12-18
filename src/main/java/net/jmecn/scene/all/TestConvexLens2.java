package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;
import static net.jmecn.scene.ShapeSDF.planeSDF;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A convex lens.
 * 
 * @author yanmaoyuan
 *
 */
public class TestConvexLens2 extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.5f, -0.5f, 0.05f), 20.0f, 0.0f, 0.0f );
        Result b = new Result( circleSDF(x, y, 0.5f, 0.5f, 0.2f), 0.0f, 0.2f, 1.5f, 4.0f );
        Result c = new Result(  planeSDF(x, y, 0.5f, 0.5f, 0.0f, -1.0f), 0.0f, 0.2f, 1.5f, 4.0f );
        return unionOp(a, intersectOp(b, c));
    }

}
