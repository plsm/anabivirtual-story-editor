package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
final public class AudioStory
  extends AbstractStory
  implements com.anabivirtual.story.core.AudioStory
{
	String filename;
	public AudioStory (long ID, Location location, String title, String filename)
	{
		super (ID, location, title);
		this.filename = filename;
	}
	@Override
	public String toString ()
	{
		return String.format ("(%d, %s, %s, @%s)", this.ID, this.title, this.filename, this.location);
	}

	@Override
	public String getFilename ()
	{
		return this.filename;
	}

	@Override
	public void setFilename (String filename)
	{
		this.filename = filename;
	}
}
