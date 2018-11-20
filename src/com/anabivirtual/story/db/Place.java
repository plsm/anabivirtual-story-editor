package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 * @date 20/nov/2018
 */
final public class Place
  implements
  com.anabivirtual.story.core.Place<Location>,
  Keyable
{
	private final long ID;
	private Location location;
	private String imageFilename;
	
	Place (long ID, Location location, String filename)
	{
		this.ID = ID;
		this.location = location;
		this.imageFilename = filename;
	}

	@Override
	public long getID ()
	{
		return this.ID;
	}

	@Override
	public Location getLocation ()
	{
		return this.location;
	}

	/**
	 * Set the location of this story in the map.
	 *
	 * @param location the new location of this story in the map.
	 */
	public void setLocation (Location location)
	{
		this.location = location;
	}

	@Override
	public String getImageFilename ()
	{
		return imageFilename;
	}

	/**
	 * Set the image filename of this place.
	 *
	 * @param filename the new image filename of this place.
	 */
	public void setImageFilename (String filename)
	{
		this.imageFilename = filename;
	}
	
	@Override
	public long getKey ()
	{
		return this.ID;
	}

	@Override
	public String toString ()
	{
		return String.format ("(%d, %s, @%s)", this.ID, this.imageFilename, this.location);
	}
}
