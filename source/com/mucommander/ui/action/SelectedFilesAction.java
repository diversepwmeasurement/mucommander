package com.mucommander.ui.action;

import com.mucommander.ui.MainFrame;
import com.mucommander.ui.table.FileTable;
import com.mucommander.ui.table.FileTableModel;
import com.mucommander.file.AbstractFile;

import javax.swing.*;

/**
 * SelectedFilesAction is an abstract action that operates on the currently active FileTable, when at least one file is
 * marked, or when a file other than the parent folder file '..' is selected.
 * When none of those conditions is satisfied, this action is disabled.
 *
 * <p>Optionally, a FileFilter can be specified using {@link #setFileFilter(com.mucommander.file.filter.FileFilter) setFileFilter}
 * to further restrict the enable condition to files that match the filter.
 *
 * @author Maxence Bernard
 */
public abstract class SelectedFilesAction extends FileAction {

    public SelectedFilesAction(MainFrame mainFrame) {
        super(mainFrame);
    }

    public SelectedFilesAction(MainFrame mainFrame, String labelKey) {
        super(mainFrame, labelKey);
    }

    public SelectedFilesAction(MainFrame mainFrame, String labelKey, KeyStroke accelerator) {
        super(mainFrame, labelKey, accelerator);
    }

    public SelectedFilesAction(MainFrame mainFrame, String labelKey, KeyStroke accelerator, String toolTipKey) {
        super(mainFrame, labelKey, accelerator, toolTipKey);
    }

    protected boolean getFileTableCondition(FileTable fileTable, AbstractFile selectedFile) {
        return selectedFile!=null || ((FileTableModel)fileTable.getModel()).getNbMarkedFiles()>0;
    }
}
