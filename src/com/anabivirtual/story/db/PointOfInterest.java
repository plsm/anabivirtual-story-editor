package com.anabivirtual.story.db;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

/**
 *
 * @author pedro
 * @date 20/nov/2018
 */
final public class PointOfInterest
  implements
  com.anabivirtual.story.core.PointOfInterest<Location>,
  Markable,
  Keyable
{
	private final long ID;
	private Location location;
	private String imageFilename;
	private boolean hasImage;
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
	private PointOfInterest (long ID, Location location, String imageFilename, String audioFilename, String audioTranscription)
	{
		this.ID = ID;
		this.location = location;
		this.imageFilename = imageFilename;
		this.hasImage = imageFilename != null;
		this.audioFilename = audioFilename;
		this.audioTranscription = audioTranscription;
	}

	/**
	 * Create a new instance of a point of interest.
	 * @param ID the primary key in the {@code point_of_interest} table.
	 * @param location the location of the point of interest.
	 * @param imageFilename the image of the point of interest.
	 * @param audioFilename the audio with the description of the point of interest.
	 * @param audioTranscription the audio transcription of the point of interest.
	 */
	static PointOfInterest create (long ID, Location location, String imageFilename, String audioFilename, String audioTranscription)
	{
		PointOfInterest result = new PointOfInterest (
		  ID, location, imageFilename, audioFilename, audioTranscription);
		return result;
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
		return this.hasImage;
	}
	
	public void disableImage ()
	{
		this.hasImage = false;
	}
	
	public void enableImage ()
	{
		this.hasImage = true;
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

	/**
	 * Compute the marker for this point of interest.
	 * @return the marker for this point of interest.
	 */
	@Override
	public Marker computeMarker ()
	{
		LatLong ll = new LatLong (
		  this.location.getLatitude (),
		  this.location.getLongitude ());
		MarkerOptions markerOptions = new MarkerOptions ();
		markerOptions
		  .position (ll)
		  .title (String.format ("POI\n@%s", this.location.getName ()))
		  .visible (true)
		;
		return new Marker (markerOptions);
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
