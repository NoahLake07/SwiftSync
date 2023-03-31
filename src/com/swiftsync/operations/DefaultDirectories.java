package com.swiftsync.operations;

import javax.swing.filechooser.FileSystemView;

public class DefaultDirectories {

    public final static String USER_DESKTOP = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Desktop";

}
