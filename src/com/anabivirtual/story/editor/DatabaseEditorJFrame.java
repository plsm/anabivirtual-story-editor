package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.BackgroundMusic;
import com.anabivirtual.story.db.Story;
import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import com.anabivirtual.story.db.Markable;
import com.anabivirtual.story.db.PointOfInterest;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.MutableComboBoxModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This frame displays components to edit a story database.
 * @author pedro
 */
public class DatabaseEditorJFrame
	extends javax.swing.JFrame
	implements MapComponentInitializedListener
{
	final JDBCDatabase database;
	final LocationTableModel locationTableModel;
	final StoryTableModel storyTableModel;
	final PointOfInterestTableModel pointOfInterestTableModel;
	final BackgroundMusicTableModel backgroundMusicTableModel;
	final Set<MutableComboBoxModel> locationComboBoxes;
	private GoogleMapView gmc;
	private GoogleMap map;
	/**
	 * Default value of inserted location latitude.
	 */
	final private double DEFAULT_LATITUDE = 0;
	/**
	 * Default value of inserted location longitude.
	 */
	final private double DEFAULT_LONGITUDE = 0;
	/**
	 * A map containing all the markers for all data in the map.
	 */
	final private HashMap<Object, Marker> dataMarkers;
	/**
	 * Creates new form DatabaseEditorJFrame
	 * @param file
	 * @param database
	 */
	public DatabaseEditorJFrame (File file, JDBCDatabase database)
	{
		this.database = database;
		this.locationTableModel = new LocationTableModel (database);
		this.storyTableModel = new StoryTableModel (database);
		this.pointOfInterestTableModel = new PointOfInterestTableModel (database);
		this.backgroundMusicTableModel = new BackgroundMusicTableModel (database);
		this.locationComboBoxes = new HashSet<> ();
		this.dataMarkers = new HashMap<> ();
		initComponents ();
		this.initLocationTable ();
		this.initStoryTable ();
		this.initPointOfInterestTable ();
		this.initBackgroundMusicTable ();
		this.setTitle (String.format (Utilities.getString ("DatabaseEditorFrameTitle"), file.getAbsolutePath ()));
		JFXPanel panel = new JFXPanel ();
		Platform.runLater (new Runnable ()
		{
			@Override
			public void run ()
			{
				gmc = new GoogleMapView ();
				gmc.addMapInitializedListener (DatabaseEditorJFrame.this);
				BorderPane root = new BorderPane(gmc);
				Scene scene = new Scene(root);
				panel.setScene(scene);
				mapPanel.add (panel, BorderLayout.CENTER);
			}
		});
	}

	private void initLocationTable ()
	{
		TableColumn latitudeColumn = this.locationsTable.getColumnModel ()
		  .getColumn (LocationTableModel.Column.LATITUDE.ordinal ());
		latitudeColumn.setCellRenderer (new LatitudeRenderer ());
		TableColumn longitudeColumn = this.locationsTable.getColumnModel ()
		  .getColumn (LocationTableModel.Column.LONGITUDE.ordinal ());
		longitudeColumn.setCellRenderer (new LongitudeRenderer ());
	}

	/**
	 * Initialise the story table to use a combo box to select locations,
	 * display location's name, and an android resource editor to verify audio
	 * filenames.
	 */
	private void initStoryTable ()
	{
		// location column
		TableColumn locationColumn = this.storiesTable.getColumnModel ()
		  .getColumn (StoryTableModel.Column.LOCATION.ordinal ());
		this.initLocationColumn (locationColumn);
		// audio filename column
		TableColumn filenameColumn = this.storiesTable.getColumnModel ()
		  .getColumn (StoryTableModel.Column.AUDIO_FILENAME.ordinal ());
		this.initAndroidResourceColumn (filenameColumn);
		// audio transcription column
		TableColumn audioTranscriptionColumn = this.storiesTable.getColumnModel ()
		  .getColumn (StoryTableModel.Column.TRANSCRIPTION.ordinal ());
		this.initMultiLineTextColumn (audioTranscriptionColumn);
	}

	/**
	 * Initialise the points of interest table to use a combo box to select
	 * locations, display location's name, and an android resource editor to
	 * verify image filenames.
	 */
	private void initPointOfInterestTable ()
	{
		// location column
		TableColumn locationColumn = this.pointsOfInterestTable.getColumnModel ()
		  .getColumn (PointOfInterestTableModel.Column.LOCATION.ordinal ());
		this.initLocationColumn (locationColumn);
		// image filename column
		TableColumn filenameColumn = this.pointsOfInterestTable.getColumnModel ()
		  .getColumn (PointOfInterestTableModel.Column.IMAGE_FILENAME.ordinal ());
		this.initAndroidResourceColumn (filenameColumn);
		// audio transcription column
		TableColumn audioTranscriptionColumn = this.pointsOfInterestTable.getColumnModel ()
		  .getColumn (PointOfInterestTableModel.Column.TRANSCRIPTION.ordinal ());
		this.initMultiLineTextColumn (audioTranscriptionColumn);
	}
	private void initBackgroundMusicTable ()
	{
		// audio filename
		TableColumn audioFilenameColumn = this.backgroundMusicTable.getColumnModel ()
		  .getColumn (BackgroundMusicTableModel.Column.AUDIO_FILENAME.ordinal ());
		this.initAndroidResourceColumn (audioFilenameColumn);
		// region center latitude
		TableColumn latitudeColumn = this.backgroundMusicTable.getColumnModel ()
		  .getColumn (BackgroundMusicTableModel.Column.REGION_CENTER_LATITUDE.ordinal ());
		this.initLatitudeColumn (latitudeColumn);
		// region center longitude
		TableColumn longitudeColumn = this.backgroundMusicTable.getColumnModel ()
		  .getColumn (BackgroundMusicTableModel.Column.REGION_CENTER_LONGITUDE.ordinal ());
		this.initLongitudeColumn (longitudeColumn);
	}
	/**
	 * Initialise the latitude column in a table in order to display a double
	 * value in degrees, in the format xx° yy' zz'' N|S.
	 *
	 * @param latitudeColumn the latitude column of a {@code JTable}.
	 */
	private void initLatitudeColumn (TableColumn latitudeColumn)
	{
		latitudeColumn.setCellRenderer (new LatitudeRenderer ());
	}
	/**
	 * Initalise the longitude column in a table in order to display a double
	 * value in degrees, in the format xx° yy' zz'' W|E.
	 *
	 * @param longitudeColumn the longitude column of a {@code JTable}.
	 */
	private void initLongitudeColumn (TableColumn longitudeColumn)
	{
		longitudeColumn.setCellRenderer (new LongitudeRenderer ());
	}
	/**
	 * Initialise the location column in a table in order to use a combo box to
	 * display available options and a renderer that shows location's name.
	 *
	 * @param locationColumn
	 * @return The combo box model used by the combo box to select locations.
	 */
	private ComboBoxModel initLocationColumn (TableColumn locationColumn)
	{
		ComboBoxModel model = new LocationComboBoxModel (this.database);
		JComboBox combox = new JComboBox (model);
		combox.setEditable (false);
		combox.setRenderer (new LocationInComboboxRenderer ());
		locationColumn.setCellEditor (new DefaultCellEditor (combox));
		locationColumn.setCellRenderer (new LocationInTableRenderer ());
		return model;
	}
	/**
	 * Initialise an android resource column in a table in order to use an editor
	 * that checks if the resource name is valid.
	 *
	 * @param androidResourceColumn the table column to initialise.
	 */
	private void initAndroidResourceColumn (TableColumn androidResourceColumn)
	{
		androidResourceColumn.setCellEditor (
		  new AndroidResourceEditor (this.pointsOfInterestTable));
	}

	/**
	 * Initialise a multi line text column in a table in order to use a text area
	 * to edit and display the text.
	 * @param multiLineTextColumn the table column to initialise.
	 */
	private void initMultiLineTextColumn (TableColumn multiLineTextColumn)
	{
		multiLineTextColumn.setCellEditor (
		  new MultiLineTextEditor (this)
		);
	}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      javax.swing.JSplitPane mainSplitPane = new javax.swing.JSplitPane();
      mapPanel = new javax.swing.JPanel();
      javax.swing.JPanel databasePanel = new javax.swing.JPanel();
      javax.swing.JTabbedPane databaseTabbedPane = new javax.swing.JTabbedPane();
      javax.swing.JPanel locationsPanel = new javax.swing.JPanel();
      javax.swing.JPanel locationsControlPanel = new javax.swing.JPanel();
      javax.swing.JButton insertLocationButton = new javax.swing.JButton();
      javax.swing.JButton deleteLocationButton = new javax.swing.JButton();
      javax.swing.JScrollPane locationsScrollPane = new javax.swing.JScrollPane();
      locationsTable = new javax.swing.JTable();
      javax.swing.JPanel storiesPanel = new javax.swing.JPanel();
      javax.swing.JScrollPane storiesScrollPane = new javax.swing.JScrollPane();
      storiesTable = new javax.swing.JTable();
      javax.swing.JPanel storiesControlPanel = new javax.swing.JPanel();
      javax.swing.JButton insertStoryButton = new javax.swing.JButton();
      javax.swing.JButton deleteStoryButton = new javax.swing.JButton();
      javax.swing.JPanel pointsOfInterestPanel = new javax.swing.JPanel();
      javax.swing.JScrollPane pointsOfInterestScrollPane = new javax.swing.JScrollPane();
      pointsOfInterestTable = new javax.swing.JTable();
      javax.swing.JPanel pointsOfInterestControlPanel = new javax.swing.JPanel();
      javax.swing.JButton insertPointOfInterestButton = new javax.swing.JButton();
      javax.swing.JButton deletePointOfInterestButton = new javax.swing.JButton();
      javax.swing.JPanel backgroundMusicPanel = new javax.swing.JPanel();
      javax.swing.JScrollPane backgroundMusicScrollPane = new javax.swing.JScrollPane();
      backgroundMusicTable = new javax.swing.JTable();
      javax.swing.JPanel backgroundMusicControlPanel = new javax.swing.JPanel();
      javax.swing.JButton insertBackgroundMusicButton = new javax.swing.JButton();
      javax.swing.JButton deleteBackgroundMusicButton = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      addWindowListener(new java.awt.event.WindowAdapter()
      {
         public void windowClosed(java.awt.event.WindowEvent evt)
         {
            formWindowClosed(evt);
         }
      });

      mapPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      mapPanel.setLayout(new java.awt.BorderLayout());
      mainSplitPane.setRightComponent(mapPanel);

      databasePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
      databasePanel.setLayout(new java.awt.BorderLayout());

      locationsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      locationsPanel.setLayout(new java.awt.BorderLayout());

      insertLocationButton.setText(Utilities.getString("NewLocation")); // NOI18N
      insertLocationButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            insertLocationButtonActionPerformed(evt);
         }
      });
      locationsControlPanel.add(insertLocationButton);

      deleteLocationButton.setText(Utilities.getString("DeleteLocation")); // NOI18N
      deleteLocationButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            deleteLocationButtonActionPerformed(evt);
         }
      });
      locationsControlPanel.add(deleteLocationButton);

      locationsPanel.add(locationsControlPanel, java.awt.BorderLayout.SOUTH);

      locationsTable.setModel(this.locationTableModel);
      locationsScrollPane.setViewportView(locationsTable);

      locationsPanel.add(locationsScrollPane, java.awt.BorderLayout.CENTER);

      databaseTabbedPane.addTab(Utilities.getString("Locations"), locationsPanel); // NOI18N

      storiesPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      storiesPanel.setLayout(new java.awt.BorderLayout());

      storiesTable.setModel(this.storyTableModel);
      storiesScrollPane.setViewportView(storiesTable);

      storiesPanel.add(storiesScrollPane, java.awt.BorderLayout.CENTER);

      insertStoryButton.setText(Utilities.getString("NewStory")); // NOI18N
      insertStoryButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            insertStoryButtonActionPerformed(evt);
         }
      });
      storiesControlPanel.add(insertStoryButton);

      deleteStoryButton.setText(Utilities.getString("DeleteStory")); // NOI18N
      deleteStoryButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            deleteStoryButtonActionPerformed(evt);
         }
      });
      storiesControlPanel.add(deleteStoryButton);

      storiesPanel.add(storiesControlPanel, java.awt.BorderLayout.SOUTH);

      databaseTabbedPane.addTab(Utilities.getString("Stories"), storiesPanel); // NOI18N

      pointsOfInterestPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      pointsOfInterestPanel.setLayout(new java.awt.BorderLayout());

      pointsOfInterestTable.setModel(this.pointOfInterestTableModel);
      pointsOfInterestScrollPane.setViewportView(pointsOfInterestTable);

      pointsOfInterestPanel.add(pointsOfInterestScrollPane, java.awt.BorderLayout.CENTER);

      insertPointOfInterestButton.setText(Utilities.getString("NewPointOfInterest")); // NOI18N
      insertPointOfInterestButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            insertPointOfInterestButtonActionPerformed(evt);
         }
      });
      pointsOfInterestControlPanel.add(insertPointOfInterestButton);

      deletePointOfInterestButton.setText(Utilities.getString("DeletePointOfInterest")); // NOI18N
      deletePointOfInterestButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            deletePointOfInterestButtonActionPerformed(evt);
         }
      });
      pointsOfInterestControlPanel.add(deletePointOfInterestButton);

      pointsOfInterestPanel.add(pointsOfInterestControlPanel, java.awt.BorderLayout.SOUTH);

      databaseTabbedPane.addTab(Utilities.getString("PointsOfInterest"), pointsOfInterestPanel); // NOI18N

      backgroundMusicPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      backgroundMusicPanel.setLayout(new java.awt.BorderLayout());

      backgroundMusicTable.setModel(this.backgroundMusicTableModel);
      backgroundMusicScrollPane.setViewportView(backgroundMusicTable);

      backgroundMusicPanel.add(backgroundMusicScrollPane, java.awt.BorderLayout.CENTER);

      insertBackgroundMusicButton.setText(Utilities.getString("NewBackgroundMusic")); // NOI18N
      insertBackgroundMusicButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            insertBackgroundMusicButtonActionPerformed(evt);
         }
      });
      backgroundMusicControlPanel.add(insertBackgroundMusicButton);

      deleteBackgroundMusicButton.setText(Utilities.getString("DeleteBackgroundMusic")); // NOI18N
      deleteBackgroundMusicButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            deleteBackgroundMusicButtonActionPerformed(evt);
         }
      });
      backgroundMusicControlPanel.add(deleteBackgroundMusicButton);

      backgroundMusicPanel.add(backgroundMusicControlPanel, java.awt.BorderLayout.SOUTH);

      databaseTabbedPane.addTab(Utilities.getString("BackgroundMusic"), backgroundMusicPanel); // NOI18N

      databasePanel.add(databaseTabbedPane, java.awt.BorderLayout.CENTER);

      mainSplitPane.setLeftComponent(databasePanel);

      getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void insertLocationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_insertLocationButtonActionPerformed
   {//GEN-HEADEREND:event_insertLocationButtonActionPerformed
		int row = this.database.getLocations ().size ();
		this.database.insertLocation (DEFAULT_LATITUDE, DEFAULT_LONGITUDE, "");
		this.locationTableModel.fireTableRowsInserted (row, row);
   }//GEN-LAST:event_insertLocationButtonActionPerformed

   private void deleteLocationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteLocationButtonActionPerformed
   {//GEN-HEADEREND:event_deleteLocationButtonActionPerformed
		int row = this.locationsTable.getSelectedRow ();
		if (row != -1) {
			this.database.deleteLocation (this.locationTableModel.getLocation (row));
			this.locationTableModel.fireTableRowsDeleted (row, row);
		}
   }//GEN-LAST:event_deleteLocationButtonActionPerformed

   private void insertStoryButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_insertStoryButtonActionPerformed
   {//GEN-HEADEREND:event_insertStoryButtonActionPerformed
		Collection<Location> ls = this.database.getLocations ();
		Location l = ls.iterator ().next ();
		Story s =
		  this.database.insertStory (l, "new title", "file.mp3", "transcription");
		this.storyTableModel.fireTableDataChanged ();
		int row = ls.size ();
		this.storyTableModel.fireTableRowsInserted (row, row);
		this.addMarkerForMarkable (s);
   }//GEN-LAST:event_insertStoryButtonActionPerformed

   private void deleteStoryButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteStoryButtonActionPerformed
   {//GEN-HEADEREND:event_deleteStoryButtonActionPerformed
		int row = this.storiesTable.getSelectedRow ();
		if (row != -1) {
			Story as = this.storyTableModel.getStory (row);
			this.database.deleteStory (as);
			this.storyTableModel.fireTableRowsDeleted (row, row);
			this.map.removeMarker (this.dataMarkers.remove (as));
		}
   }//GEN-LAST:event_deleteStoryButtonActionPerformed

   private void insertPointOfInterestButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_insertPointOfInterestButtonActionPerformed
   {//GEN-HEADEREND:event_insertPointOfInterestButtonActionPerformed
		Location l = this.getDefaultLocation ();
		PointOfInterest poi =
		  this.database.insertPointOfInterest (l, "title", "image.png", "audio.mp3", "transcription");
		Collection ps = this.database.getPointsOfInterest ();
		int row = ps.size ();
		this.pointOfInterestTableModel.fireTableRowsInserted (row, row);
		this.addMarkerForMarkable (poi);
   }//GEN-LAST:event_insertPointOfInterestButtonActionPerformed

   private void deletePointOfInterestButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deletePointOfInterestButtonActionPerformed
   {//GEN-HEADEREND:event_deletePointOfInterestButtonActionPerformed
		int row = this.pointsOfInterestTable.getSelectedRow ();
		if (row != -1) {
			PointOfInterest p = this.pointOfInterestTableModel.getPointOfInterest (row);
			this.database.deletePointOfInterest (p);
			this.pointOfInterestTableModel.fireTableRowsDeleted (row, row);
			this.map.removeMarker (this.dataMarkers.remove (p));
		}
   }//GEN-LAST:event_deletePointOfInterestButtonActionPerformed

   private void insertBackgroundMusicButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_insertBackgroundMusicButtonActionPerformed
   {//GEN-HEADEREND:event_insertBackgroundMusicButtonActionPerformed
		this.database.insertBackgroundMusic ("audio.mp3", DEFAULT_LATITUDE, DEFAULT_LONGITUDE, 1);
		Collection bs = this.database.getBackgroundMusic ();
		int row = bs.size ();
		this.backgroundMusicTableModel.fireTableRowsInserted (row, row);
   }//GEN-LAST:event_insertBackgroundMusicButtonActionPerformed

   private void deleteBackgroundMusicButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteBackgroundMusicButtonActionPerformed
   {//GEN-HEADEREND:event_deleteBackgroundMusicButtonActionPerformed
      int row = this.backgroundMusicTable.getSelectedRow ();
		if (row != -1) {
			BackgroundMusic bm = this.backgroundMusicTableModel.getBackgroundMusic (row);
			this.database.deleteBackgroundMusic (bm);
			this.backgroundMusicTableModel.fireTableRowsDeleted (row, row);
		}
   }//GEN-LAST:event_deleteBackgroundMusicButtonActionPerformed

   private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
   {//GEN-HEADEREND:event_formWindowClosed
		this.database.close ();
   }//GEN-LAST:event_formWindowClosed

	/**
	 * Get a location to be used in inserted stories and places.
	 *
	 * @return a location to be used in inserted stories and places.
	 */
	private Location getDefaultLocation ()
	{
		Collection<Location> ls = this.database.getLocations ();
		return ls.iterator ().next ();
	}

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JTable backgroundMusicTable;
   private javax.swing.JTable locationsTable;
   private javax.swing.JPanel mapPanel;
   private javax.swing.JTable pointsOfInterestTable;
   private javax.swing.JTable storiesTable;
   // End of variables declaration//GEN-END:variables

	@Override
	public void mapInitialized ()
	{
		float center_latitude = 0, center_longitude = 0;
		for (PointOfInterest poi : this.database.getPointsOfInterest ()) {
			center_latitude += poi.getLocation ().getLatitude ();
			center_longitude += poi.getLocation ().getLongitude ();
			Marker m = poi.computeMarker ();
			this.dataMarkers.put (poi, m);
		}
		for (Story s : this.database.getStories ()) {
			center_latitude += s.getLocation ().getLatitude ();
			center_longitude += s.getLocation ().getLongitude ();
			Marker m = s.computeMarker ();
			this.dataMarkers.put (s, m);
		}
		int size =
		  this.database.getPointsOfInterest ().size () +
		  this.database.getStories ().size ();
		if (size > 0) {
			center_latitude = center_latitude / size;
			center_longitude = center_longitude / size;
		}
		LatLong center = new LatLong (center_latitude, center_longitude);
		//Once the map has been loaded by the Webview, initialize the map details.
		MapOptions options = new MapOptions ();
		options.
		  center (center).
		  mapMarker (true).
		  zoom (9).
		  overviewMapControl (false).
		  panControl (false).
		  rotateControl (false).
		  scaleControl (false).
		  streetViewControl (false).
		  zoomControl (false).
		  mapType (MapTypeIdEnum.ROADMAP);
		this.map = this.gmc.createMap (options);
		// Add markers
		for (Marker m : this.dataMarkers.values ()) {
			this.map.addMarker (m);
		}
	}
	
	/**
	 * Add a marker in the map for the given markable.
	 * @param markable an object that has a marker.
	 */
	private void addMarkerForMarkable (Markable markable)
	{
		Platform.runLater (() -> {
			Marker m = markable.computeMarker ();
			this.map.addMarker (m);
			this.dataMarkers.put (markable, m);
		});
	}
}


