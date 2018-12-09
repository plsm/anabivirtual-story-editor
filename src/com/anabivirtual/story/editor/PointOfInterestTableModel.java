package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import com.anabivirtual.story.db.PointOfInterest;
import java.util.Collection;

/**
 * Table model that provides a connection between the {@code point_of_interest}
 * table in the database and the {@code JTable}.
 *
 * @author pedro
 * @date 20/nov/2018
 */
public class PointOfInterestTableModel
	  extends javax.swing.table.AbstractTableModel
{
	/**
	 * The database.
	 */
	final private JDBCDatabase database;
	/**
	 * Collection of {@code PointOfInterest} instances representing all the rows
	 * in the {@code point_of_interest} table.
	 */
	final Collection<PointOfInterest> pointsOfInterest;
	/**
	 * The random access iterator to the {@code PointOfInterest} collection.
	 */
	final private RandomAccessIterator<PointOfInterest> ci;

	/**
	 * Columns in the point_of_interest table.
	 */
	enum Column
	{
		ID,
		TITLE,
		LOCATION,
		HAS_IMAGE,
		IMAGE_FILENAME,
		AUDIO_FILENAME,
		TRANSCRIPTION
	}
	/**
	 * Construct the table model that provides a connection between the
	 * {@code point_of_interest} table in the database and the {@code JTable}.
	 *
	 * @param database The database that is going to be edited.
	 */
	PointOfInterestTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.pointsOfInterest = database.getPointsOfInterest ();
		this.ci = new RandomAccessIterator<> (this.pointsOfInterest);
	}
	@Override
	public int getRowCount ()
	{
		return this.pointsOfInterest.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		PointOfInterest pointOfInterest = this.getPointOfInterest (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return pointOfInterest.getID ();
			case TITLE:
				return pointOfInterest.getTitle ();
			case LOCATION:
				return pointOfInterest.getLocation ();
			case HAS_IMAGE:
				return pointOfInterest.hasImage ();
			case IMAGE_FILENAME:
				return pointOfInterest.getImageFilename ();
			case AUDIO_FILENAME:
				return pointOfInterest.getAudioFilename ();
			case TRANSCRIPTION:
				return pointOfInterest.getTranscription ();
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
			case IMAGE_FILENAME:
			case AUDIO_FILENAME:
			case TRANSCRIPTION:
				return String.class;
			case HAS_IMAGE:
				return Boolean.class;
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
			case HAS_IMAGE:
			case AUDIO_FILENAME:
			case TRANSCRIPTION:
				return true;
			case IMAGE_FILENAME:
				PointOfInterest pointOfInterest = this.getPointOfInterest (rowIndex);
				return pointOfInterest.hasImage ();
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		PointOfInterest aPointOfInterest = this.getPointOfInterest (rowIndex);
		boolean updated = false;
		String asString;
		boolean asBoolean;
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case TITLE:
				asString = (String) aValue;
				if (asString.compareTo (aPointOfInterest.getTitle ()) != 0) {
					aPointOfInterest.setTitle (asString);
					updated = true;
				}
				break;
			case LOCATION:
				if (aPointOfInterest.getLocation () != (Location) aValue) {
					aPointOfInterest.setLocation ((Location) aValue);
					updated = true;
				}
				break ;
			case HAS_IMAGE:
				asBoolean = (Boolean) aValue;
				if (asBoolean != aPointOfInterest.hasImage ()) {
					if (asBoolean)
						aPointOfInterest.enableImage ();
					else
						aPointOfInterest.disableImage ();
					updated = true;
				}
				break ;
			case IMAGE_FILENAME:
				if (
				  aPointOfInterest.getImageFilename () == null ||
				  aPointOfInterest.getImageFilename ().compareTo ((String) aValue) != 0) {
					aPointOfInterest.setImageFilename ((String) aValue);
					updated = true;
				}
				break ;
			case AUDIO_FILENAME:
				asString = (String) aValue;
				if (aPointOfInterest.getAudioFilename ().compareTo (asString) != 0) {
					aPointOfInterest.setAudioFilename (asString);
					updated = true;
				}
				break;
			case TRANSCRIPTION:
				asString = (String) aValue;
				if (aPointOfInterest.getTranscription ().compareTo (asString) != 0) {
					aPointOfInterest.setTranscription (asString);
					updated = true;
				}
				break;
			default:
				throw new Error ("Not reachable");
		}
		if (updated) {
			this.database.updatePointOfInterest (aPointOfInterest);
			this.fireTableCellUpdated (rowIndex, columnIndex);
		}
	}

	PointOfInterest getPointOfInterest (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
