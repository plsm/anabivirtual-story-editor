package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
final public class AudioStory extends AbstractStory
{
	String filename;
	public AudioStory (long ID, Location location, String title, String filename)
	{
		super (ID, location, title);
		this.filename = filename;
	}
	public String toString ()
	{
		return String.format ("(%d, %s, %s, @%s)", this.ID, this.title, this.filename, this.location);
	}
}
