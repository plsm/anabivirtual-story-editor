package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Story;
import com.anabivirtual.story.db.Location;
import java.util.Collection;

/**
 *
 * @author pedro
 * @date 24/out/2018
 */
final public class StoryTableModel
	  extends javax.swing.table.AbstractTableModel
{
	final private JDBCDatabase database;
	final Collection<Story> stories;
	final private RandomAccessIterator<Story> ci;

	enum Column {
		ID,
		TITLE,
		LOCATION,
		AUDIO_FILENAME,
		TRANSCRIPTION
	};
	StoryTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.stories = database.getStories ();
		this.ci = new RandomAccessIterator<> (this.stories);
	}
	@Override
	public int getRowCount ()
	{
		return this.stories.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		Story story = this.getStory (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return story.getID ();
			case TITLE:
				return story.getTitle ();
			case LOCATION:
				return story.getLocation ();
			case AUDIO_FILENAME:
				return story.getAudioFilename ();
			case TRANSCRIPTION:
				return story.getTranscription ();
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
		switch (Column.values () [columnIndex]) {
			case ID:
				return Long.class;
			case TITLE:
			case AUDIO_FILENAME:
			case TRANSCRIPTION:
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
			case LOCATION:
			case AUDIO_FILENAME:
			case TRANSCRIPTION:
				return true;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		Story aStory = this.getStory (rowIndex);
		boolean updated = false;
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case TITLE:
				if (aStory.getTitle ().compareTo ((String) aValue) != 0) {
					aStory.setTitle ((String) aValue);
					updated = true;
				}
				break ;
			case LOCATION:
				if (aStory.getLocation () != (Location) aValue) {
					aStory.setLocation ((Location) aValue);
					updated = true;
				}
				break ;
			case AUDIO_FILENAME:
				if (aStory.getAudioFilename ().compareTo ((String) aValue) != 0) {
					aStory.setAudioFilename ((String) aValue);
					updated = true;
				}
				break ;
			case TRANSCRIPTION:
				if (aStory.getTranscription ().compareTo ((String) aValue) != 0) {
					aStory.setTranscription ((String) aValue);
					updated = true;
				}
				break ;
			default:
				throw new Error ("Not reachable");
		}
		if (updated) {
			this.database.updateStory (aStory);
			this.fireTableCellUpdated (rowIndex, columnIndex);
		}
	}

	Story getStory (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
