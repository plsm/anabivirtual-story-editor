package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import java.util.Collection;

/**
 * Model used in combo boxes that display locations available in the database.
 * @author pedro
 */
class LocationComboBoxModel
  extends javax.swing.AbstractListModel<Location>
  implements javax.swing.ComboBoxModel<Location>
{
	final JDBCDatabase database;
	final Collection<Location> locations;
	final private RandomAccessIterator<Location> ci;
	private Location selectedLocation;
	LocationComboBoxModel (JDBCDatabase database)
	{
		this.database = database;
		this.locations = database.getLocations ();
		this.ci = new RandomAccessIterator<> (this.locations);
		this.selectedLocation = null;
	}

	@Override
	public int getSize ()
	{
		return this.locations.size ();
	}

	@Override
	public Location getElementAt (int index)
	{
		return this.ci.getValueAt (index);
	}

	@Override
	public void setSelectedItem (Object anItem)
	{
		for (Location l : this.locations) {
			if (anItem == l) {
				this.selectedLocation = l;
				return ;
			}
		}
	}

	@Override
	public Object getSelectedItem ()
	{
		return this.selectedLocation;
	}
}
