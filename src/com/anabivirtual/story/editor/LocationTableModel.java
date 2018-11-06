package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import java.util.Collection;

/**
 *
 * @author pedro
 * @date 24/out/2018
 */
final public class LocationTableModel
	  extends javax.swing.table.AbstractTableModel
{
	final private JDBCDatabase database;
	final Collection<Location> locations;
	final private RandomAccessIterator<Location> ci;

	enum Column {
		ID,
		LATITUDE,
		LONGITUDE,
		NAME
	};
	LocationTableModel (JDBCDatabase database)
	{
		this.database = database;
		this.locations = database.getLocations ();
		this.ci = new RandomAccessIterator<> (this.locations);
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
		Location location = this.getLocation (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return location.ID;
			case LATITUDE:
				return location.latitude;
			case LONGITUDE:
				return location.longitude;
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
				return Double.class;
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
		Location l = this.getLocation (rowIndex);
		switch (Column.values () [columnIndex]) {
			case ID:
				return ;
			case LATITUDE:
				if (l.latitude != (Double) aValue) {
					l.latitude = (Double) aValue;
					this.database.updateLocation (l);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
			case LONGITUDE:
				if (l.longitude != (Double) aValue) {
					l.longitude = (Double) aValue;
					this.database.updateLocation (l);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
			case NAME:
				if (l.name.compareTo ((String) aValue) != 0) {
					l.name = (String) aValue;
					this.database.updateLocation (l);
					this.fireTableCellUpdated (rowIndex, columnIndex);
				}
				return ;
		}
		throw new Error ("Not reachable");
	}

	Location getLocation (int rowIndex)
	{
		return this.ci.getValueAt (rowIndex);
	}
}
