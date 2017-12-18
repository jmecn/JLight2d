package net.jmecn.scene.all;

import static net.jmecn.scene.ShapeSDF.circleSDF;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * One lighting circle subtract with another.
 * You can see a moon :)
 * 
 * @author yanmaoyuan
 *
 */
public class TestSubtract extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        Result a = new Result( circleSDF(x, y, 0.4f, 0.5f, 0.20f));
        Result b = new Result( circleSDF(x, y, 0.6f, 0.5f, 0.20f), 1f );
        return subtractOp(b, a);
    }
}
