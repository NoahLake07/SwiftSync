package com.swiftsync.ui.process;

/**
 * A Process is an object that links com.swiftsync.program.SwiftSync UI components to the process in which the application is running.
 */
public interface BackupProcessLink {

    //TODO implement methods into the Backup Process

    // * PROGRESS METHODS
    public void initializingBackup();
    public void startedBackup();
    public void byteChunkTransferred();
    public void initializingVerification();
    public void verificationStarted();
    public void finishingProcess();

    // * ERROR METHODS
    public void initializingError();
    public void backupError();
    public void initializingVerificationError();
    public void unknownErrorOccurred();

}
