package com.mucommander.ui.action;

import com.mucommander.ui.MainFrame;
import com.mucommander.ui.FolderPanel;
import com.mucommander.ui.table.FileTable;
import com.mucommander.event.TableSelectionListener;
import com.mucommander.event.TableChangeListener;
import com.mucommander.file.AbstractFile;
import com.mucommander.file.filter.FileFilter;

import javax.swing.*;

/**
 * FileAction is an abstract action that operates on the currently active FileTable. It is enabled only when
 * the table condition as tested by {@link #getFileTableCondition(FileTable, AbstractFile) getFileTableCondition()}
 * method is satisfied.
 *
 * <p>Optionally, a FileFilter can be specified using {@link #setFileFilter(com.mucommander.file.filter.FileFilter) setFileFilter}
 * to further restrict the enable condition to files that match the filter.
 *
 * <p>Those tests are performed each time the selected file on the currently active FileTable changes, and
 * each time the currently active FileTable changes. Thus, this action's enabled status always reflects the current
 * state of the active FileTable. 
 *
 * @author Maxence Bernard
 */
public abstract class FileAction extends MucoAction implements TableSelectionListener, TableChangeListener {

    /** Filter that restricts the enable condition to files that match it (can be null) */
    protected FileFilter filter;


    public FileAction(MainFrame mainFrame) {
        super(mainFrame);
        addListeners(mainFrame);
    }

    public FileAction(MainFrame mainFrame, String labelKey) {
        super(mainFrame, labelKey);
        addListeners(mainFrame);
    }

    public FileAction(MainFrame mainFrame, String labelKey, KeyStroke accelerator) {
        super(mainFrame, labelKey, accelerator);
        addListeners(mainFrame);
    }

    public FileAction(MainFrame mainFrame, String labelKey, KeyStroke accelerator, String toolTipKey) {
        super(mainFrame, labelKey, accelerator, toolTipKey);
        addListeners(mainFrame);
    }


    private void addListeners(MainFrame mainFrame) {
        mainFrame.addTableChangeListener(this);
        mainFrame.getFolderPanel1().getFileTable().addTableSelectionListener(this);
        mainFrame.getFolderPanel2().getFileTable().addTableSelectionListener(this);
    }


    /**
     * Restricts the enable condition to files that match the specified filter.
     *
     * @param filter FileFilter instance
     */
    public void setFileFilter(FileFilter filter) {
        this.filter = filter;
    }


    /**
     * Enables/disables this action if both of the {@link #getFileTableCondition(FileTable, AbstractFile) getFileTableCondition()}
     * and filter (if there is one) tests are satisfied.
     *
     * <p>This method is called each time the selected file on the currently active FileTable changes, and
     * each time the currently active FileTable changes.
     *
     * @param fileTable currently active FileTable
     * @param selectedFile new currently selected file in current FileTable
     */
    protected void updateEnabledState(FileTable fileTable, AbstractFile selectedFile) {
        boolean enabled = getFileTableCondition(fileTable, selectedFile);

        // Test the selected file (if any) against the filter (if any)
        if(enabled && filter!=null)
            enabled = filter.accept(selectedFile);

        // Note: AbstractAction checks if enabled value has changed before firing an event
        setEnabled(enabled);
    }


    /**
     *
     *
     * <p>This method is called each time the selected file on the currently active FileTable changes, and
     * each time the currently active FileTable changes.
     *
     * @param fileTable currently active FileTable
     * @param selectedFile new currently selected file in current FileTable
     */
    protected abstract boolean getFileTableCondition(FileTable fileTable, AbstractFile selectedFile);


    ///////////////////////////////////////////
    // TableSelectionListener implementation //
    ///////////////////////////////////////////

    /**
     * Enables/disables this action based on the given new selected file: if it is <code>null</code>
     * (parent folder '..' selected), the action will be disabled, if not it will be disabled.
     */
    public void selectionChanged(FileTable source, AbstractFile selectedFile) {
        updateEnabledState(source, selectedFile);
    }


    ////////////////////////////////////////
    // TableChangeListener implementation //
    ////////////////////////////////////////

    public void tableChanged(FolderPanel folderPanel) {
        FileTable fileTable = folderPanel.getFileTable();
        updateEnabledState(fileTable, fileTable.getSelectedFile(false));
    }
}
