package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.AudioStory;
import com.anabivirtual.story.db.Location;
import java.util.Collection;

/**
 *
 * @author pedro
 * @date 10/nov/2018
 */
public class AudioStoryTableModel
  extends javax.swing.table.AbstractTableModel
{
	enum Column {
		ID,
		TITLE,
		FILENAME,
		LOCATION
	};
	final private JDBCDatabase database;
	final private Collection<AudioStory> audioStories;
	final private RandomAccessIterator<AudioStory> rai;
	
	AudioStoryTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.audioStories = database.getAudioStories ();
		this.rai = new RandomAccessIterator<> (this.audioStories);
	}

	@Override
	public int getRowCount ()
	{
		return this.audioStories.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		AudioStory audioStory = this.rai.getValueAt (rowIndex);
		switch (Column.values ()[columnIndex]) {
			case ID:
				return audioStory.ID;
			case TITLE:
				return audioStory.title;
			case FILENAME:
				return audioStory.getFilename ();
			case LOCATION:
				return audioStory.location;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		AudioStory audioStory = this.rai.getValueAt (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case LOCATION:
				if (audioStory.location != (Location) aValue) {
					audioStory.location = (Location) aValue;
					this.database.updateAudioStory (audioStory);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
			case TITLE:
				if (audioStory.title.compareTo ((String) aValue) != 0) {
					audioStory.title = (String) aValue;
					this.database.updateAudioStory (audioStory);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
			case FILENAME:
				if (audioStory.filename.compareTo ((String) aValue) != 0) {
					audioStory.filename = (String) aValue;
					this.database.updateAudioStory (audioStory);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public String getColumnName (int columnIndex)
	{
		return Utilities.getString (Column.values () [columnIndex].name ());
	}

	@Override
	public Class<?> getColumnClass (int columnIndex)
	{
		switch (Column.values ()[columnIndex]) {
			case ID:
				return Long.class;
			case TITLE:
				return String.class;
			case FILENAME:
				return String.class;
			case LOCATION:
				return Location.class;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public boolean isCellEditable (int rowIndex, int columnIndex)
	{
		switch (Column.values () [columnIndex]) {
			case ID:
				return false;
			case TITLE:
			case FILENAME:
			case LOCATION:
				return true;
		}
		throw new Error ("Not reachable");
	}
}
