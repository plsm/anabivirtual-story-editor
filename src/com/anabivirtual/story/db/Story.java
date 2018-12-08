package com.anabivirtual.story.db;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

/**
 * An editable story.
 * Instances of this class represent records in table {@code story}.
 * 
 * @author pedro
 * @date 20/nov/2018
 */
final public class Story
  implements
  com.anabivirtual.story.core.Story<Location>,
  Markable,
  Keyable
{
	private final long ID;
	private Location location;
	private String title;
	private String filename;
	private String transcription;
	/**
	 * Construct a story with the given parameters.
	 * @param ID the primary key of the corresponding record in table {@code story}. 
	 * @param location the location of this story in the globe.
	 * @param title the title of this story.
	 * @param filename the audio filename containing the story that is played to the user.
	 * @param transcription the audio transcription
	 */
	private Story (long ID, Location location, String title, String filename, String transcription)
	{
		this.ID = ID;
		this.location = location;
		this.title = title;
		this.filename = filename;
		this.transcription = transcription;
	}

	/**
	 * Create a story with the given parameters.
	 * @param ID the primary key of the corresponding record in table {@code story}. 
	 * @param location the location of this story in the globe.
	 * @param title the title of this story.
	 * @param filename the audio filename containing the story that is played to the user.
	 * @param transcription the audio transcription
	 */
	static Story create (long ID, Location location, String title, String filename, String transcription)
	{
		Story result = new Story (
		  ID, location, title, filename, transcription);
		return result;
	}

	@Override
	public long getID ()
	{
		return this.ID;
	}

	@Override
	public String getTitle ()
	{
		return this.title;
	}

	/**
	 * Set the title of this story.
	 *
	 * @param title the new title of this story.
	 */
	public void setTitle (String title)
	{
		this.title = title;
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
	public String getAudioFilename ()
	{
		return this.filename;
	}

	/**
	 * Set the audio filename of this story.
	 *
	 * @param filename the new audio filename of this story.
	 */
	public void setAudioFilename (String filename)
	{
		this.filename = filename;
	}

	@Override
	public String getTranscription ()
	{
		return this.transcription;
	}

	/**
	 * Set the audio transcription of this story.
	 *
	 * @param value the new audio transcription of this story.
	 */
	public void setTranscription (String value)
	{
		this.transcription = value;
	}

	@Override
	public long getKey ()
	{
		return this.ID;
	}

	/**
	 * Compute the marker for this story.
	 * @return the marker for this story.
	 */
	@Override
	public Marker computeMarker ()
	{
		LatLong ll = new LatLong (
		  this.location.getLatitude (),
		  this.location.getLongitude ());
		String t = String.format (
		  "%s\n@%s",
		  this.title,
		  this.location.getName ());
		MarkerOptions markerOptions = new MarkerOptions ();
		markerOptions
		  .position (ll)
		  .title (t)
		  .visible (true)
		;
		return new Marker (markerOptions);
	}

	@Override
	public String toString ()
	{
		String displayedTranscription = this.transcription.length () > 30 ?
		  String.format (
		    "%s...%s",
		    this.transcription.substring (0, 10),
		    this.transcription.substring (this.transcription.length () - 10)) :
		  this.transcription;
		return String.format ("(%d, %s, %s, %s, @%s)",
		  this.ID, this.title, this.filename, displayedTranscription, this.location);
	}
}
