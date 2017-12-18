package net.jmecn.scene.all;

import static net.jmecn.FMath.*;
import static net.jmecn.scene.ShapeSDF.*;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A lighting circle, and two black boxes.
 * 
 * @author yanmaoyuan
 *
 */
public class TestReflection2 extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.4f, 0.2f, 0.1f), 2.0f, 0.0f );
        Result b = new Result(    boxSDF(x, y, 0.5f, 0.8f, TWO_PI / 16.0f, 0.1f, 0.1f), 0.0f, 0.9f);
        Result c = new Result(    boxSDF(x, y, 0.8f, 0.5f, TWO_PI / 16.0f, 0.1f, 0.1f), 0.0f, 0.9f);
        return unionOp(a, unionOp(b, c));
    }

}