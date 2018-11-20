package com.anabivirtual.story.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
 
/**
 *
 * @author pedro
 */
final public class JDBCDatabase
  implements com.anabivirtual.story.core.Database<Location, Story, Place>
{
	private final Connection connection;
	private final Map<Long, Location> locations;
	private final Map<Long, Story> stories;
	private final Map<Long, Place> places;
	private final Map<Long, BackgroundMusic> backgroundMusic;
	JDBCDatabase (Connection connection)
	{
		this.connection = connection;
		this.locations = this.readLocations ();
		this.stories = this.readStories ();
		this.places = this.readPlaces ();
		this.backgroundMusic = this.readBackgroundMusic ();
	}
	/**
	 * Closes the database and clears all structures containing information from
	 * the database.
	 */
	public void close ()
	{
		try {
			this.connection.close ();
		}
		catch (SQLException ex) {
			System.err.println ("Error closing the database");
			ex.printStackTrace (System.err);
		}
		this.locations.clear ();
		this.stories.clear ();
		this.places.clear ();
		this.backgroundMusic.clear ();
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
			for (String createTable : readCreateDatabaseResource ()) {
				Statement stmt = conn.createStatement ();
				stmt.execute (createTable);
				stmt.close ();
			}
			return new JDBCDatabase (conn);
		}
		catch (SQLException ex) {
			ex.printStackTrace (System.err);
			System.exit (1);
			return null;
		}
	}

	/**
	 * Read the create-database.sql resource that contains the SQL data
	 * definition commands.
	 *
	 * The {@code CREATE TABLE} and {@code CREATE VIEW} statements must be
	 * separated by a single blank line.
	 *
	 * @return A collection of strings where each string corresponds
	 * to a command from the DDL.
	 */
	static private Collection<String> readCreateDatabaseResource ()
	{
		Collection<String> result = new LinkedList<> ();
		StringBuilder createStatement = new StringBuilder ();
		Object o = new Object ();
		InputStream is = o.getClass ().getResourceAsStream ("/com/anabivirtual/story/db/sql/create-database.sql");
		BufferedReader br = new BufferedReader (new InputStreamReader (is));
		boolean eof = false;
		boolean first_line_command = true;
		do {
			try {
				String line = br.readLine ();
				if (line == null) {
					result.add (createStatement.toString ());
					eof = true;
				}
				else if (line.isEmpty ()) {
					result.add (createStatement.toString ());
					createStatement = new StringBuilder ();
					first_line_command = true;
				}
				else {
					if (first_line_command) {
						first_line_command = false;
					}
					else {
						createStatement.append (' ');
					}
					createStatement.append (line);
				}
			}
			catch (IOException ex) {
				System.err.println ("Error reading create-database.sql resource");
				ex.printStackTrace (System.err);
				System.exit (1);
			}
		} while (!eof);
		return result;
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
	/**
	 * Read all rows from the {@code location} table in the database.
	 * @return A map from location's keys to {@code Location} instances.
	 */
	private Map<Long, Location> readLocations ()
	{
		String sql = "SELECT * FROM location";
		CursorToRecord<Location> c2r = (ResultSet rs) -> new Location (
		  rs.getLong ("ID"),
		  rs.getDouble ("latitude"),
		  rs.getDouble ("longitude"),
		  rs.getString ("name")
		);
		return toMap (sql, c2r);
	}
	/**
	 * Read all rows from the {@code view_story} view in the database and return
	 * a map containing {@code Story} instances.
	 *
	 * @return A map from story's primary keys to {@code Story} instances.
	 */
	private Map<Long, Story> readStories ()
	{
		String sql = "SELECT * FROM view_story";
		CursorToRecord<Story> c2r = (ResultSet rs) -> new Story (
		  rs.getLong ("story_ID"),
		  JDBCDatabase.this.locations.get (rs.getLong ("location_ID")),
		  rs.getString ("title"),
		  rs.getString ("audio_filename"),
		  rs.getString ("transcription")
		);
		return toMap (sql, c2r);
	}
	/**
	 * Read all rows from the {@code view_place} view in the database and return
	 * a map containing {@code Place} instances.
	 *
	 * @return a map containing {@code Place} instances.
	 */
	private Map<Long, Place> readPlaces ()
	{
		String sql = "SELECT * FROM view_place";
		CursorToRecord<Place> c2r = (ResultSet rs) -> new Place (
		  rs.getLong ("place_ID"),
		  JDBCDatabase.this.locations.get (rs.getLong ("location_ID")),
		  rs.getString ("image_filename")
		);
		return toMap (sql, c2r);
	}
	private Map<Long, BackgroundMusic> readBackgroundMusic ()
	{
		String sql = "SELECT * FROM background_music";
		CursorToRecord<BackgroundMusic> c2r = (ResultSet rs) -> new BackgroundMusic (
		  rs.getLong ("ID"),
		  rs.getString ("filename"),
		  rs.getDouble ("region_center_latitude"),
		  rs.getDouble ("region_center_longitude"),
		  rs.getDouble ("region_radius"));
		return toMap (sql, c2r);
	}
	@Override
	public Collection<Location> getLocations ()
	{
		return this.locations.values ();
	}
	@Override
	public Collection<Story> getStories ()
	{
		return this.stories.values ();
	}
	@Override
	public Collection<Place> getPlaces ()
	{
		return this.places.values ();
	}
	public Collection<BackgroundMusic> getBackgroundMusic ()
	{
		return this.backgroundMusic.values ();
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
		String sql = "INSERT INTO location (latitude, longitude, name) VALUES (?, ?, ?)";
		try (PreparedStatement ps = this.connection.prepareStatement (sql, Statement.RETURN_GENERATED_KEYS)) {
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
	public boolean deleteLocation (Location location)
	{
		String sql = "DELETE FROM location WHERE ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, location.getID ());
			ps.executeUpdate ();
			ps.close ();
			this.locations.remove (location.getID ());
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
		String sql = "UPDATE location SET latitude = ?, longitude = ?, name = ? WHERE id = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setDouble (1, location.getLatitude ());
			ps.setDouble (2, location.getLongitude ());
			ps.setString (3, location.getName ());
			ps.setLong (4, location.getID ());
			ps.executeUpdate ();
			ps.close ();
			return true;
		}
		catch (SQLException ex) {
			System.err.println ("Error updating location");
			ex.printStackTrace (System.err);
			return false;
		}
	}
	//**************************************************************************
	public Story insertStory (Location location, String title, String filename, String transcription)
	{
		String sql = "INSERT INTO story (location_ID, title, audio_filename, transcription) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, location.getID ());
			ps.setString (2, title);
			ps.setString (3, filename);
			ps.setString (4, transcription);
			ps.executeUpdate ();
			ResultSet rs = ps.getGeneratedKeys ();
			rs.next ();
			long story_ID = rs.getLong (1);
			ps.close ();
			Story insertedStory = new Story (
			  story_ID, location, title, filename, transcription);
			this.stories.put (story_ID, insertedStory);
			return insertedStory;
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting story");
			ex.printStackTrace (System.err);
			return null;
		}
	}
	public boolean deleteStory (Story story)
	{
		String sql = "DELETE FROM story WHERE ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, story.getID ());
			ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error removing story");
			ex.printStackTrace (System.err);
			return false;
		}
		this.stories.remove (story.getID ());
		return true;
	}
	public boolean updateStory (Story story)
	{
		String sql = "UPDATE story SET title = ?, location_ID = ?, audio_filename = ?, transcription = ? WHERE ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setString (1, story.getTitle ());
			ps.setLong (2, story.getLocation ().getID ());
			ps.setString (3, story.getAudioFilename ());
			ps.setString (4, story.getTranscription ());
			ps.setLong (5, story.getID ());
			ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error updating story");
			ex.printStackTrace (System.err);
			return false;
		}
		return true;
	}
	//**************************************************************************
	public BackgroundMusic insertBackgroundMusic (String filename,
	  double regionCenterLatitude, double regionCenterLongitude,
	  double regionRadius)
	{
		String sql = "INSERT INTO background_music (audio_filename, region_center_latitude, region_center_longitude, region_radius) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = this.connection.prepareStatement (sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString (1, filename);
			ps.setDouble (2, regionCenterLatitude);
			ps.setDouble (3, regionCenterLongitude);
			ps.setDouble (4, regionRadius);
			ps.executeUpdate ();
			ResultSet rs = ps.getGeneratedKeys ();
			rs.next ();
			long newID = rs.getLong (1);
			BackgroundMusic insertedBackgroundMusic = new BackgroundMusic (
			  newID,
			  filename,
			  regionCenterLatitude, regionCenterLongitude,
			  regionRadius
			);
			this.backgroundMusic.put (newID, insertedBackgroundMusic);
			return insertedBackgroundMusic;
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting background music " + filename);
			ex.printStackTrace (System.err);
			return null;
		}
	}
	public boolean deleteBackgroundMusic (BackgroundMusic backgroundMusic)
	{
		String sql = "DELETE FROM background_music WHERE ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, backgroundMusic.getID ());
			ps.executeUpdate ();
			ps.close ();
		}
		catch (SQLException ex) {
			System.err.println ("Error deleting background music " + backgroundMusic);
			ex.printStackTrace (System.err);
			return false;
		}
		this.backgroundMusic.remove (backgroundMusic.getID ());
		return true;
	}
	public boolean updateBackgroundMusic (BackgroundMusic backgroundMusic)
	{
		String sql = "UPDATE background_music SET audio_filename = ?, region_center_latitude = ?, region_center_longitude = ?, region_radius = ? WHERE ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setString (1, backgroundMusic.getAudioFilename ());
			ps.setDouble (2, backgroundMusic.getRegionCenterLatitude ());
			ps.setDouble (3, backgroundMusic.getRegionCenterLongitude ());
			ps.setDouble (4, backgroundMusic.getRegionRadius ());
			ps.setLong (5, backgroundMusic.getID ());
			ps.executeUpdate ();
			ps.close ();
		}
		catch (SQLException ex) {
			System.err.println ("Error while updating backgrund music " + backgroundMusic);
			ex.printStackTrace (System.err);
			return false;
		}
		return true;
	}
	//**************************************************************************
	/**
	 * Execute a SQL select statement and return the result as a map of objects.
	 * If there is an exception, an empty collection is returned.
	 *
	 * @param <T> The type that represents the rows of the result.
	 * Has to extend the {@code Keyable} interface in order to
	 * provide the row key.
	 * @param selectSql The SQL select statement.
	 * @param cr An object that converts a row to a Java object
	 * and returns its key.
	 * @return A map of objects representing the result of the SQL select.
	 */
	private <T extends Keyable> Map<Long, T> toMap (String selectSql, CursorToRecord<T> cr)
	{
		Map<Long, T> result = new LinkedHashMap<> ();
		try {
			Statement stmt = this.connection.createStatement ();
			ResultSet rs = stmt.executeQuery (selectSql);
			while (rs.next ()) {
				T record = cr.getData (rs);
				result.put (record.getKey (), record);
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
	 * This interface is used by the {@code toMap} method to convert a result set
	 * to a Java collection.
	 *
	 * @param <T> The type of the object that represents the rows in the result
	 * set.
	 * @see #toMap(java.lang.String, com.anabivirtual.story.db.JDBCDatabase.CursorToRecord)
	 */
	@FunctionalInterface
	private interface CursorToRecord<T> {
		/**
		 * Convert the current row to a Java object.
		 * @param cursor The result set holding the cursor to the current row.
		 * @return A Java object representing the current row.
		 * @throws SQLException
		 */
		public T getData (ResultSet cursor) throws SQLException;
	}
}

