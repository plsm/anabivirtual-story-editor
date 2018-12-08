package com.anabivirtual.story.db;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

/**
 *
 * @author pedro
 */
public class Location
  implements
  com.anabivirtual.story.core.Location,
  Markable,
  Keyable
{
	private final long ID;
	private double latitude;
	private double longitude;
	private String name;

	public Location (long ID, double latitude, double longitude, String name)
	{
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}

	@Override
	final public long getID ()
	{
		return ID;
	}

	@Override
	final public double getLatitude ()
	{
		return latitude;
	}

	/**
	 * Set the latitude of this location.  Units are degree.
	 *
	 * @param latitude the new latitude of this location.
	 */
	final public void setLatitude (double latitude)
	{
		this.latitude = latitude;
	}

	@Override
	final public double getLongitude ()
	{
		return longitude;
	}

	/**
	 * Set the longitude of this location.  Units are degree.
	 *
	 * @param longitude the new longitude of this location.
	 */
	final public void setLongitude (double longitude)
	{
		this.longitude = longitude;
	}

	@Override
	final public String getName ()
	{
		return name;
	}

	/**
	 * Set the name of this location.
	 *
	 * @param name the new name of this location.
	 */
	final public void setName (String name)
	{
		this.name = name;
	}

	@Override
	public long getKey ()
	{
		return this.ID;
	}

	/**
	 * Compute the marker for this location.
	 */
	@Override
	public Marker computeMarker ()
	{
		LatLong ll = new LatLong (this.latitude, this.longitude);
		MarkerOptions markerOptions = new MarkerOptions ();
		markerOptions
		  .position (ll)
		  .title (this.name)
		  .visible (true)
		;
		return new Marker (markerOptions);
	}

	@Override
	public String toString ()
	{
		return String.format ("(%d, %f, %f, %s)", this.ID, this.latitude, this.longitude, this.name);
	}
}
