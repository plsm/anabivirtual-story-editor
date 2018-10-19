package com.anabivirtual.story.db;

import java.util.Collection;

/**
 *
 * @author pedro
 */
public interface Database
{

	/**
	 *
	 * @return
	 */
	public Collection<Location> getLocations ();

	/**
	 *
	 * @return
	 */
	public Collection<AudioStory> getAudioStories ();
}
