package net.jmecn;

/**
 * 用于保存图像数据。 图像格式采用3BYTE_RGB。
 * 
 * @author yanmaoyuan
 *
 */
public class Image {

    // 图片的宽度
    protected final int width;
    // 图片的高度
    protected final int height;
    // 颜色数据
    protected final byte[] components;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new byte[width * height * 3];
    }

    /**
     * 纯色填充
     * 
     * @param color
     */
    public void fill(byte color) {
        int length = width * height;
        for (int i = 0; i < length; i++) {
            int index = i * 3;

            // 使用一个判断，避免无谓的赋值。
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