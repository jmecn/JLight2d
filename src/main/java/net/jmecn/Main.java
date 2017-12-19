package net.jmecn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.jmecn.Renderer.SampleMethod;
import net.jmecn.renderer.RayTracingWithBeerLambert;
import net.jmecn.scene.Scene2D;
import net.jmecn.scene.all.TestShadow;

/**
 * Main
 * 
 * @author yanmaoyuan
 *
 */
public class Main {
    
    public static void main(String[] args) throws ClassNotFoundException {
        new Main();
    }
    
    private JComboBox<Integer> listResolution;
    private JComboBox<Integer> listSamples;
    private JComboBox<String> listSampleMethod;
    private JList<String> listRenderer;
    private JList<String> listScene;
    private JButton btnStart;
    private JLabel lblStatus;
    
    private int resolution;
    private int samples;
    private SampleMethod sampleMethod;
    private Scene2D scene2d;
    private Renderer renderer;
    
    public Main() {
        setupComponents();
        
        JFrame frame = new JFrame();
        frame.setContentPane(getContentPanel());
        frame.setTitle("Light2D");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private void display() {
        resolution = (Integer)listResolution.getSelectedItem();
        samples = (Integer)listSamples.getSelectedItem();
        sampleMethod = SampleMethod.valueOf((String)listSampleMethod.getSelectedItem());
        
        try {
            scene2d = (Scene2D)Class.forName((String)listScene.getSelectedValue()).newInstance();
        } catch (Exception e) {
            scene2d = new TestShadow();
        }
        
        try {
            renderer = (Renderer)Class.forName((String)listRenderer.getSelectedValue()).newInstance();
        } catch (Exception e) {
            renderer = new RayTracingWithBeerLambert();
        }
        
        renderer.setRenderTarget(new Image(resolution, resolution));
        renderer.setSampleMethod(sampleMethod);
        renderer.setSamples(samples);
        renderer.setScene(scene2d);
        
        lblStatus.setText(renderer.toString());
        
        Display app = new Display(renderer);
        app.start();
    }
    
    private void setupComponents() {
        listResolution = new JComboBox<Integer>();
        listResolution.addItem(128);
        listResolution.addItem(256);
        listResolution.addItem(384);
        listResolution.addItem(512);
        listResolution.addItem(640);
        listResolution.addItem(768);
        listResolution.addItem(896);
        listResolution.addItem(1024);
        listResolution.setSelectedIndex(3);
        
        listSamples = new JComboBox<Integer>();
        listSamples.addItem(16);
        listSamples.addItem(32);
        listSamples.addItem(64);
        listSamples.addItem(128);
        listSamples.addItem(256);
        listSamples.setSelectedIndex(2);
        
        listSampleMethod = new JComboBox<String>();
        listSampleMethod.addItem(SampleMethod.Uniform.name());
        listSampleMethod.addItem(SampleMethod.Stratified.name());
        listSampleMethod.addItem(SampleMethod.Jittered.name());
        listSampleMethod.setSelectedIndex(2);
        
        listScene = new JList<String>();
        try {
            List<String> clazzes = scanClazz(Scene2D.class, "net.jmecn.scene.all");
            listScene.setModel(new AbstractListModel<String>() {
                private static final long serialVersionUID = 1L;
                public int getSize() { return clazzes.size(); }
                public String getElementAt(int i) { return clazzes.get(i); }
            });
            listScene.setSelectedIndex(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        listRenderer = new JList<String>();
        try {
            List<String> clazzes = scanClazz(Renderer.class, "net.jmecn.renderer");
            listRenderer.setModel(new AbstractListModel<String>() {
                private static final long serialVersionUID = 1L;
                public int getSize() { return clazzes.size(); }
                public String getElementAt(int i) { return clazzes.get(i); }
            });
            listRenderer.setSelectedIndex(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        btnStart = new JButton("go");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                new Thread() {
                    public void run() {
                        display();
                    }
                }.start();
            }
        });
        
        lblStatus = new JLabel("Light2D");
    }
    
    private JPanel getContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        /// North
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        
        JLabel lblSampleMethod = new JLabel("Sample Method:");
        northPanel.add(lblSampleMethod);
        northPanel.add(listSampleMethod);
        
        JLabel lblSamples = new JLabel("Samples per pixel:");
        northPanel.add(lblSamples);
        northPanel.add(listSamples);
        
        JLabel lblResolution = new JLabel("Resolution:");
        northPanel.add(lblResolution);
        northPanel.add(listResolution);
        
        northPanel.add(btnStart);
        
        //// Center
        
        JSplitPane splitPane = new JSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // left
        JPanel leftPanel = new JPanel(new BorderLayout());
        splitPane.setLeftComponent(leftPanel);
        leftPanel.add(new JLabel("Renderer"), BorderLayout.NORTH);
        JScrollPane leftScroll = new JScrollPane();
        leftPanel.add(leftScroll, BorderLayout.CENTER);
        leftScroll.setViewportView(listRenderer);

        // Right
        JPanel rightPanel = new JPanel(new BorderLayout());
        splitPane.setRightComponent(rightPanel);
        rightPanel.add(new JLabel("Scene"), BorderLayout.NORTH);
        JScrollPane rightScroll = new JScrollPane();
        rightPanel.add(rightScroll, BorderLayout.CENTER);
        rightScroll.setViewportView(listScene);
        
        /// SOUTH
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(lblStatus);
        
        return mainPanel;
    }
    
    private List<String> scanClazz(Class<?> superClass, String scanPack) throws ClassNotFoundException {
        List<String> results = new ArrayList<String>();
        scanPack = scanPack.replace(".", File.separator);
        
        String classpath = Main.class.getResource("/").getPath();
        
        // search class file
        File[] files = new File(classpath + scanPack).listFiles();
        
        // use this to remove prefix
        classpath = classpath.replace("/","\\").replaceFirst("\\\\","");
        
        for(File file : files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".class")) {
                    String filePath = file.getPath();
                    filePath = filePath.replace(classpath,"").replace("\\",".").replace(".class","");
                    
                    if (superClass.isAssignableFrom(Class.forName(filePath))) {
                        results.add(filePath);
                    }
                }
            }
        }
        
        return results;
    }
    
}
