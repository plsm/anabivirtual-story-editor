package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
public class Location
{
	public final long ID;
	public double latitude;
	public double longitude;
	public String name;

	public static final String FIELD_ID = "ID";
	public static final String FIELD_LATITUDE = "latitude";
	public static final String FIELD_LONGITUDE = "longitude";
	public static final String FIELD_NAME = "name";
	
	public Location (long ID, double latitude, double longitude, String name)
	{
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}
	
	@Override
	public String toString ()
	{
		return String.format ("(%d, %f, %f, %s)", this.ID, this.latitude, this.longitude, this.name);
	}
}
