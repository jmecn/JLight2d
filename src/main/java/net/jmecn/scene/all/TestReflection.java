package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;
import static net.jmecn.scene.ShapeSDF.planeSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

public class TestReflection extends Scene2D {

    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.4f, 0.2f, 0.1f), new Color(2.0f), 0.0f );
        Result b = new Result(  planeSDF(x, y, 0.0f, 0.5f, 0.0f, -1.0f), Color.BLACK, 0.9f );
        Result c = new Result( circleSDF(x, y, 0.5f, 0.5f, 0.4f), Color.BLACK, 0.9f );
        return unionOp(a, subtractOp(b, c));
    }

}
