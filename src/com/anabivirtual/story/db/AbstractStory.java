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
	final public long getID ()
	{
		return this.ID;
	}

	@Override
	final public String getTitle ()
	{
		return this.title;
	}

	@Override
	final public void setTitle (String title)
	{
		this.title = title;
	}

	@Override
	final public com.anabivirtual.story.core.Location getLocation ()
	{
		return this.location;
	}

	@Override
	final public void setLocation (com.anabivirtual.story.core.Location location)
	{
		this.location = location;
	}
}
