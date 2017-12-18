package net.jmecn;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import net.jmecn.algo.*;
import net.jmecn.scene.*;
import net.jmecn.scene.all.*;

/**
 * Main
 * 
 * @author yanmaoyuan
 *
 */
public class Main {

    public static void main(String[] args) {
        Main app = new Main();
        app.setResolution(512, 512);
        app.start();
    }
    
    /**
     * The scene to be rendered.
     */
    private Scene2D scene = new TestGlassBox();

    /**
     * Render task
     */
    private Renderer task = new BeerLambert();
    
    // Title
    protected String title = "Light2D";
    
    // Render target
    private Image renderTarget;
    protected int width = 512;
    protected int height = 512;
    private byte[] components;

    // Display image
    private BufferedImage displayImage;
    private byte[] displayComponents;

    // Window
    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    
    private boolean isRunning;

    public Main() {
    }

    /**
     * start main loop
     */
    public void start() {
        initScreen(width, height, title);
        
        renderTarget = new Image(width, height);
        components = renderTarget.getComponents();
        
        task.setRenderTarget(renderTarget);
        task.setScene(scene);
        new Thread(task).start();
        
        isRunning = true;
        runLoop();
    }
    
    private void initScreen(int width, int height, String title) {
        canvas = new Canvas();
        
        // 设置画布的尺寸
        Dimension size = new Dimension(width, height);
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);
        canvas.setFocusable(true);

        // 创建主窗口
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.add(canvas);// 设置画布
        frame.pack();
        frame.setVisible(true);
        centerScreen();// 窗口居中
        
        // 焦点集中到画布上，响应用户输入。
        canvas.requestFocus();
        
        // 创建双缓冲
        canvas.createBufferStrategy(1);
        bufferStrategy = canvas.getBufferStrategy();
        
        // 创建缓冲图像
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        // 获得图像中的数组
        displayComponents = ((DataBufferByte)displayImage.getRaster().getDataBuffer()).getData();
    }

    /**
     * Locate the frame to screen center.
     */
    private void centerScreen() {
        Dimension size = frame.getSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - size.width) / 2;
        int y = (screen.height - size.height) / 2;
        frame.setLocation(x, y);
    }
    
    /**
     * Display the render target on canvas
     */
    private void runLoop() {
        while (isRunning) {
            try {
                Thread.sleep(64);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            swapBuffer();
        }
    }
    
    /**
     * Swap buffer
     * @param image
     * @param fps
     */
    private void swapBuffer() {
        int length = width * height;
        for (int i = 0; i < length; i++) {
            // blue
            displayComponents[i * 3] = components[i * 3 + 2];
            // green
            displayComponents[i * 3 + 1] = components[i * 3 + 1];
            // red
            displayComponents[i * 3 + 2] = components[i * 3];
        }
        
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
        graphics.dispose();
        
        // display
        bufferStrategy.show();
    }
    
    /**
     * stop loop
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Set render target resolution
     * 
     * @param width
     * @param height
     */
    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Set screen title
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
