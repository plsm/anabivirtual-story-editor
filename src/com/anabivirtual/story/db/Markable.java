package com.anabivirtual.story.db;

import com.lynden.gmapsfx.javascript.object.Marker;

/**
 * Provides method to return a marker for an object.
 * @author pedro
 */
public interface Markable
{
	Marker computeMarker ();
}
