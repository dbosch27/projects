/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbosch.dancelights;

import java.awt.Color;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 *
 * @author davidboschwitz
 */
public class LightFrame extends JFrame {

    /**
     * Creates new form JFrame
     */
    public LightFrame() {
        initComponents();
    }

    private static ArrayList<LightFrame> myFrames;
    private static Thread thread;
    private static long timeout, lastTap = 0;
    private static int nextLocationX = 0, nextLocationY = 0;
    private static boolean stop = false;
    private static final Color[] default_colors = new Color[]{Color.red, Color.BLUE, Color.yellow, Color.green, Color.magenta, Color.cyan, Color.white};
    private static Color[] colors;
    private boolean fullscreen = false;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyChar()) {
            case 't':
                tap();
                break;

            case 'q':
                stop();
                break;

            case 'f':
                //full screen (individual frames)
                if (!fullscreen) {
                    setBounds(getGraphicsConfiguration().getBounds());
                    getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
                    fullscreen = true;
                } else {
                    getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
                    //this.setBounds(0, 0, this.getToolkit().getScreenSize().width / 2, this.getToolkit().getScreenSize().height / 2);
                    fullscreen = false;
                }
                break;

            case 'c':
                if (fullscreen) {
                    getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
                    //this.setBounds(0, 0, this.getToolkit().getScreenSize().width / 2, this.getToolkit().getScreenSize().height / 2);
                }
                String color = JOptionPane.showInputDialog("Color input to have");
                if (color == null || color.isEmpty() || color.equals("default")) {
                    colors = default_colors;
                } else {
                    try {
                        colors = new Color[]{(Color) Color.class.getField(color).get(null)};
                    } catch (IllegalAccessException iae) {
                    } catch (NoSuchFieldException nsfe) {
                    }
                }
                if (fullscreen) {
                    setBounds(getGraphicsConfiguration().getBounds());
                    getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
                }
                break;

            case 'n':
                //"Next" don't wait for sleep to finish
                thread.interrupt();
                break;

            case 'a':
                //"add" a new screen
                java.awt.EventQueue.invokeLater(() -> {
                    LightFrame lf = new LightFrame();
                    lf.setTitle("Color Strobe");
                    lf.setLocation(nextLocationX++ * 10, nextLocationY++ * 10);
                    lf.setVisible(true);
                    myFrames.add(lf);
        });
        }
    }//GEN-LAST:event_formKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LightFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /*</editor-fold>*/

        int size = Integer.parseInt(args[0]);

        myFrames = new ArrayList<>();

        colors = default_colors;
        /* Create and display the form */
        for (int i = 0; i < size; i++) {
            java.awt.EventQueue.invokeLater(() -> {
                    LightFrame lf = new LightFrame();
                    lf.setTitle("Color Strobe");
                    lf.setLocation(nextLocationX++ * 10, nextLocationY++ * 10);
                    lf.setVisible(true);
                    myFrames.add(lf);
            });
        }

        thread = Thread.currentThread();
        
        //wait to make sure frames are ready
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {

        }
        process();
    }

    private static void process() {
        timeout = 200;
        Random r = new Random(System.currentTimeMillis());
        boolean black = true;
        do {
            black = !black;
            for (LightFrame lf : myFrames) {
                lf.getContentPane().setBackground(black ? Color.black : colors[Math.abs(r.nextInt()) % colors.length]);
            }

            try {
                Thread.sleep(black ? (long) (timeout * 0.75D) : timeout);
            } catch (InterruptedException ie) {
            }

            if (stop) {
                break;
            }
        } while (true);

        System.exit(0);
    }

    private static void tap() {
        if (lastTap > 0) {
            timeout = System.currentTimeMillis() - lastTap;
            thread.interrupt();
        }
        lastTap = System.currentTimeMillis();
    }

    private static void stop() {
        stop = true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
