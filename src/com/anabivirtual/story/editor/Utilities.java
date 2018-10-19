package com.anabivirtual.story.editor;

import java.util.ResourceBundle;

/**
 *
 * @author pedro
 */
public class Utilities
{
	public static final ResourceBundle STRING_BUNDLE = ResourceBundle.getBundle ("com/anabivirtual/story/editor/StringBundle");
	public static String getString (String key)
	{
		return STRING_BUNDLE.getString (key);
	}
}
