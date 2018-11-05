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
	/**
	 * Checks if the given filename is a valid android resource.
	 *
	 * A resource filename must consist of the lowercase letters {@code a}
	 * through {@code z}, the digits {@code 0} through {@code 9} and the
	 * underscore character, {@code _}.
	 *
	 * @param filename the filename to check.
	 * @return {@code true} if the string represents a valid android resource
	 * filename.
	 */
	public static boolean isValidAndroidResourceFilename (String filename)
	{
		int last = filename.lastIndexOf ('.');
		if (last == -1) {
			last = filename.length ();
		}
		for (int i = 0; i < last; i++) {
			char c = filename.charAt (i);
			if (!Character.isDigit (c) && c != '_' && (c > 'z' || c < 'a')) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Convert a filename to a valid android resource filename.
	 *
	 * A resource filename must consist of the lowercase letters {@code a}
	 * through {@code z}, the digits {@code 0} through {@code 9} and the
	 * underscore character, {@code _}.
	 *
	 * @param filename the filename to convert.
	 * @return a valid android resource filename.
	 */
	public static String convertToValidAndroidResourceFilename (String filename)
	{
		java.lang.StringBuilder result = new StringBuilder (filename.length ());
		int last = filename.lastIndexOf ('.');
		if (last == -1) {
			last = filename.length ();
		}
		// process the characters up to the filename extension
		for (int i = 0; i < last; i++) {
			char c = filename.charAt (i);
			if (Character.isDigit (c) || c == '_' || (c >= 'a' && c <= 'z')) {
				result.append (c);
			}
			else if (c >= 'A' && c <= 'Z') {
				result.append (Character.toLowerCase (c));
			}
		}
		// add the filename extension
		for (int i = last; i < filename.length (); i++) {
			result.append (filename.charAt (i));
		}
		return result.toString ();
	}
}
