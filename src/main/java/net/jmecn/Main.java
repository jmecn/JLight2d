package net.jmecn;

import javax.swing.SwingUtilities;

/**
 * Main
 * 
 * @author yanmaoyuan
 *
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SettingsDialog();
            }
        });
    }
}
