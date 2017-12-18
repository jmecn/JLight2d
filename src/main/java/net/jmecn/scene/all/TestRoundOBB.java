package net.jmecn.scene.all;

import static net.jmecn.FMath.*;
import static net.jmecn.scene.ShapeSDF.*;

import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * A round edge OBB
 * 
 * @author yanmaoyuan
 *
 */
public class TestRoundOBB extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        return new Result(boxSDF(x, y, 0.5f, 0.5f, TWO_PI / 16.0f, 0.3f, 0.1f) - 0.1f, 1.0f );
    }
}
