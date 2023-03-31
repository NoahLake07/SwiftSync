package com.swiftsync.program.concept.simple.panels;

import com.swiftsync.program.concept.simple.SimpleSwiftSync;
import freshui.graphics.FButton;
import freshui.graphics.FPanel;
import freshui.program.FreshProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimpleSwiftSyncPanel extends FPanel {

    private FButton returnToHomeBtn = new FButton("Return to Home",100,20);
    private JLabel pageLabel = new JLabel("Untitled SimpleSwiftSync Page");
    private int pageLabelPadding = 30;

    public SimpleSwiftSyncPanel(SimpleSwiftSync parent){
        this.setProgramParent(parent);

        this.setWidth(parent.getWidth());
        this.setHeight(parent.getHeight());

        this.add(returnToHomeBtn,30,30);
        MouseListener returnToHomeAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parent.setPageTo(SimpleSwiftSync.DASHBOARD);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                returnToHomeBtn.setColor(SimpleSwiftSync.dashBtnHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnToHomeBtn.setColor(SimpleSwiftSync.dashBtnDefault);
            }
        };
        returnToHomeBtn.addMouseListener(returnToHomeAdapter);

        pageLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
        this.add(pageLabel, (int) (returnToHomeBtn.getX()+returnToHomeBtn.getWidth() + pageLabelPadding*2), 30);

    }

    @Override
    public void setVisible(boolean b){
        super.setVisible(b);
        returnToHomeBtn.setVisible(b);
    }

    public void setLabel(String label){
        this.pageLabel.setText(label);
    }

    public String getLabel(){
        return pageLabel.getText();
    }

}
