package com.anabivirtual.story.db;

/**
 * Represents background music that is played when the user is inside a circular
 * region.
 *
 * @author pedro
 * @date 12/nov/2018
 */
public class BackgroundMusic
  implements
  com.anabivirtual.story.core.BackgroundMusic,
  Keyable
{

	final private long ID;
	private String filename;
	private double regionCenterLatitude;
	private double regionCenterLongitude;
	private double regionRadius;

	BackgroundMusic (long ID, String filename,
	  double regionCenterLatitude,
	  double regionCenterLongitude,
	  double regionRadius)
	{
		this.ID = ID;
		this.filename = filename;
		this.regionCenterLatitude = regionCenterLatitude;
		this.regionCenterLongitude = regionCenterLongitude;
		this.regionRadius = regionRadius;
	}

	@Override
	public long getKey ()
	{
		return this.ID;
	}

	@Override
	public long getID ()
	{
		return ID;
	}

	@Override
	public String getAudioFilename ()
	{
		return filename;
	}

	/**
	 * Set the audio filename of this music.
	 *
	 * @param filename the new audio filename of this music.
	 */
	public void setAudioFilename (String filename)
	{
		this.filename = filename;
	}

	@Override
	public double getRegionCenterLatitude ()
	{
		return regionCenterLatitude;
	}

	/**
	 * Set the latitude of the center of the region where the music is played.
	 * Units are degree.
	 *
	 * @param regionCenterLatitude the new latitude of the center of the region
	 * where the music is played.
	 */
	public void setRegionCenterLatitude (double regionCenterLatitude)
	{
		this.regionCenterLatitude = regionCenterLatitude;
	}

	@Override
	public double getRegionCenterLongitude ()
	{
		return regionCenterLongitude;
	}

	/**
	 * Set the longitude of the center of the region where the music is played.
	 * Units are degree.
	 *
	 * @param regionCenterLongitude the new longitude of the center of the region
	 * where the music is played.
	 */
	public void setRegionCenterLongitude (double regionCenterLongitude)
	{
		this.regionCenterLongitude = regionCenterLongitude;
	}

	@Override
	public double getRegionRadius ()
	{
		return regionRadius;
	}

	/**
	 * Set the radius of the region where the music is played.
	 *
	 * @param regionRadius the new radius of the region where the music is
	 * played.
	 */
	public void setRegionRadius (double regionRadius)
	{
		this.regionRadius = regionRadius;
	}
	
	public String toString ()
	{
		return String.format ("(%d, %s, (%f, %f)-%f)",
		  this.ID, this.filename,
		  this.regionCenterLatitude, this.regionCenterLongitude,
		  this.regionRadius);
	}
}
