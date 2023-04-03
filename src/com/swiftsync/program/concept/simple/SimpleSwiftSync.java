package com.swiftsync.program.concept.simple;

import com.swiftsync.program.concept.simple.panels.BackupChooseFiles;
import com.swiftsync.program.concept.simple.panels.BackupFinished;
import com.swiftsync.program.concept.simple.panels.BackupProcess;
import com.swiftsync.program.concept.simple.panels.SimpleSwiftSyncPanel;
import freshui.FreshUI;
import freshui.graphics.FButton;
import freshui.gui.Header;
import freshui.io.Printer;
import freshui.util.FColor;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimpleSwiftSync extends FreshUI {

    public static Color dashBtnDefault, dashBtnHover;

    private FButton backupBtn;
    private Header header;

    public static final int     BACKUP_CHOOSE_FILES_PAGE = 101,
                                DASHBOARD = 100,
                                BACKUP_IN_PROGRESS = 102;
    public static final int DASH_BTN_WIDTH = 200, DASH_BTN_HEIGHT = 50;
    public static final int    DASH_BTN_LEFT_PADDING = 100,
                                DASH_BTN_HEADER_PADDING = 50,
                                DASH_BTN_Y_SPACING = 10;
    private SimpleSwiftSyncPanel backupChooseFilesPanel, backupFinishedPanel;
    private BackupProcess backupProcessPanel;

    public SimpleSwiftSync(){
        // instantiate all simple values
        dashBtnDefault = new Color(168, 168, 168);
        dashBtnHover = FColor.darker(dashBtnDefault,0.9);
    }

    public void init(){
        header = new Header(this.getWidth(),"SimpleSwiftSync Dashboard",LEFT,this);

        backupBtn = new FButton("Create Backups",DASH_BTN_WIDTH,DASH_BTN_HEIGHT);
        backupBtn.setColor(dashBtnDefault);
        MouseListener backupMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setPageTo(BACKUP_CHOOSE_FILES_PAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backupBtn.setColor(dashBtnHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backupBtn.setColor(dashBtnDefault);
            }
        };
        backupBtn.addMouseListener(backupMouseAdapter);

        backupChooseFilesPanel = new BackupChooseFiles(this);
        backupChooseFilesPanel.setLabel("Choose File Host/Backup Locations");

        backupProcessPanel = new BackupProcess(this);
        backupProcessPanel.setVisible(false);
        backupFinishedPanel = new BackupFinished(this);

        add(header,0,0);
        add(backupBtn, DASH_BTN_LEFT_PADDING,header.getHeight()+DASH_BTN_HEADER_PADDING);
        add(backupChooseFilesPanel,0,0);

        setPageTo(DASHBOARD);
    }

    public void setPageTo(int page){
        hideAll();
        switch(page){
            case (DASHBOARD) -> {
                header.setVisible(true);
                backupBtn.setVisible(true);
            }

            case(BACKUP_CHOOSE_FILES_PAGE) -> {
                backupChooseFilesPanel.setVisible(true);
            }

            case(BACKUP_IN_PROGRESS) -> {
                backupProcessPanel.setVisible(true);
                backupProcessPanel.run();
            }

            default -> {
                Printer.setColor(Printer.RED);
                Printer.println("\tERROR 404: PAGE NOT FOUND");
                Printer.reset();
            }
        }

    }

    private void hideAll(){
        // dashboard
        header.setVisible(false);
        backupBtn.setVisible(false);

        // backup sub pages
        backupChooseFilesPanel.setVisible(false);
        backupProcessPanel.setVisible(false);
        backupFinishedPanel.setVisible(false);

    }

    public static void main(String[] args) {
        new SimpleSwiftSync().start();
    }
}
