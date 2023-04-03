package com.swiftsync.program.concept.simple.panels;

import com.swiftsync.operations.Backup;
import com.swiftsync.program.concept.simple.SimpleSwiftSync;
import com.swiftsync.ui.process.BackupProcessLink;
import freshui.io.Printer;

import javax.swing.*;
import java.io.File;

public class BackupProcess extends SimpleSwiftSyncPanel {

    public static File parent = null, destination = null;
    public static boolean doProgressShare = false, doDebug = false;
    private static Backup process;
    private BackupProcessLink processLink;
    private JProgressBar progBar;

    public BackupProcess(SimpleSwiftSync parent){
        super(parent);

        this.setLabel("Backing Up...");

        progBar = new JProgressBar(0,100);
        add(progBar, 50,50);
    }

    public void run(){
        this.setVisible(true);
        Printer.println("\tBackupProcess run method started",Printer.GREEN);
        Printer.println("\t\t>> BackupProcess FPanel visibility bool: " + this.isVisible(), Printer.GREEN_BACKGROUND);
        processLink = new BackupProcessLink(){

            @Override
            public void initializingBackup() {
                Printer.println("\tINITIALIZING BACKUP");
            }

            @Override
            public void startedBackup() {
                Printer.println("\tSTARTED BACKUP");
            }

            @Override
            public void byteChunkTransferred() {
                Printer.println("\tTRANSFERRING DATA... [" + process.getProgress() + "%]");
                progBar.setValue((int) process.getProgress());
            }

            @Override
            public void initializingVerification() {
                Printer.println("\tINITIALIZING VERIFICATION");
            }

            @Override
            public void verificationStarted() {
                Printer.println("\tVERIFICATION STARTED");
            }

            @Override
            public void finishingProcess() {
                Printer.println("\tFINISHING PROCESS");
            }

            @Override
            public void initializingError() {
                Printer.println("\tINITIALIZING ERROR");
            }

            @Override
            public void backupError() {
                Printer.println("\tBACKUP ERROR");
            }

            @Override
            public void initializingVerificationError() {
                Printer.println("\tINITIALIZING VERIFICATION ERROR");
            }

            @Override
            public void unknownErrorOccurred() {
                Printer.println("\tAN UNKNOWN ERROR OCCURRED");
            }
        };
        process = new Backup(parent,destination.getAbsolutePath(),processLink,doProgressShare);
        if(doDebug) process.enableDebug();
        process.run();

    }

    @Override
    public void buttonClicked(){
        boolean isUserSure = true;

        if(isUserSure){
            super.buttonClicked();
        }
    }

}
