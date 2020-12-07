/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ai.synthesis.twophasessa.scriptInterface.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import gui.MouseControllerPanel;
import gui.PhysicalGameStatePanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import rts.GameState;
import util.Pair;


/**
 *
 * @author santi
 */
public class PhysicalGameStateScriptInterfaceJFrame extends JFrame {
    PhysicalGameStatePanel panel = null;
    MouseControllerPanel mousePanel = null;
    
    JFXPanel fxPanel = new JFXPanel();
        
    public PhysicalGameStateScriptInterfaceJFrame(String title, int dx, int dy, PhysicalGameStatePanel a_panel) {
        super(title);
        panel = a_panel;

        panel.setPreferredSize(new Dimension(dx-270, dy));
        getContentPane().removeAll();
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        getContentPane().add(panel);

        fxPanel.setPreferredSize(new Dimension(270, 691));
        getContentPane().add(fxPanel);


        pack();
        setSize(dx,dy);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ///Tathy
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
					initFX(fxPanel);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
       });
        
        
    }
    
    public void recreate(int dx, int dy, PhysicalGameStatePanel a_panel) {
    	getContentPane().getPreferredSize();
    	getContentPane().remove(panel);
    	panel = a_panel;
    	panel.setPreferredSize(new Dimension(dx-270, dy));
    	getContentPane().add(panel, BoxLayout.X_AXIS);
    	setVisible(true);
    }
    
    public PhysicalGameStatePanel getPanel() {
        return panel;
    }
    
    public MouseControllerPanel getMousePanel() {
        return mousePanel;
    }
    
    public void setStateDirect(GameState gs) {
        panel.setStateDirect(gs);
    }
    
    
    public Object getContentAtCoordinates(int x, int y) {
        Insets insets = getInsets();
        x-=insets.left;
        y-=insets.top;
                
        Rectangle r = panel.getBounds();
        if (x>=r.x && x<r.x+r.width &&
            y>=r.y && y<r.y+r.height) {
            Pair<Integer,Integer> cell = panel.getContentAtCoordinates(x - r.x, y - r.y);            
            return cell;
        }
        
        r = mousePanel.getBounds();
        if (x>=r.x && x<r.x+r.width &&
            y>=r.y && y<r.y+r.height) {
            String button = mousePanel.getContentAtCoordinates(x - r.x, y - r.y);
            return button;
        }
        
        return null;
    }
    
    private static void initFX(JFXPanel fxPanel) throws Exception {
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }
    
    private static Scene createScene() throws Exception {
    	Parent root = FXMLLoader.load(PhysicalGameStateScriptInterfaceJFrame.class.getResource("VisualScriptInterface.fxml"));
        Scene  scene = new  Scene(root);

        return (scene);
    }
    
    public static void close() {
    	//dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
    
    
    
        
}