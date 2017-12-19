package net.jmecn.scene.all;

import static net.jmecn.math.FMath.fabsf;
import static net.jmecn.scene.ShapeSDF.capsuleSDF;

import net.jmecn.math.Color;
import net.jmecn.scene.Result;
import net.jmecn.scene.Scene2D;

/**
 * M
 * 
 * @author yanmaoyuan
 *
 */
public class TestM extends Scene2D {
    
    @Override
    public Result scene(float x, float y) {
        x = fabsf(x - 0.5f) + 0.5f;
        Result a = new Result( capsuleSDF(x, y, 0.75f, 0.25f, 0.75f, 0.75f, 0.05f), Color.WHITE );
        Result b = new Result( capsuleSDF(x, y, 0.75f, 0.25f, 0.50f, 0.75f, 0.05f), Color.WHITE );
        return a.sd < b.sd ? a : b;
    }
}
