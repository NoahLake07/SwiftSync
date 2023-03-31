package com.swiftsync.program;

import acm.graphics.GImage;
import com.swiftsync.Pages;
import com.swiftsync.ui.panels.*;
import freshui.FreshUI;
import freshui.util.Resizer;

import javax.swing.*;
import java.awt.*;

public class SwiftSync extends FreshUI implements Pages {

    int page = DASHBOARD;
    private final double BUTTON_Y_RELATIVE_BASELINE = 0.35;
    private final Color DASH_LIGHT_BACKGROUND = new Color(171, 171, 171);

    GImage backupBtn, compareBtn, formatBtn;
    JLabel dashText, subDashText;

    Dashboard dashboard = new Dashboard();

    public void init() {
        this.setProgramName("com.swiftsync.program.SwiftSync");
        this.setSize(650, 525);
        this.setBackground(DASH_LIGHT_BACKGROUND);

        initializeDashboard();

        add(dashboard, 0, 0);

        startResize();
    }

    private void initializeDashboard() {

        /* LOGO
        logo = new GImage("res/com.swiftsync.program.SwiftSync Logo B3-c (150ppi).png");
        logo.scale(0.5);
        this.add(logo, 10, 10);
        */

        //* DASHBOARD TEXT
        dashText = new JLabel("Dashboard");
        subDashText = new JLabel("Select an operation");
        dashText.setFont(new Font("Sans Serif",Font.BOLD,25));
        add(dashText,0,0);
        dashText.setLocation(this.getWidth()/2-dashText.getWidth()/2,this.getHeight()/9);

        double buttonScaleFactor = 0.14;


        int padding = this.getWidth()/10;
        double buttonYPosition = this.getHeight()*BUTTON_Y_RELATIVE_BASELINE;

        //* CREATE BACKUPS BUTTON
        backupBtn = new GImage("res/Create Backups Icon.png/");
        backupBtn.scale(buttonScaleFactor);
        add(backupBtn,0,0);
        backupBtn.setLocation(padding,buttonYPosition);


        //* COMPARE FILES BUTTON
        compareBtn = new GImage("res/Compare Files Icon.png/");
        compareBtn.scale(buttonScaleFactor);
        add(compareBtn,0,0);
        compareBtn.setLocation(this.getWidth()/2-compareBtn.getWidth()/2,buttonYPosition);

        //* FORMAT BUTTON
        formatBtn = new GImage("res/Format Icon.png/");
        formatBtn.scale(buttonScaleFactor);
        add(formatBtn,0,0);
        formatBtn.setLocation(this.getWidth()-formatBtn.getWidth()-padding,buttonYPosition);

    }

        public void updatePages () {
            switch (page) {
                case DASHBOARD -> {
                    backupBtn.setVisible(true);
                    compareBtn.setVisible(true);
                    formatBtn.setVisible(true);
                    dashText.setVisible(true);
                }
            }
        }

        public void setPage(int p){
            page = p;
            updatePages();
        }

        private void startResize(){
            Resizer resizeA = new Resizer(this);
        }

        public static void main (String[]args){
            new SwiftSync().start();
        }
}