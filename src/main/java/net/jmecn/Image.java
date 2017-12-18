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

    /**
     * fill the image with same color
     * 
     * @param color
     */
    public void fill(byte color) {
        int length = width * height;
        for (int i = 0; i < length; i++) {
            int index = i * 3;
            if (components[index] != color || components[index + 1] != color || components[index + 2] != color) {
                components[index] = color;
                components[index + 1] = color;
                components[index + 2] = color;
            }
        }
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