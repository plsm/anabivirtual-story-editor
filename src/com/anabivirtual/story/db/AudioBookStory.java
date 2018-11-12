package com.anabivirtual.story.db;

/**
 * Represents a story with an audio file and with an audio transcription.
 *
 * @author pedro
 * @date 8/nov/2018
 */
public class AudioBookStory
  extends AudioStory
  implements com.anabivirtual.story.core.AudioBookStory<Location>
{
	public String transcription;

	AudioBookStory (long ID, Location location, String title, String filename, String transcription)
	{
		super (ID, location, title, filename);
		this.transcription = transcription;
	}

	@Override
	final public String getTranscription ()
	{
		return this.transcription;
	}

	@Override
	final public void setTranscription (String value)
	{
		this.transcription = value;
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
