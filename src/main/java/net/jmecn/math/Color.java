package net.jmecn.math;

public class Color {

    public float r, g, b;

    public final static Color BLACK = new Color(0f);
    public final static Color WHITE = new Color(1f);
    
    public Color() {
        r = g = b = 0f;
    }
    
    public Color(float color) {
        r = g = b = color;
    }
    
    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
    }
    
    public Color add(Color color) {
        return new Color(r + color.r, g + color.g, b + color.b );
    }
    
    public Color addLocal(Color color) {
        r += color.r;
        g += color.g;
        b += color.b;
        return this;
    }
    
    public Color add(float scalor) {
        return new Color(r + scalor, g + scalor, b + scalor );
    }
    
    public Color addLocal(float scalor) {
        r += scalor;
        g += scalor;
        b += scalor;
        return this;
    }
    
    public Color mult(Color color) {
        return new Color( r * color.r, g * color.g, b * color.b );
    }
    
    public Color multLocal(Color color) {
        r *= color.r;
        g *= color.g;
        b *= color.b;
        return this;
    }

    public Color mult(float scalor) {
        return new Color( r * scalor, g * scalor, b * scalor );
    }
    
    public Color multLocal(float scalor) {
        r *= scalor;
        g *= scalor;
        b *= scalor;
        return this;
    }
}