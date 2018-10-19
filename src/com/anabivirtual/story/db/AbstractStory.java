package com.anabivirtual.story.db;

/**
 *
 * @author pedro
 */
abstract public class AbstractStory
{
	public final long ID;
	public Location location;
	public String title;
	static enum Type {
		AUDIO
	};
	protected AbstractStory (long ID, Location location, String title)
	{
		this.ID = ID;
		this.location = location;
		this.title = title;
	}
}
