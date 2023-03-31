package com.swiftsync.program.concept;

import com.swiftsync.operations.Backup;
import freshui.io.Printer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    private static void doDirectoryTest(){
        File toCopy = new File("/Users/NL21320/Desktop/ThVir VS1/");
        Backup backupProcessA = new Backup(toCopy, "/Users/NL21320/Desktop/Copied File.zip/",true);
        backupProcessA.enableDebug();
        backupProcessA.run();

        System.out.println("\t\tSTART TIME: \t" + backupProcessA.startTime);
        System.out.println("\t\tEND TIME: \t" + backupProcessA.endTime);
    }

    private static void doSingularTest(){
        File toCopy = new File("/Users/NL21320/Desktop/ThVir VS1/Video/MVI_9950.MP4");
        Backup backupProcessA = new Backup(toCopy, "/Users/NL21320/Desktop/MVI_9950 copy.MP4",true);
        backupProcessA.enableDebug();
        backupProcessA.run();

        System.out.println("\t\tSTART TIME: \t" + backupProcessA.startTime);
        System.out.println("\t\tEND TIME: \t" + backupProcessA.endTime);
    }
    
    private static void createLargeFileA(){
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File("testres/largeFileA"));
            for (int i = 0; i <= 20000000; i++) {
                for (int j = 0; j <= 80; j++) {
                    writer.append("\uDBFF\uDFFD-");
                }
               writer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createComplexFile(){

        final String characters = "abcdefghijklmnopqrstuvwxyz" +
                                  "1234567890" +
                                  "-=[];',./`~!@#$%^&*()_+{}|:<>?" +
                                  "œ∑´´†¥¨ˆˆπåß∂ƒ©˙∆˚¬…æ“‘«Ω≈ç√∫˜˜≤≥ç¡™£¢∞§¶•ªº–≠" +
                                    "⁄€‹›ﬁ°·‚—±Œ„´Á¨·‚Ø∏ÅÎÏÔÒÚÆ¸ıÂ";
        File complexA = new File("/Users/NL21320/Desktop/complexFileA");
        FileWriter writer = null;
        try {
            writer = new FileWriter(complexA);
            writer.append("<swiftsync/\n");
            writer.append("\t<complexfileA/\n");

            int generated = 0;
            int toGenerate = 15550;
            while(generated<=1900000){
                for (int i = 0; i <= toGenerate; i++) {
                    for (int j = 0; j < 750; j++) {
                        int randomChar = (int) Math.floor(Math.random() *(characters.length()-1 - 0 + 1) + 0);
                        writer.append(characters.charAt(randomChar));
                    }
                    writer.append("\n");
                    generated++;
                }
            }

            writer.append("</");
            writer.append("</endfile/\n");

            System.out.println("\tCOMPLEX FILE CREATED at " + complexA.getPath());
            System.out.println("\t\tSIZE: " + complexA.length()/1000000d + " MB");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void backup(File from, String to){
        Backup backup = new Backup(from, to, true);
        backup.enableDebug();
        backup.run();

        Printer.setColor(Printer.YELLOW);
        System.out.println("\t      START TIME: " + backup.startTime);
        System.out.println("\t        END TIME: " + backup.endTime);
        Printer.setColor(Printer.GREEN);
        System.out.println("\tPROCESS DURATION:\t\t" + backup.getDuration().toString());
        Printer.reset();
    }

    public static void main(String[] args) {
        backup(new File("/Users/NL21320/Desktop/complexFileA"),
                                "/Users/NL21320/Desktop/complexFileA copy");
    }

}
