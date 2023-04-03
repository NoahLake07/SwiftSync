package com.swiftsync.program.concept.simple.panels;

import com.swiftsync.program.concept.simple.SimpleSwiftSync;
import freshui.graphics.FButton;
import freshui.graphics.FPanel;
import freshui.gui.Toggle;
import freshui.gui.input.Input;
import freshui.io.Printer;
import freshui.program.FreshProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import static com.swiftsync.program.concept.simple.SimpleSwiftSync.BACKUP_IN_PROGRESS;

public class BackupChooseFiles extends SimpleSwiftSyncPanel {

    File from, to;

    public BackupChooseFiles(SimpleSwiftSync parent){
        super(parent);

        this.setVisible(false);
        Input fromLoc = new Input("From:",parent);
        Input toLoc = new Input("To:",parent);
        fromLoc.setWidth(400);
        toLoc.setWidth(400);
        add(fromLoc, 30,70);
        add(toLoc, 30, 120);

        FButton chooseFromLoc = new FButton("Choose");
        chooseFromLoc.setSize(100,fromLoc.getHeight());
        MouseListener chooseLoc = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(null);
                if(option == JFileChooser.APPROVE_OPTION){
                    from = fileChooser.getSelectedFile();
                }
                fromLoc.setInputText(from.getAbsolutePath());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                chooseFromLoc.setColor(SimpleSwiftSync.dashBtnHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chooseFromLoc.setColor(SimpleSwiftSync.dashBtnDefault);
            }
        };
        chooseFromLoc.addMouseListener(chooseLoc);
        add(chooseFromLoc, 450, 70);

        FButton chooseToLoc = new FButton("Choose");
        chooseToLoc.setSize(100,fromLoc.getHeight());
        MouseListener chooseLoc2 = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if(option == JFileChooser.APPROVE_OPTION){
                    to = fileChooser.getSelectedFile();
                }
                toLoc.setInputText(to.getAbsolutePath());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                chooseToLoc.setColor(SimpleSwiftSync.dashBtnHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chooseToLoc.setColor(SimpleSwiftSync.dashBtnDefault);
            }
        };
        chooseToLoc.addMouseListener(chooseLoc2);
        add(chooseToLoc, 450, 120);

        JLabel doProgressShareLabel = new JLabel("Enable Progress Share?");
        Toggle doProgressShare = new Toggle(true);
        this.add(doProgressShareLabel,30,180);
        this.add(doProgressShare, 200, 180);

        JLabel doDebugLogsLabel = new JLabel("Enable Debug Logging?");
        Toggle doDebugLogs = new Toggle(true);
        doDebugLogs.setColorA(new Color(232, 134, 5));
        this.add(doDebugLogsLabel,30,230);
        this.add(doDebugLogs, 200, 230);

        FButton startBtn = new FButton("START BACKUP PROCESS", 500,70);
        MouseListener startProcessMSL = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(doDebugLogs.getStatus()){
                    Printer.println("Process Started...");
                }

                // forward process to back up screen
                BackupProcess.parent = from;
                BackupProcess.destination = to;
                BackupProcess.doProgressShare = doProgressShare.getStatus();
                BackupProcess.doDebug = doDebugLogs.getStatus();
                parent.setPageTo(BACKUP_IN_PROGRESS); // parent main will auto start backup process

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startBtn.setColor(SimpleSwiftSync.dashBtnHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startBtn.setColor(SimpleSwiftSync.dashBtnDefault);
            }
        };
        startBtn.addMouseListener(startProcessMSL);
        add(startBtn,30,400);

    }

}
