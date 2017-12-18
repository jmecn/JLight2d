package net.jmecn;

import static net.jmecn.FMath.RAND_MAX;
import static net.jmecn.FMath.TWO_PI;
import static net.jmecn.FMath.rand;

import net.jmecn.scene.Scene2D;

/**
 * A base class for all renderer
 * @author yanmaoyuan
 *
 */
public abstract class Renderer implements Runnable {
    
    public enum SampleMethod {
        Uniform,
        Stratified,
        Jittered,
    }
    
    public final static float MAX_STEP = 64;
    public final static float MAX_DISTANCE = 5.0f;
    public final static float BIAS = 1e-4f;
    public final static int MAX_DEPTH = 3;
    
    // Render target
    protected int width;
    protected int height;
    protected byte[] components;
    
    // Scene
    protected Scene2D scene2d;
    
    // Samples of each pixel
    protected int samples = 128;
    
    // Sample method
    protected SampleMethod sampleMethod = SampleMethod.Jittered;
    
    /**
     * Set render target
     * @param renderTarget
     */
    public void setRenderTarget(Image renderTarget) {
        this.width = renderTarget.getWidth();
        this.height = renderTarget.getHeight();
        this.components = renderTarget.getComponents();
    }

    /**
     * Set scene
     * @param scene2d
     */
    public void setScene(Scene2D scene2d) {
        this.scene2d = scene2d;
    }
    
    /**
     * Set samples per pixel
     * 
     * @param samples
     */
    public void setSamples(int samples) {
        if (samples > 0) {
            this.samples = samples;
        }
    }

    /**
     * Set sample method
     * 
     * @param sampleMethod
     */
    public void setSampleMethod(SampleMethod sampleMethod) {
        if (sampleMethod != null) {
            this.sampleMethod = sampleMethod;
        }
    }
    
    /**
     * Monte Carlo method
     * @param i
     * @return
     */
    protected float monteCarloMethod(int i) {
        float a = 0;
        switch (sampleMethod) {
            case Uniform:
                a = TWO_PI * rand() / RAND_MAX;
                break;
            case Stratified:
                a = TWO_PI * i / samples;
                break;
            case Jittered:
                a = TWO_PI * ( i + (float)rand() / RAND_MAX) / samples;
                break;
        }
        
        return a;
    }
    
}
