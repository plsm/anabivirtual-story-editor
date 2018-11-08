package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
abstract public class AbstractStory
  implements com.anabivirtual.story.core.Story
{
	public final long ID;
	public com.anabivirtual.story.core.Location location;
	public String title;
	protected AbstractStory (long ID, Location location, String title)
	{
		this.ID = ID;
		this.location = location;
		this.title = title;
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

	@Override
	public void setTitle (String title)
	{
		this.title = title;
	}

	@Override
	public com.anabivirtual.story.core.Location getLocation ()
	{
		return this.location;
	}

	@Override
	public void setLocation (com.anabivirtual.story.core.Location location)
	{
		this.location = location;
	}
}
