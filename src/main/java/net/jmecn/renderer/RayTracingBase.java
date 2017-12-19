package net.jmecn.renderer;

import static net.jmecn.FMath.*;

import net.jmecn.Renderer;

public class RayTracingBase extends Renderer {

    /**
     * Calculate the signed distance from a point to a circle border.
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
    protected float trace(float ox, float oy, float dx, float dy) {
        float t = 0.0f;
        for (int i = 0; i < MAX_STEP && t < MAX_DISTANCE; i++) {
            // signed distance
            float sd = circleSDF(ox + dx * t, oy + dy * t, 0.5f, 0.5f, 0.1f);
            if (sd < EPSILON)
                return 2.0f;// emissive
            
            // ray marching
            t += sd;
        }
        return 0.0f;
    }

    /**
     * Use Monte Carlo integration to calculate light intensity of each pixel.
     * 
     * @param x
     * @param y
     * @return
     */
    protected float sample(float x, float y) {
        float sum = 0.0f;
        for (int i = 0; i < samples; i++) {
            float a = monteCarloMethod(i);
            // ray tracing
            sum += trace(x, y, cosf(a), sinf(a));
        }
        return sum / samples;
    }

    @Override
    public Void call() {
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float u = (float) x / width;
                float v = (float) y / height;
                
                byte color = (byte)fminf(sample(u, v) * 255.0f, 255.0f);
                
                components[index] = color;
                components[index+1] = color;
                components[index+2] = color;
                
                index += 3;
            }
        }
        return null;
    }
}
