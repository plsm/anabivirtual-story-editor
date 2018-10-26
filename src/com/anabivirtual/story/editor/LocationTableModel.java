package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.Database;
import com.anabivirtual.story.db.Location;
import java.util.Vector;

/**
 *
 * @author pedro
 * @date 24/out/2018
 */
final public class LocationTableModel
	  extends javax.swing.table.AbstractTableModel
{
	final private Database database;
	final private Vector<Location> locations;

	enum Column {
		ID,
		LATITUDE,
		LONGITUDE,
		NAME
	};
	LocationTableModel (Database database)
	{
		this.database = database;
		this.locations = new Vector<> (database.getLocations ());
	}
	@Override
	public int getRowCount ()
	{
		return this.locations.size ();
	}

	@Override
	public int getColumnCount ()
	{
		return Column.values ().length;
	}

	@Override
	public Object getValueAt (int rowIndex, int columnIndex)
	{
		Location location = this.locations.elementAt (rowIndex);
		int seconds, minutes, degrees;
		char side;
		switch (Column.values () [columnIndex]) {
			case ID:
				return location.ID;
			case LATITUDE:
				seconds = (int) Math.abs (Math.round (location.latitude * 3600));
				minutes = (seconds / 60) % 60;
				degrees = seconds / 3600;
				seconds = seconds % 60;
				side = location.latitude < 0 ? 'N' : 'S';
				return String.format ("%2d° %2d' %2d'' %c", degrees, minutes, seconds, side);
			case LONGITUDE:
				seconds = (int) Math.abs (Math.round (location.longitude * 3600));
				minutes = (seconds / 60) % 60;
				degrees = seconds / 3600;
				seconds = seconds % 60;
				side = location.longitude < 0 ? 'W' : 'E';
				return String.format ("%2d° %2d' %2d'' %c", degrees, minutes, seconds, side);
			case NAME:
				return location.name;
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
				return Integer.class;
			case LATITUDE:
			case LONGITUDE:
			case NAME:
				return String.class;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public boolean isCellEditable (int rowIndex, int columnIndex)
	{
		switch (Column.values () [columnIndex]) {
			case ID:
				return false;
			case LATITUDE:
			case LONGITUDE:
			case NAME:
				return true;
		}
		throw new Error ("Not reachable");
	}

	@Override
	public void setValueAt (Object aValue, int rowIndex, int columnIndex)
	{
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
