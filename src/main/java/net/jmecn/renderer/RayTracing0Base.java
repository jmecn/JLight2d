package net.jmecn.renderer;

import static net.jmecn.math.FMath.*;

import net.jmecn.Renderer;
import net.jmecn.math.Color;

public class RayTracing0Base extends Renderer {

    /**
     * Calculate the signed distance from a point to a circle.
     * 
     * SDF is short of Signed Distance Filed.
     * 
     * @param x Point X
     * @param y Point Y
     * @param cx Center X
     * @param cy Center Y
     * @param r Radius
     * @return sd &gt; 0 The point is out of the circle;
     *         sd = 0 The point is on border of the circle;
     *         sd &lt; 0 The point is in the circle;
     */
    float circleSDF(float x, float y, float cx, float cy, float r) {
        float ux = x - cx, uy = y - cy;
        return sqrtf(ux * ux + uy * uy) - r;
    }
    
    /**
     * Use ray marching to calculate the nearest point in the scene from origin.
     * 
     * https://developer.nvidia.com/gpugems/GPUGems2/gpugems2_chapter08.html
     * 
     * @param ox Origin X
     * @param oy Origin Y
     * @param dx Direction X
     * @param dy Direction Y
     * @return
     */
    protected Color trace(float ox, float oy, float dx, float dy) {
        float t = 0.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            // signed distance
            float sd = circleSDF(ox + dx * t, oy + dy * t, 0.5f, 0.5f, 0.1f);
            if (sd < EPSILON)
                return new Color(2.0f);// emissive
            
            // ray marching
            t += sd;
        }
        return new Color(0.0f);
    }

    /**
     * Use Monte Carlo integration to calculate light intensity of each pixel.
     * 
     * @param x
     * @param y
     * @return
     */
    protected Color sample(float x, float y) {
        Color sum = new Color(0);
        for (int i = 0; i < samples; i++) {
            float a = monteCarloMethod(i);
            // ray tracing
            sum.addLocal( trace(x, y, cosf(a), sinf(a)) );
        }
        return sum.multLocal(invSamples);
    }

    @Override
    public Void call() {
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float s = (float) x / width;
                float t = (float) y / height;
                
                Color c = sample(s, t);
                
                components[index] = (byte)(fminf(c.r * 255.0f, 255.0f));
                components[index+1] = (byte)(fminf(c.g * 255.0f, 255.0f));
                components[index+2] = (byte)(fminf(c.b * 255.0f, 255.0f));
                
                index += 3;
            }
        }
        return null;
    }
}
