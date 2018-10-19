package com.anabivirtual.story.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
 
/**
 *
 * @author pedro
 */
public class JDBCDatabase
	  implements Database
{
	private final Connection connection;
	private Map<Long, Location> locations;
	JDBCDatabase (Connection connection)
	{
		this.connection = connection;
	}

	static public JDBCDatabase createDatabase (String fileName)
	{
		File dbFile = new File (fileName);
		dbFile.delete ();
		try {
			String url = String.format ("jdbc:sqlite:%s", fileName);
			// create database
			Connection conn = DriverManager.getConnection (url);
			DatabaseMetaData meta = conn.getMetaData ();
			System.out.println ("The driver name is " + meta.getDriverName ());
			System.out.println ("A new database has been created.");
			// SQLite connection string
			Statement stmt = conn.createStatement ();
			stmt.execute ("CREATE TABLE location (\n" +
"  ID INTEGER PRIMARY KEY ASC NOT NULL,\n" +
"  latitude REAL NOT NULL,\n" +
"  longitude REAL NOT NULL,\n" +
"  name TEXT NOT NULL\n" +
"  )");
			stmt.execute ("CREATE TABLE story (\n" +
"  ID INTEGER PRIMARY KEY ASC NOT NULL,\n" +
"  title TEXT NOT NULL,\n" +
"  type INT NOT NULL,\n" +
"  location_ID INT NOT NULL,\n" +
"  FOREIGN KEY (location_ID) REFERENCES location (ID) ON DELETE CASCADE\n" +
")");
			stmt.execute ("CREATE TABLE audio (\n" +
"  story_ID INTEGER NOT NULL,\n" +
"  filename TEXT NOT NULL,\n" +
"  FOREIGN KEY (story_ID) REFERENCES story (ID) ON DELETE CASCADE\n" +
")");
			stmt.execute ("CREATE INDEX location_index\n" +
"  ON location (latitude, longitude)");
			stmt.execute ("CREATE VIEW audio_story (\n" +
"  location_ID,\n" +
"  story_ID,\n" +
"  latitude,\n" +
"  longitude,\n" +
"  name,\n" +
"  title,\n" +
"  filename\n" +
") AS SELECT\n" +
"  location.ID,\n" +
"  story.ID,\n" +
"  location.latitude,\n" +
"  location.longitude,\n" +
"  location.name,\n" +
"  story.title,\n" +
"  audio.filename\n" +
"  FROM location\n" +
"  INNER JOIN story ON location.ID = story.location_ID\n" +
"  INNER JOIN audio ON story.ID = audio.story_ID");
			return new JDBCDatabase (conn);
		}
		catch (SQLException ex) {
			System.err.println (ex.getMessage());
			return null;
		}
	}

	static public JDBCDatabase editDatabase (String filename)
	{
		try {
			// db parameters
			String url = String.format ("jdbc:sqlite:%s", filename);
			// create a connection to the database
			Connection conn = DriverManager.getConnection (url);
			System.out.println ("Connection to SQLite has been established.");
			return new JDBCDatabase (conn);
		}
		catch (SQLException ex) {
			System.err.println (ex.getMessage ());
			return null;
		}
	}
	@Override
	public Collection<Location> getLocations ()
	{
		String sql = "SELECT * FROM location";
		CursorRecord<Location> cr = (ResultSet rs) -> {
			return new Location (
				  rs.getLong ("ID"),
				  rs.getDouble ("latitude"),
				  rs.getDouble ("longitude"),
				  rs.getString ("name")
			);
		};
		Collection<Location> result = this.toCollection (sql, cr);
		this.locations = new LinkedHashMap<> (result.size ());
		for (Location l : result) {
			this.locations.put (l.ID, l);
		}
		return result;
	}

	@Override
	public Collection<AudioStory> getAudioStories ()
	{
		if (this.locations == null)
			this.getLocations ();
		String sql = "SELECT * FROM audio_story";
		CursorRecord<AudioStory> cr = (ResultSet rs) -> {
			return new AudioStory (
				  rs.getLong ("story_ID"),
				  this.locations.get (rs.getLong ("location_ID")),
				  rs.getString ("title"),
				  rs.getString ("filename")
			);
		};
		return this.toCollection (sql, cr);
	}
	/**
	 * Execute a SQL select statement and return the result as a collection of objects.
	 * If there is an exception, an empty collection is returned.
	 *
	 * @param <T> The type that represents the rows of the result.
	 * @param selectSql The SQL select statement.
	 * @param cr A function that converts a row to a Java object.
	 * @return A collection of objects representing the result of the SQL select.
	 */
	private <T> Collection<T> toCollection (String selectSql, CursorRecord<T> cr)
	{
		ArrayList<T> result = new ArrayList<> ();
		try {
			Statement stmt = this.connection.createStatement ();
			ResultSet rs = stmt.executeQuery (selectSql);
			while (rs.next ()) {
				result.add (cr.getData (rs));
			}
		}
		catch (SQLException ex) {
			System.err.println (ex.getMessage ());
		}
		return result;
	}
	/**
	 * An interface that maps the current row in a result set to a Java object.
	 * This interface is used by the {@code toCollection method} to convert a result set to a Java collection.
	 *
	 * @param <T> The type of the object that represents the rows in the result set.
	 * @see #toCollection(java.lang.String, com.anabivirtual.story.db.JDBCDatabase.CursorRecord)
	 */
	@FunctionalInterface
	private interface CursorRecord<T> {
		/**
		 * Convert the current row to a Java object.
		 * @param cursor The result set holding the cursor to the current row.
		 * @return A Java object representing the current row.
		 * @throws SQLException 
		 */
		public T getData (ResultSet cursor) throws SQLException;
	}
}

