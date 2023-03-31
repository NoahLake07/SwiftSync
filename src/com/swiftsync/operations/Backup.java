package com.swiftsync.operations;

import com.swiftsync.data.ProcessDuration;
import com.swiftsync.ui.process.ProcessLink;
import freshui.io.Printer;

import java.io.*;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Backup implements Runnable {

    // * com.swiftsync.program.SwiftSync UI linking parent
    private ProcessLink myDriver = null;

    // * background variables
    private File parentFile;
    private String backupLocation;
    private boolean progressShare;
    /**
     * Contains a boolean status that the process will check for at the end of a backup. If the value is set to true,
     * then the process will double beck the backup by performing several checks.
     */
    private boolean autoVerifyBackup = true;
    private boolean debug = false;
    /**
     * Contains data for the current backup status. This data is separate from the overall progress, and only represents
     * the data for the backup.
     */
    private int backupStatus;
    /**
     * Contains data pertaining to the overall progress/status of the backup process.
     */
    private int overallStatus;
    private double fileTolerance = 0.0002;

    // * tracking variables
    public double taskProgress = 0.00;
    public LocalDateTime startTime, endTime;

    // region Constructors

    public Backup(){
        this(null,null,null,false);
    }

    public Backup(File parent, String copyTo){
        this(parent,copyTo,null,false);
    }

    public Backup(File parent, String copyTo, boolean enableProgressShare){
        this(parent,copyTo,null,enableProgressShare);
    }

    public Backup(File parent, String copyTo, ProcessLink process, boolean enableProgressShare){
        this.parentFile = parent;
        this.backupLocation = copyTo;
        this.progressShare = enableProgressShare;
        this.myDriver = process;
    }

    // endregion

    // region Mutator Methods

    public boolean getDebug(){
        return debug;
    }

    public void setDebug(boolean b){
        debug = b;
    }

    public void enableDebug(){
        this.setDebug(true);
    }

    public void disableDebug(){
        this.setDebug(false);
    }

    public void setProcess(ProcessLink process){
        this.myDriver = process;
    }

    public int getOverallStatus(){
        return overallStatus;
    }

    // endregion

    // region Time Recognition Methods
    public ProcessDuration getDuration(){
        if(startTime == null || endTime == null){
            return null;
        } else {
            ProcessDuration duration = new ProcessDuration();


            return duration;
        }
    }
    // endregion

    // region RUN METHODS

    @Override
    public void run() {
        if(verifyParent()){
            this.overallStatus = Status.INITIALIZING;
            if(debug) System.out.println("\t<debug/ PARENT IS FILE");
            try { this.backup(); } catch (IOException e) {
                this.overallStatus = Status.INITIALIZING_ERROR;
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (parentFile.isDirectory()) {
            this.overallStatus = Status.INITIALIZING;
            if(debug) System.out.println("\t<debug/ PARENT IS DIRECTORY");
            try { this.directoryBackup(); } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            this.overallStatus = Status.INITIALIZING_ERROR;
            throw new Error("Specified Parent File Does Not Exist.");
        }
    }

    private void backup() throws IOException {
        // instantiate trackers
        taskProgress = 0.01;
        int completedOperations = 0;
        this.startTime = LocalDateTime.now();

        // find possible chunks
        int predictedOperations = (int) (parentFile.length()/1024);
        if(debug) System.out.println("\t PREDICTED 1024BYTE OPERATIONS: " + predictedOperations);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(parentFile);
            os = new FileOutputStream(backupLocation);
            byte[] buffer = new byte[1024];
            int length;
            this.overallStatus = Status.BACKUP_IN_PROGRESS;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
                updateProgress(predictedOperations, ++completedOperations);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            is.close();
            os.close();
        }

        backupStatus = verifyBackup();

        // end time set
        this.endTime = LocalDateTime.now();
    }

    private void directoryBackup() throws IOException {
        // instantiate trackers
        taskProgress = 0.01;
        int completedOperations = 0;
        this.startTime = LocalDateTime.now();

        // find possible chunks
        int predictedOperations = (Util.directorySize(parentFile)/1024);
        if(debug) System.out.println("\t PARENT FILE LENGTH: " + Util.directorySize(parentFile));
        if(debug) System.out.println("\t PREDICTED 1024 BYTE OPERATIONS: " + predictedOperations);

        FileOutputStream fos = new FileOutputStream(backupLocation);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        this.overallStatus = Status.BACKUP_IN_PROGRESS;
        zipFile(parentFile, parentFile.getName(), zipOut,completedOperations,predictedOperations);
        zipOut.close();
        fos.close();

        backupStatus = verifyBackup();

        // end time set
        this.endTime = LocalDateTime.now();
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut, int cO, int pO) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut,cO,pO);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
            updateProgress(pO,++cO);
        }
        fis.close();
    }


    private void updateProgress(double pO, double cO){
        taskProgress = Math.round(cO/pO*100);
        if(debug){
            //System.out.println("\t<debug: CURRENT TASK PROGRESS: " + taskProgress + "%");
        }
    }

    // endregion

    // region Verification Methods
    private boolean verifyParent(){
        return parentFile.exists() && parentFile.isFile();
    }

    private int verifyBackup(){
        this.overallStatus = Status.PREPARING_VERIFICATION;

        // fetch sizes of original file and new file
        double originalSize = parentFile.length();
        double newSize = new File(backupLocation).length();
        if(debug) {
            Printer.setColor(Printer.BLUE);
            Printer.println("Parent File Size (Bytes):" + originalSize);
            Printer.println("Backup File Size (Bytes):" + newSize);
            Printer.reset();
        }

        // check for file size difference


        return 0;
    }

    // endregion

    // region Utility
    private class Util {

        /**
         * Recursively finds and sums all sizes of the files found within
         * @param directory a valid directory
         * @returns the size (in bytes) how large the directories contacts are, or -1 if the file specified is not a directory
         */
        static int directorySize(File directory){
            if(!directory.isDirectory()) return -1;

            int bytesFound = 0;

            File[] contents = directory.listFiles();
            for (int i = 0; i < contents.length-1; i++) {
                if(contents[i].isDirectory()){
                    bytesFound += directoryContentsSize(contents[i]);
                } else {
                    bytesFound += contents[i].length();
                }
            }

            return bytesFound;
        }

        private static int directoryContentsSize(File subdirectory){
            int bytesFound = 0;

            File[] contents = subdirectory.listFiles();
            for (int i = 0; i < contents.length-1; i++) {
                if(contents[i].isDirectory()){
                    bytesFound += directoryContentsSize(contents[i]);
                } else {
                    bytesFound += contents[i].length();
                }
            }

            return bytesFound;
        }
    }
    // endregion

    // region Constants

    public static final class Status {

        // * Safe Backup Processes
        public final static int NOT_STARTED = 0;
        public final static int INITIALIZING = 1;
        public final static int BACKUP_IN_PROGRESS = 2;
        public final static int PREPARING_VERIFICATION = 3;
        public final static int VERIFYING_BACKUP = 4;
        public final static int WRAPPING_UP = 5;
        public final static int DONE = 6;

        // * Error Backup Codes
        public final static int INITIALIZING_ERROR = -100;
        public final static int BACKUP_ERROR = -101;
        public final static int INITIALIZING_VERIFY_ERROR = -102;
        public final static int UNKNOWN_ERROR = -103;

    }

    // endregion
}
