package net.jmecn;

import net.jmecn.algo.*;
import net.jmecn.scene.*;
import net.jmecn.scene.all.*;

/**
 * 应用程序主类
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
    
    // Scene
    private Scene2D scene = new TestConcaveLens();

    // Render task
    private RenderTask task = new BeerLambert();
    
    protected int width;
    protected int height;
    protected String title;

    // Screen
    private Screen screen;

    // Render target
    private Image renderTarget;
    
    private boolean isRunning;

    /**
     * 构造方法
     */
    public Main() {
        width = 512;
        height = 512;
        title = "Light2d";
        
        // 改变运行状态
        isRunning = true;
    }

    /**
     * 启动程序
     */
    public void start() {
        // 创建主窗口
        screen = new Screen(width, height, title);
        
        // 实例化renderTarget
        renderTarget = new Image(width, height);
        
        task.setRenderTarget(renderTarget);
        task.setScene(scene);
        new Thread(task).start();
        
        while (isRunning) {
            try {
                Thread.sleep(64);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            screen.swapBuffer(renderTarget);
        }
    }

    /**
     * 停止程序
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * 设置分辨率
     * @param width
     * @param height
     */
    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
