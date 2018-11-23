package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import com.anabivirtual.story.db.Place;
import java.util.Collection;

/**
 * Table model that provides a connection between the {@code place} table in the
 * database and the {@code JTable}.
 *
 * @author pedro
 * @date 20/nov/2018
 */
public class PlaceTableModel
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
	final Collection<Place> places;
	/**
	 * The random access iterator to the {@code Place} collection.
	 */
	final private RandomAccessIterator<Place> ci;

	/**
	 * Columns in the place table.
	 */
	enum Column
	{
		ID,
		LOCATION,
		IMAGE_FILENAME
	}
	/**
	 * Construct the table model that provides a connection between the
	 * {@code place} table in the database and the {@code JTable}.
	 *
	 * @param database The database that is going to be edited.
	 */
	PlaceTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.places = database.getPlaces ();
		this.ci = new RandomAccessIterator<> (this.places);
	}
	@Override
	public int getRowCount ()
	{
		return this.places.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		Place place = this.getPlace (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return place.getID ();
			case LOCATION:
				return place.getLocation ();
			case IMAGE_FILENAME:
				return place.getImageFilename ();
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
			case IMAGE_FILENAME:
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
			case LOCATION:
			case IMAGE_FILENAME:
				return true;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		Place aPlace = this.getPlace (rowIndex);
		boolean updated = false;
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case LOCATION:
				if (aPlace.getLocation () != (Location) aValue) {
					aPlace.setLocation ((Location) aValue);
				}
				break ;
			case IMAGE_FILENAME:
				if (aPlace.getImageFilename ().compareTo ((String) aValue) != 0) {
					aPlace.setImageFilename ((String) aValue);
					updated = true;
				}
				break ;
			default:
				throw new Error ("Not reachable");
		}
		if (updated) {
			this.database.updatePlace (aPlace);
			this.fireTableCellUpdated (rowIndex, columnIndex);
		}
	}

	Place getPlace (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
