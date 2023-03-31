package com.swiftsync.ui;

import com.swiftsync.operations.Backup;
import com.swiftsync.operations.DefaultDirectories;
import com.swiftsync.ui.process.ProcessLink;
import freshui.FreshUI;
import freshui.gui.input.Input;
import freshui.io.Printer;
import svu.csc213.Dialog;

import java.io.File;

public class BasicBackupProgram extends FreshUI {

    public BasicBackupProgram(){
        String to, from;

        from = Dialog.getString("Copy From:");
        if(from.equals("complex")){
            from = DefaultDirectories.USER_DESKTOP + "/complexFileA";
            to = DefaultDirectories.USER_DESKTOP;
        } else {
            to = Dialog.getString("Copy To:");
        }

        Printer.setColor(Printer.BLUE);
        Printer.println("Copy From:\t"+from);
        Printer.println("Copy To:\t"+to);
        Printer.reset();

        Backup backupOperation = new Backup(new File(from),to,true);
        backupOperation.setDebug(true);

        backupOperation.run();
        Printer.println("\tSTATUS CODE: " + backupOperation.getOverallStatus());
        this.exit();
    }

    public static void main(String[] args) {
        new BasicBackupProgram().start();
    }

}
