package com.kooixiuhong.rewritesystem;

import com.kooixiuhong.rewritesystem.app.gui.HomeScreen;
import java.awt.*;

public class RewriteSystemApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HomeScreen frame = new HomeScreen();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
