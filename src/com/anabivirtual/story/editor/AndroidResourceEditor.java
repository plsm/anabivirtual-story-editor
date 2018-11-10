package com.anabivirtual.story.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * A cell editor that checks if the string is a valid android resource filename.
 * @author pedro
 * @date 10/nov/2018
 */
class AndroidResourceEditor
  extends javax.swing.DefaultCellEditor
{
	final JTextField textField;

	AndroidResourceEditor (JTable table)
	{
		super (new JTextField ());
		this.textField = (JTextField) this.editorComponent;
		this.textField.setBorder (
		  BorderFactory.createLineBorder (table.getGridColor (), 2));
		this.textField.getInputMap ().put (
		  KeyStroke.getKeyStroke (KeyEvent.VK_ENTER, 0),
		  "check");
		this.textField.getActionMap ().put ("check", new Check ());
	}

	private class Check
	  extends AbstractAction
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			if (!isValidAndroidResourceFilename ()) {
				if (useAlternativeAndroidResourceFilename ()) {
					textField.postActionEvent ();
				}
			}
			else {
				textField.postActionEvent ();
			}
		}
	}

	/**
	 * Checks if the cell value is a valid android resource filename. If the
	 * value is invalid, the textfield remains visible so that the user fixes the
	 * value.
	 *
	 * @return {@code true} is the cell value
	 */
	@Override
	public boolean stopCellEditing ()
	{
		String value = this.textField.getText ();
		if (!Utilities.isValidAndroidResourceFilename (value)) {
			JOptionPane.showMessageDialog (
			  null,
			  String.format (
				 Utilities.getString ("InvalidAndroidResourceFilename"),
				 value),
			  Utilities.getString ("WarningTitle"),
			  JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return super.stopCellEditing ();
	}

	private boolean isValidAndroidResourceFilename ()
	{
		String value = this.textField.getText ();
		return Utilities.isValidAndroidResourceFilename (value);
	}

	/**
	 * Asks the user if he wants to use the proposed android resource filename.
	 * @return {@code true} if the user answered yes.
	 * @see Utilities#convertToValidAndroidResourceFilename(java.lang.String)
	 */
	private boolean useAlternativeAndroidResourceFilename ()
	{
		String value = this.textField.getText ();
		String proposed = Utilities.convertToValidAndroidResourceFilename (value);
		int answer = JOptionPane.showConfirmDialog (
		  null,
		  String.format (
		    Utilities.getString ("InvalidAndroidResourceFilenameUseInstead"),
		    value,
		    proposed),
		  Utilities.getString ("WarningTitle"),
		  JOptionPane.YES_NO_OPTION,
		  JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.YES_OPTION) {
			this.textField.setText (proposed);
			return true;
		}
		else {
			return false;
		}
	}
}
