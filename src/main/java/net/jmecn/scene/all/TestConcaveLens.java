package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.boxSDF;
import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A concave lens.
 * 
 * @author yanmaoyuan
 *
 */
public class TestConcaveLens extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.5f, -0.5f, 0.05f), new Color(20.0f), 0.0f, 0.0f );
        Result b = new Result(    boxSDF(x, y, 0.5f, 0.5f, 0.0f, 0.2f, 0.1f), Color.BLACK, 0.2f, 1.5f, new Color(4.0f) );
        Result c = new Result( circleSDF(x, y, 0.5f, 0.12f, 0.35f), Color.BLACK, 0.2f, 1.5f, new Color(4.0f) );
        Result d = new Result( circleSDF(x, y, 0.5f, 0.87f, 0.35f), Color.BLACK, 0.2f, 1.5f, new Color(4.0f) );
        return unionOp(a, subtractOp(b, unionOp(c, d)));
    }

}
