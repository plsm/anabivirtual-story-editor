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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
 
/**
 *
 * @author pedro
 */
final public class JDBCDatabase
{
	private final Connection connection;
	private final Map<Long, Location> locations;
	private final Map<Long, AbstractStory> stories;
	private final Map<Long, AudioStory> audios;
	private final Map<Long, AudioBookStory> audioBooks;
	JDBCDatabase (Connection connection)
	{
		this.connection = connection;
		this.locations = this.readLocations ();
		this.stories = new LinkedHashMap<> ();
		this.audios = this.readAudioStories ();
		this.audioBooks = this.readAudioBookStories ();
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
			System.err.println (ex.getMessage());
			return null;
		}
	}

	/**
	 * Read the create-database.sql resource that contains the SQL data
	 * definition commands.
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
				}
				else {
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
	private Map<Long, Location> readLocations ()
	{
		String sql = "SELECT * FROM location";
		CursorToRecord<Location> c2r = new CursorToRecord<Location> ()
		{
			@Override
			public Location getData (ResultSet rs) throws SQLException
			{
				return new Location (
				  rs.getLong ("ID"),
				  rs.getDouble ("latitude"),
				  rs.getDouble ("longitude"),
				  rs.getString ("name")
				);
			}

			@Override
			public long getKey (ResultSet rs) throws SQLException
			{
				return rs.getLong ("ID");
			}
		};
		return toMap (sql, c2r);
	}
	private Map<Long, AudioStory> readAudioStories ()
	{
		String sql = "SELECT * FROM audio_story";
		CursorToRecord<AudioStory> cr = new CursorToRecord<AudioStory> ()
		{
			@Override
			public AudioStory getData (ResultSet rs) throws SQLException
			{
				return new AudioStory (
				  rs.getLong ("story_ID"),
				  locations.get (rs.getLong ("location_ID")),
				  rs.getString ("title"),
				  rs.getString ("filename")
				);
			}

			@Override
			public long getKey (ResultSet rs) throws SQLException
			{
				return rs.getLong ("location_ID");
			}
		};
		return toMap (sql, cr);
	}
	/**
	 * Read the view {@code audio_book} and return a map containing
	 * {@code AudioBookStory} instances.
	 *
	 * @return a map containing {@code AudioBookStory} instances.
	 */
	private Map<Long, AudioBookStory> readAudioBookStories ()
	{
		String sql = "SELECT * FROM audio_book";
		CursorToRecord<AudioBookStory> cr = new CursorToRecord<AudioBookStory> ()
		{
			@Override
			public AudioBookStory getData (ResultSet rs) throws SQLException
			{
				return new AudioBookStory (
				  rs.getLong ("story_ID"),
				  locations.get (rs.getLong ("location_ID")),
				  rs.getString ("title"),
				  rs.getString ("filename"),
				  rs.getString ("transcription")
				);
			}

			@Override
			public long getKey (ResultSet rs) throws SQLException
			{
				return rs.getLong ("location_ID");
			}
		};
		return toMap (sql, cr);
	}
	public Collection<Location> getLocations ()
	{
		return this.locations.values ();
	}
	public Collection<AbstractStory> getStories ()
	{
		return this.stories.values ();
	}
	public Collection<AudioStory> getAudioStories ()
	{
		return this.audios.values ();
	}
	public Collection<AudioBookStory> getAudioBookStories ()
	{
		return this.audioBooks.values ();
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
	private long insertStory (Location location, String title)
	{
		try {
			String sql = "INSERT INTO story (title, location_ID) VALUES (?, ?)";
			PreparedStatement ps = this.connection.prepareStatement (sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString (1, title);
			ps.setLong (2, location.ID);
			ps.executeUpdate ();
			ResultSet rs = ps.getGeneratedKeys ();
			rs.next ();
			long result = rs.getLong (1);
			ps.close ();
			return result;
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting story");
			System.err.println (ex.getMessage ());
			ex.printStackTrace (System.err);
			return -1;
		}
	}
	private boolean removeStory (AbstractStory story)
	{
		String sql = "DELETE FROM story WHERE ID = ?";
		int rowCount;
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, story.ID);
			rowCount = ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error removing story");
			ex.printStackTrace (System.err);
			return false;
		}
		this.stories.remove (story.ID);
		System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
		return true;
	}
	public boolean updateStory (AbstractStory story)
	{
		try {
			String sql = "UPDATE story SET title = ?, location_ID = ? WHERE id = ?";
			PreparedStatement ps = this.connection.prepareStatement (sql);
			ps.setString (1, story.title);
			ps.setLong (2, story.location.getID ());
			ps.setLong (3, story.ID);
			int rowCount = ps.executeUpdate ();
			ps.close ();
			System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
			return true;
		}
		catch (SQLException ex) {
			System.err.println ("Error updating story");
			ex.printStackTrace (System.err);
			return false;
		}
	}
	//**************************************************************************
	public AudioStory insertAudioStory (Location location, String title, String filename)
	{
		long story_ID = insertStory (location, title);
		try {
			String sql = "INSERT INTO audio (story_ID, filename) VALUES (?, ?)";
			PreparedStatement ps = this.connection.prepareStatement (sql);
			ps.setLong (1, story_ID);
			ps.setString (2, filename);
			ps.executeUpdate ();
			ps.close ();
			AudioStory insertedStory = new AudioStory (story_ID, location, title, filename);
			this.stories.put (story_ID, insertedStory);
			this.audios.put (story_ID, insertedStory);
			return insertedStory;
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting audio");
			System.err.println (ex.getMessage ());
			ex.printStackTrace (System.err);
			return null;
		}
	}
	public boolean deleteAudioStory (AudioStory story)
	{
		String sql = "DELETE FROM audio WHERE story_ID = ?";
		int rowCount;
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, story.ID);
			rowCount = ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error removing audio story");
			ex.printStackTrace (System.err);
			return false;
		}
		this.audios.remove (story.ID);
		System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
		return this.removeStory (story);
	}
	public boolean updateAudioStory (AudioStory story)
	{
		String sql;
		PreparedStatement ps;
		int rowCount;
		try {
			this.updateStory (story);
			sql = "UPDATE audio SET filename = ? WHERE story_ID = ?";
			ps = this.connection.prepareStatement (sql);
			ps.setString (1, story.filename);
			ps.setLong (2, story.ID);
			rowCount = ps.executeUpdate ();
			ps.close ();
			System.out.println (String.format ("%d row(s) where affected by this SQL DML", rowCount));
			return true;
		}
		catch (SQLException ex) {
			System.err.println ("Error updating audio story");
			ex.printStackTrace (System.err);
			return false;
		}
	}
	//**************************************************************************
	public AudioBookStory insertAudioBookStory (Location location, String title, String filename, String transcription)
	{
		long story_ID = insertStory (location, title);
		String sql = "INSERT INTO audio_book (story_ID, filename, transcription) VALUES (?, ?, ?)";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, story_ID);
			ps.setString (2, filename);
			ps.setString (3, transcription);
			ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error inserting audio book");
			ex.printStackTrace (System.err);
			return null;
		}
		AudioBookStory insertedStory = new AudioBookStory (story_ID, location, title, filename, transcription);
		this.stories.put (story_ID, insertedStory);
		this.audioBooks.put (story_ID, insertedStory);
		return insertedStory;
	}
	public boolean deleteAudioBookStory (AudioBookStory story)
	{
		String sql = "DELETE FROM audio_book WHERE story_ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setLong (1, story.ID);
			ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error removing audio book story");
			ex.printStackTrace (System.err);
			return false;
		}
		this.audioBooks.remove (story.ID);
		return this.removeStory (story);
	}
	public boolean updateAudioBookStory (AudioBookStory story)
	{
		this.updateStory (story);
		String sql = "UPDATE audio_book SET filename = ?, transcription = ? WHERE story_ID = ?";
		try (PreparedStatement ps = this.connection.prepareStatement (sql)) {
			ps.setString (1, story.filename);
			ps.setString (2, story.transcription);
			ps.setLong (3, story.ID);
			ps.executeUpdate ();
		}
		catch (SQLException ex) {
			System.err.println ("Error updating audio book story");
			ex.printStackTrace (System.err);
			return false;
		}
		return true;
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
	/**
	 * Execute a SQL select statement and return the result as a map of objects.
	 * If there is an exception, an empty collection is returned.
	 *
	 * @param <T> The type that represents the rows of the result.
	 * @param selectSql The SQL select statement.
	 * @param cr An object that converts a row to a Java object
	 * and returns its key.
	 * @return A map of objects representing the result of the SQL select.
	 */
	private <T> Map<Long, T> toMap (String selectSql, CursorToRecord<T> cr)
	{
		Map<Long, T> result = new LinkedHashMap<> ();
		try {
			Statement stmt = this.connection.createStatement ();
			ResultSet rs = stmt.executeQuery (selectSql);
			while (rs.next ()) {
				result.put (cr.getKey (rs), cr.getData (rs));
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
	private interface CursorToRecord<T> {
		/**
		 * Convert the current row to a Java object.
		 * @param cursor The result set holding the cursor to the current row.
		 * @return A Java object representing the current row.
		 * @throws SQLException
		 */
		public T getData (ResultSet cursor) throws SQLException;
		/**
		 * Get the key of the current row.
		 * @param cursor The result set holding the cursor to the current row.
		 * @return the key of the current row.
		 * @throws SQLException
		 */
		public long getKey (ResultSet cursor) throws SQLException;
	}
}

