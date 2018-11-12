package com.anabivirtual.story.db;

/**
 * Represents an historical image story.
 * @author pedro
 * @date 12/nov/2018
 */
public class HistoricalImageStory
  extends AbstractStory
  implements com.anabivirtual.story.core.HistoricalImageStory<Location>
{
	public String filename;

	HistoricalImageStory (long ID, Location location, String title, String filename)
	{
		super (ID, location, title);
		this.filename = filename;
	}

	@Override
	public String getFilename ()
	{
		return filename;
	}

	@Override
	public void setFilename (String filename)
	{
		this.filename = filename;
	}
	
	@Override
	public String toString ()
	{
		return String.format ("(%d, %s, %s, @%s)", this.ID, this.title, this.filename, this.location);
	}
}