/**
 * A latitude renderer.
 * Latitudes are displayed in degrees, minutes and seconds.
 * @author pedro
 */
class LatitudeRenderer
  extends javax.swing.table.DefaultTableCellRenderer
{
	public LatitudeRenderer ()
	{
		super ();
	}

	@Override
	public void setValue (Object value)
	{
		double latitude = (Double) value;
		int seconds = (int) Math.abs (Math.round (latitude * 3600));
		int minutes = (seconds / 60) % 60;
		int degrees = seconds / 3600;
		seconds = seconds % 60;
		char side = latitude > 0 ? 'N' : 'S';
		String v = String.format ("%2d° %2d' %2d'' %c", degrees, minutes, seconds, side);
		this.setText (v);
	}
}

/**
 * A longitude renderer.
 * Longitudes are displayed in degrees, minutes and seconds.
 * @author pedro
 */
class LongitudeRenderer
  extends javax.swing.table.DefaultTableCellRenderer
{
	public LongitudeRenderer ()
	{
		super ();
	}

	@Override
	public void setValue (Object value)
	{
		double latitude = (Double) value;
		int seconds = (int) Math.abs (Math.round (latitude * 3600));
		int minutes = (seconds / 60) % 60;
		int degrees = seconds / 3600;
		seconds = seconds % 60;
		char side = latitude < 0 ? 'W' : 'E';
		String v = String.format ("%2d° %2d' %2d'' %c", degrees, minutes, seconds, side);
		this.setText (v);
	}
}

class LocationInComboboxRenderer
  extends DefaultListCellRenderer
  implements
  ListCellRenderer<Object>
{
	@Override
	public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Location l = (Location) value;
		return super.getListCellRendererComponent (list, l.getName (), index, isSelected, cellHasFocus);
	}
}

class LocationInTableRenderer
  extends DefaultTableCellRenderer
{
	@Override
	public void setValue (Object value)
	{
		Location l = (Location) value;
		this.setText (l.getName ());
	}
}
