package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
public class Location
  implements
  com.anabivirtual.story.core.Location,
  Keyable
{
	public final long ID;
	public double latitude;
	public double longitude;
	public String name;

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

	@Override
	final public void setLatitude (double latitude)
	{
		this.latitude = latitude;
	}

	@Override
	final public double getLongitude ()
	{
		return longitude;
	}

	@Override
	final public void setLongitude (double longitude)
	{
		this.longitude = longitude;
	}

	@Override
	final public String getName ()
	{
		return name;
	}

	@Override
	final public void setName (String name)
	{
		this.name = name;
	}

	@Override
	public long getKey ()
	{
		return this.ID;
	}

	@Override
	public String toString ()
	{
		return String.format ("(%d, %f, %f, %s)", this.ID, this.latitude, this.longitude, this.name);
	}
}
