package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.BackgroundMusic;
import com.anabivirtual.story.db.JDBCDatabase;
import java.util.Collection;

/**
 * Table model that provides a connection between the {@code background_music}
 * table in the database and the {@code JTable}.
 *
 * @author pedro
 * @date 23/nov/2018
 */
public class BackgroundMusicTableModel
  extends javax.swing.table.AbstractTableModel
{
	/**
	 * The database.
	 */
	final private JDBCDatabase database;
	/**
	 * Collection of {@code Place} instances representing all the rows in the
	 * {@code place} table.
	 */
	final Collection<BackgroundMusic> backgroundMusic;
	/**
	 * The random access iterator to the {@code Place} collection.
	 */
	final private RandomAccessIterator<BackgroundMusic> ci;

	/**
	 * Columns in the place table.
	 */
	enum Column
	{
		ID,
		AUDIO_FILENAME,
		REGION_CENTER_LATITUDE,
		REGION_CENTER_LONGITUDE,
		REGION_RADIUS
	}
	/**
	 * Construct the table model that provides a connection between the
	 * {@code place} table in the database and the {@code JTable}.
	 *
	 * @param database The database that is going to be edited.
	 */
	BackgroundMusicTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.backgroundMusic = database.getBackgroundMusic ();
		this.ci = new RandomAccessIterator<> (this.backgroundMusic);
	}

	@Override
	public int getRowCount ()
	{
		return this.backgroundMusic.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		BackgroundMusic aBackgroundMusic = this.getBackgroundMusic (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return aBackgroundMusic.getID ();
			case AUDIO_FILENAME:
				return aBackgroundMusic.getAudioFilename ();
			case REGION_CENTER_LATITUDE:
				return aBackgroundMusic.getRegionCenterLatitude ();
			case REGION_CENTER_LONGITUDE:
				return aBackgroundMusic.getRegionCenterLongitude ();
			case REGION_RADIUS:
				return aBackgroundMusic.getRegionRadius ();
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
			case AUDIO_FILENAME:
				return String.class;
			case REGION_CENTER_LATITUDE:
			case REGION_CENTER_LONGITUDE:
			case REGION_RADIUS:
				return Double.class;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public boolean isCellEditable (int rowIndex, int columnIndex)
	{
		switch (Column.values () [columnIndex]) {
			case ID:
				return false;
			case AUDIO_FILENAME:
			case REGION_CENTER_LATITUDE:
			case REGION_CENTER_LONGITUDE:
			case REGION_RADIUS:
				return true;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		BackgroundMusic aBackgroundMusic = this.getBackgroundMusic (rowIndex);
		boolean updated = false;
		String asString;
		double asDouble;
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case AUDIO_FILENAME:
				asString = (String) aValue;
				if (aBackgroundMusic.getAudioFilename ().compareTo (asString) != 0) {
					aBackgroundMusic.setAudioFilename (asString);
					updated = true;
				}
				break;
			case REGION_CENTER_LATITUDE:
				asDouble = (Double) aValue;
				if (aBackgroundMusic.getRegionCenterLatitude () != asDouble) {
					aBackgroundMusic.setRegionCenterLatitude (asDouble);
					updated = true;
				}
				break;
			case REGION_CENTER_LONGITUDE:
				asDouble = (Double) aValue;
				if (aBackgroundMusic.getRegionCenterLongitude () != asDouble) {
					aBackgroundMusic.setRegionCenterLongitude (asDouble);
					updated = true;
				}
				break;
			case REGION_RADIUS:
				asDouble = (Double) aValue;
				if (aBackgroundMusic.getRegionRadius () != asDouble) {
					aBackgroundMusic.setRegionRadius (asDouble);
					updated = true;
				}
				break;
			default:
				throw new Error ("Not reachable");
		}
		if (updated) {
			this.database.updateBackgroundMusic (aBackgroundMusic);
			this.fireTableCellUpdated (rowIndex, columnIndex);
		}
	}

	BackgroundMusic getBackgroundMusic (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
