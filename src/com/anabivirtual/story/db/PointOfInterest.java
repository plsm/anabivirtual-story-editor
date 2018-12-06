package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 * @date 20/nov/2018
 */
final public class PointOfInterest
  implements
  com.anabivirtual.story.core.PointOfInterest<Location>,
  Keyable
{
	private final long ID;
	private Location location;
	private String imageFilename;
	private String audioFilename;
	private String audioTranscription;
	/**
	 * Create a new instance of a point of interest.
	 * @param ID the primary key in the {@code point_of_interest} table.
	 * @param location the location of the point of interest.
	 * @param imageFilename the image of the point of interest.
	 * @param audioFilename the audio with the description of the point of interest.
	 * @param audioTranscription the audio transcription of the point of interest.
	 */
	PointOfInterest (long ID, Location location, String imageFilename, String audioFilename, String audioTranscription)
	{
		this.ID = ID;
		this.location = location;
		this.imageFilename = imageFilename;
		this.audioFilename = audioFilename;
		this.audioTranscription = audioTranscription;
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
	 * Set the image filename of this point of interest.
	 *
	 * @param filename the new image filename of this point of interest.
	 */
	public void setImageFilename (String filename)
	{
		this.imageFilename = filename;
	}
	
	@Override
	public boolean hasImage ()
	{
		return this.imageFilename != null;
	}
	
	public void clearImageFilename ()
	{
		this.imageFilename = null;
	}
	
	@Override
	public String getAudioFilename ()
	{
		return this.audioFilename;
	}
	
	public void setAudioFilename (String value)
	{
		this.audioFilename = value;
	}
	
	@Override
	public String getTranscription ()
	{
		return this.audioTranscription;
	}

	/**
	 * Set the audio transcription of this point of interest.
	 *
	 * @param value the new audio transcription of this point of interest.
	 */
	public void setTranscription (String value)
	{
		this.audioTranscription = value;
	}

	@Override
	public long getKey ()
	{
		return this.ID;
	}

	@Override
	public String toString ()
	{
		String displayedTranscription = this.audioTranscription.length () > 30 ?
		  String.format (
		    "%s...%s",
		    this.audioTranscription.substring (0, 10),
		    this.audioTranscription.substring (this.audioTranscription.length () - 10)) :
		  this.audioTranscription;
		return String.format ("(%d, %s, %s, %s, @%s)",
		  this.ID,
		  this.imageFilename == null ? "NULL" : this.imageFilename,
		  this.audioFilename,
		  displayedTranscription,
		  this.location);
	}
}
