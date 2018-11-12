package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.AbstractStory;
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
	final Collection<AbstractStory> stories;
	final private RandomAccessIterator<AbstractStory> ci;

	enum Column {
		ID,
		TITLE,
		LOCATION
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
		AbstractStory story = this.getStory (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return story.ID;
			case TITLE:
				return story.title;
			case LOCATION:
				return story.location;
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
				return true;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		AbstractStory aStory = this.getStory (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case TITLE:
				if (aStory.title.compareTo ((String) aValue) != 0) {
					aStory.title = (String) aValue;
					this.database.updateStory (aStory);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
			case LOCATION:
				if (aStory.location != (Location) aValue) {
					aStory.location = (Location) aValue;
					this.database.updateStory (aStory);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
		}
		throw new Error ("Not reachable");
	}

	AbstractStory getStory (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
