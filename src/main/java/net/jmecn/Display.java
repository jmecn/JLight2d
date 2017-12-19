package net.jmecn;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;

/**
 * Use a window to display the render process.
 * 
 * @author yanmaoyuan
 *
 */
public class Display {

    /**
     * Render task
     */
    private Renderer renderer;
    private ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    
    // Render target
    private Image renderTarget;
    protected int width;
    protected int height;
    private byte[] components;
    
    // Canvas
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private BufferedImage displayImage;
    private byte[] displayComponents;
    Graphics graphics;
    
    // Window
    protected String title;
    private JFrame frame;
    
    private JFrame parent;
    
    /**
     * Initialize the display window
     * @param renderer
     */
    public Display(JFrame parent, Renderer renderer) {
        this.parent = parent;
        this.renderer = renderer;
        this.title = renderer.toString();
        
        setupRenderTarget();
        setupCanvas();
        setupWindow();
    }

    /**
     * start main loop
     */
    public void start() {
        if (future != null) {
            return;
        }
        
        future = threadPool.submit(renderer);
        
        long startTime = System.currentTimeMillis();
        
        runLoop();
        
        float timeInSecond = (float) (System.currentTimeMillis() - startTime) / 1000L;
        title += String.format(" %.2fs", timeInSecond);
        frame.setTitle(title);
    }

    /**
     * Display the render target on canvas
     */
    private void runLoop() {
        
        graphics = bufferStrategy.getDrawGraphics();
        
        while (future != null && !future.isDone() && !future.isCancelled()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            swapBuffer();
        }
        future = null;
        
        graphics.dispose();
    }
    
    /**
     * Swap buffer
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
        
        graphics.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
        
        // display
        bufferStrategy.show();
    }
    
    /**
     * Set render target
     */
    private void setupRenderTarget() {
        this.renderTarget = renderer.getRenderTarget();
        this.width = renderTarget.getWidth();
        this.height = renderTarget.getHeight();
        this.components = renderTarget.getComponents();
    }
    
    /**
     * Setup canvas
     */
    private void setupCanvas() {
        // canvas size
        Dimension size = new Dimension(width, height);
        
        // setup canvas
        canvas = new Canvas();
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);
        canvas.setFocusable(true);

        // setup buffer strategy
        canvas.createBufferStrategy(1);
        bufferStrategy = canvas.getBufferStrategy();
        
        // setup display image
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        displayComponents = ((DataBufferByte)displayImage.getRaster().getDataBuffer()).getData();
    }
    
    /**
     * Setup window
     */
    private void setupWindow() {
        // setup window
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(width, height);
        frame.setTitle(title);
        frame.add(canvas);// add canvas
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(parent);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                threadPool.shutdown();
                future = null;
            }
        });
    }
    
}
