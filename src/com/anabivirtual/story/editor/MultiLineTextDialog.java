package com.anabivirtual.story.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * A dialog box to edit multi line text.
 * This is used by a table cell editor.
 * @author pedro
 */
class MultiLineTextDialog
	extends javax.swing.JDialog
{
	/**
	 * Creates a modal dialog to edit multi line text.
	 * Modal dialogs do not go away until the user closes them.
	 * @param parent 
	 */
	public MultiLineTextDialog (java.awt.Frame parent, ActionListener acceptValue)
	{
		super (parent, true);
		ActionListener closeWindow = (ActionEvent e) -> {
			this.setVisible (false);
		};
		initComponents();
		this.okButton.addActionListener (acceptValue);
		this.okButton.addActionListener (closeWindow);
		this.cancelButton.addActionListener (closeWindow);
		//Ensure the TextArea always gets the first focus.
      this.addComponentListener (new ComponentAdapter () {
			@Override
			public void componentShown (ComponentEvent ce)
			{
				MultiLineTextDialog.this.multiLineTextArea.requestFocusInWindow ();
			}
      });
	}
	
	void setMultiLineText (String value)
	{
		this.multiLineTextArea.setText (value);
	}
	
	String getMultiLineText ()
	{
		return this.multiLineTextArea.getText ();
	}

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      controlPanel = new javax.swing.JPanel();
      okButton = new javax.swing.JButton();
      cancelButton = new javax.swing.JButton();
      textScrollPane = new javax.swing.JScrollPane();
      multiLineTextArea = new javax.swing.JTextArea();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

      okButton.setText(Utilities.getString("OK")); // NOI18N
      controlPanel.add(okButton);

      cancelButton.setText(Utilities.getString("Cancel")); // NOI18N
      controlPanel.add(cancelButton);

      getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);

      multiLineTextArea.setColumns(20);
      multiLineTextArea.setRows(5);
      textScrollPane.setViewportView(multiLineTextArea);

      getContentPane().add(textScrollPane, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton cancelButton;
   private javax.swing.JPanel controlPanel;
   private javax.swing.JTextArea multiLineTextArea;
   private javax.swing.JButton okButton;
   private javax.swing.JScrollPane textScrollPane;
   // End of variables declaration//GEN-END:variables

}
