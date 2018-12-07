package com.anabivirtual.story.editor;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author pedro
 * @date 6/dez/2018
 */
public class MultiLineTextEditor
  extends AbstractCellEditor
  implements TableCellEditor
{
	/**
	 * This button is used to display the multi line text and on click to display
	 * the dialog that allows the user to edit the text.
	 */
	private JButton button;
	/**
	 * The multi line text that is going to be edited.
	 */
	private String multiLineText;
	/**
	 * The dialog that is presented to the user.
	 */
	private MultiLineTextDialog dialog;
	/**
	 * Construct a multi line text editor.
	 * @param parent The frame where the dialog is going to be shown.
	 */
   MultiLineTextEditor (Frame parent)
	{
		ActionListener accept = (ActionEvent e) -> {
			this.multiLineText = this.dialog.getMultiLineText ();
		};
		this.dialog = new MultiLineTextDialog (parent, accept);
		button = new JButton ();
		button.addActionListener ((ActionEvent e) -> {
			this.button.setText (this.multiLineText);
			this.dialog.setMultiLineText (this.multiLineText);
			this.dialog.setVisible (true);
			this.fireEditingStopped ();
		});
		button.setBorderPainted (false);

	}

	/**
	 * Return the multi line text that the user edited.
	 * @return the multi line text that the user edited. 
	 */
	@Override
	public Object getCellEditorValue ()
	{
		return this.multiLineText;
	}
	
    public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column)
	 {
        this.multiLineText = (String) value;
        return button;
    }
}
