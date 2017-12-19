package net.jmecn;

/**
 * Use this class to store image data.
 * Format: 3BYTE_RGB
 * 
 * @author yanmaoyuan
 *
 */
public class Image {

    private final int width;
    private final int height;
    private final byte[] components;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new byte[width * height * 3];
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getComponents() {
        return components;
    }

}