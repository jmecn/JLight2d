package net.jmecn;

import net.jmecn.scene.Scene2D;

public abstract class RenderTask implements Runnable {
    
    public final static int N = 128;
    public final static float MAX_STEP = 64;
    public final static float MAX_DISTANCE = 5.0f;
    public final static float BIAS = 1e-4f;
    public final static int MAX_DEPTH = 3;
    
    protected int width;
    protected int height;
    protected byte[] components;
    
    protected Scene2D scene2d;
    
    public void setRenderTarget(Image renderTarget) {
        this.width = renderTarget.getWidth();
        this.height = renderTarget.getHeight();
        this.components = renderTarget.getComponents();
    }

    public void setScene(Scene2D scene2d) {
        this.scene2d = scene2d;
    }
}
