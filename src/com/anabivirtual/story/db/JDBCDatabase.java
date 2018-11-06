package com.anabivirtual.story.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
		if (this.locations != null)
			return this.locations.values ();
		String sql = "SELECT * FROM location";
		CursorRecord<Location> cr = (ResultSet rs) -> {
			return new Location (
				  rs.getLong ("ID"),
				  rs.getDouble ("latitude"),
				  rs.getDouble ("longitude"),
				  rs.getString ("name")
			);
		};
		Collection<Location> ls = this.toCollection (sql, cr);
		this.locations = new LinkedHashMap<> (ls.size ());
		for (Location l : ls) {
			this.locations.put (l.ID, l);
		}
		return this.locations.values ();
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
	//**************************************************************************
	/**
	 * Insert a new location in the database using the provided parameters.
	 * The collection returned by method {@code getLocations()} will hold
	 * the new location.
	 * @param latitude The latitude of the new location.
	 * @param longitude The longitude of the new location.
	 * @param name The name of the new location.
	 * @return An instance of {@code Location} representing the new location,
	 * {@code null} if an error occurred.
	 */
	public Location insertLocation (double latitude, double longitude, String name)
	{
		try {
			String sql = "INSERT INTO location (latitude, longitude, name) VALUES (?, ?, ?)";
			PreparedStatement ps = this.connection.prepareStatement (sql, Statement.RETURN_GENERATED_KEYS);
			ps.setDouble (1, latitude);
			ps.setDouble (2, longitude);
			ps.setString (3, name);
			ps.executeUpdate ();
			ResultSet rs = ps.getGeneratedKeys ();
			rs.next ();
			long newID = rs.getLong (1);
			ps.close ();
			Location insertedLocation = new Location (newID, latitude, longitude, name);
			this.locations.put (newID, insertedLocation);
			return insertedLocation;
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting location");
			System.err.println (ex.getMessage ());
			ex.printStackTrace (System.err);
			return null;
		}
	}
	/**
	 * Removes the given location from the database.
	 * The given location should be one in the collection returned by method {@code getLocations}.
	 * @param location The location to be removed.
	 * @return {@code false} if an error occurred {@code true} otherwise.
	 * @see #getLocations()
	 */
	public boolean removeLocation (Location location)
	{
		try {
			String sql = "DELETE FROM location WHERE ID = ?";
			PreparedStatement ps = this.connection.prepareStatement (sql);
			ps.setLong (1, location.ID);
			System.out.println (ps.toString ());
			int rowCount = ps.executeUpdate ();
			ps.close ();
			System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
			this.locations.remove (location.ID);
			return true;
		}
		catch (SQLException ex) {
			System.err.println ("Error removing location");
			ex.printStackTrace (System.err);
			return false;
		}
	}
	/**
	 * Updates the given location in the database.
	 * The given location should be one in the collection returned by method {@code getLocations}.
	 * @param location The updated location.
	 * @return {@code false} if an error occurred {@code true} otherwise.
	 * @see #getLocations()
	 */
	public boolean updateLocation (Location location)
	{
		try {
			String sql = "UPDATE location SET latitude = ?, longitude = ?, name = ? WHERE id = ?";
			PreparedStatement ps = this.connection.prepareStatement (sql);
			ps.setDouble (1, location.latitude);
			ps.setDouble (2, location.longitude);
			ps.setString (3, location.name);
			ps.setLong (4, location.ID);
			int rowCount = ps.executeUpdate ();
			ps.close ();
			System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
			return true;
		}
		catch (SQLException ex) {
			System.err.println ("Error updating location");
			ex.printStackTrace (System.err);
			return false;
		}
	}
	//**************************************************************************
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
			stmt.close ();
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

