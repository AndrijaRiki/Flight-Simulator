package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PopupMsg extends Dialog {
    private static final long serialVersionUID = 1L;

    // Constructor with Frame owner
    public PopupMsg(Frame owner, String msg) {
        super(owner, "NOTIFICATION", true);
        init(msg);
    }

    // Constructor with Dialog owner
    public PopupMsg(Dialog owner, String msg) {
        super(owner, "NOTIFICATION", true);
        init(msg);
    }

    // Constructor without owner (Frame null)
    public PopupMsg(String msg) {
        super((Frame) null, "NOTIFICATION", true);
        init(msg);
    }

    // Common initialization logic
    private void init(String msg) {
        setLayout(new BorderLayout(2, 2));
        add(new Label(msg, Label.CENTER), BorderLayout.CENTER);

        Panel buttonsPanel = new Panel();
        Button okBtn = new Button("OK");
        
        okBtn.addActionListener((ie) -> dispose());
        okBtn.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.toUpperCase(e.getKeyChar()) == KeyEvent.VK_ENTER) {
                    dispose();
                }
            }
        });

        buttonsPanel.add(okBtn);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    // Static helper methods for easier calling

    public static void showMsg(Frame owner, String msg) {
        PopupMsg popup = new PopupMsg(owner, msg);
        popup.setVisible(true);
    }

    public static void showMsg(Dialog owner, String msg) {
        PopupMsg popup = new PopupMsg(owner, msg);
        popup.setVisible(true);
    }

    // Version with only message (no owner specified)
    public static void showMsg(String msg) {
        PopupMsg popup = new PopupMsg(msg);
        popup.setVisible(true);
    }
}