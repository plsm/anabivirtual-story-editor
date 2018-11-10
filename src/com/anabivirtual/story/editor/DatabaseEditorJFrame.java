package com.anabivirtual.story.editor;

import com.anabivirtual.story.db.JDBCDatabase;
import com.anabivirtual.story.db.Location;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.LinkedList;
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
	final AudioStoryTableModel audioStoryTableModel;
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
	 * Creates new form DatabaseEditorJFrame
	 * @param file
	 * @param database
	 */
	public DatabaseEditorJFrame (File file, JDBCDatabase database)
	{
		this.database = database;
		this.locationTableModel = new LocationTableModel (database);
		this.storyTableModel = new StoryTableModel (database);
		this.audioStoryTableModel = new AudioStoryTableModel (database);
		initComponents ();
		this.initLocationTable ();
		this.initStoryTable ();
		this.initAudioStoryTable ();
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

	private void initStoryTable ()
	{
		TableColumn locationColumn = this.storiesTable.getColumnModel ()
		  .getColumn (StoryTableModel.Column.LOCATION.ordinal ());
		JComboBox combox = new JComboBox (new LocationComboBoxModel (this.database));
		combox.setEditable (false);
		combox.setRenderer (new LocationInComboboxRenderer ());
		locationColumn.setCellEditor (new DefaultCellEditor (combox));
		locationColumn.setCellRenderer (new LocationInTableRenderer ());
	}
	/**
	 * Initialise the audio story table to use a combo box to select locations,
	 * display location's name, and an editor to verify audio filenames.
	 */
	private void initAudioStoryTable ()
	{
		TableColumn locationColumn = this.audioStoriesTable.getColumnModel ()
		  .getColumn (AudioStoryTableModel.Column.LOCATION.ordinal ());
		this.initLocationColumn (locationColumn);
		TableColumn filenameColumn = this.audioStoriesTable.getColumnModel ()
		  .getColumn (AudioStoryTableModel.Column.FILENAME.ordinal ());
		filenameColumn.setCellEditor (new AndroidResourceEditor (this.audioStoriesTable));
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
      javax.swing.JTabbedPane databaseTabbedPane = new javax.swing.JTabbedPane();
      javax.swing.JPanel locationsPanel = new javax.swing.JPanel();
      javax.swing.JPanel locationsControlPanel = new javax.swing.JPanel();
      newLocationButton = new javax.swing.JButton();
      deleteLocationButton = new javax.swing.JButton();
      javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
      javax.swing.JLabel dummyLabel = new javax.swing.JLabel();
      javax.swing.JScrollPane locationsScrollPane = new javax.swing.JScrollPane();
      locationsTable = new javax.swing.JTable();
      javax.swing.JPanel storiesPanel = new javax.swing.JPanel();
      javax.swing.JScrollPane storiesScrollPane = new javax.swing.JScrollPane();
      storiesTable = new javax.swing.JTable();
      javax.swing.JPanel storiesControlPanel = new javax.swing.JPanel();
      insertStoryButton = new javax.swing.JButton();
      deleteStoryButton = new javax.swing.JButton();
      javax.swing.JPanel audioStoriesPanel = new javax.swing.JPanel();
      javax.swing.JScrollPane audioStoriesScrollPane = new javax.swing.JScrollPane();
      audioStoriesTable = new javax.swing.JTable();
      javax.swing.JPanel audioStoriesControlPanel = new javax.swing.JPanel();
      javax.swing.JButton insertAudioStoryButton = new javax.swing.JButton();
      javax.swing.JButton deleteAudioStoryButton = new javax.swing.JButton();
      mapPanel = new javax.swing.JPanel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      locationsPanel.setLayout(new java.awt.BorderLayout());

      locationsControlPanel.setLayout(new javax.swing.BoxLayout(locationsControlPanel, javax.swing.BoxLayout.X_AXIS));

      newLocationButton.setText(Utilities.getString("NewLocation")); // NOI18N
      newLocationButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            newLocationButtonActionPerformed(evt);
         }
      });
      locationsControlPanel.add(newLocationButton);

      deleteLocationButton.setText(Utilities.getString("DeleteLocation")); // NOI18N
      deleteLocationButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            deleteLocationButtonActionPerformed(evt);
         }
      });
      locationsControlPanel.add(deleteLocationButton);
      locationsControlPanel.add(jSeparator1);

      dummyLabel.setText("...");
      locationsControlPanel.add(dummyLabel);

      locationsPanel.add(locationsControlPanel, java.awt.BorderLayout.SOUTH);

      locationsTable.setModel(this.locationTableModel);
      locationsScrollPane.setViewportView(locationsTable);

      locationsPanel.add(locationsScrollPane, java.awt.BorderLayout.CENTER);

      databaseTabbedPane.addTab(Utilities.getString("Locations"), locationsPanel); // NOI18N

      storiesPanel.setLayout(new java.awt.BorderLayout());

      storiesTable.setModel(this.storyTableModel);
      storiesScrollPane.setViewportView(storiesTable);

      storiesPanel.add(storiesScrollPane, java.awt.BorderLayout.CENTER);

      insertStoryButton.setText(Utilities.getString("InsertStory")); // NOI18N
      storiesControlPanel.add(insertStoryButton);

      deleteStoryButton.setText(Utilities.getString("DeleteStory")); // NOI18N
      storiesControlPanel.add(deleteStoryButton);

      storiesPanel.add(storiesControlPanel, java.awt.BorderLayout.SOUTH);

      databaseTabbedPane.addTab(Utilities.getString("Stories"), storiesPanel); // NOI18N

      audioStoriesPanel.setLayout(new java.awt.BorderLayout());

      audioStoriesTable.setModel(this.audioStoryTableModel);
      audioStoriesScrollPane.setViewportView(audioStoriesTable);

      audioStoriesPanel.add(audioStoriesScrollPane, java.awt.BorderLayout.CENTER);

      insertAudioStoryButton.setText(Utilities.getString("Insert")); // NOI18N
      audioStoriesControlPanel.add(insertAudioStoryButton);

      deleteAudioStoryButton.setText(Utilities.getString("Delete")); // NOI18N
      audioStoriesControlPanel.add(deleteAudioStoryButton);

      audioStoriesPanel.add(audioStoriesControlPanel, java.awt.BorderLayout.SOUTH);

      databaseTabbedPane.addTab(Utilities.getString("AudioStories"), audioStoriesPanel); // NOI18N

      mainSplitPane.setLeftComponent(databaseTabbedPane);

      mapPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      mapPanel.setLayout(new java.awt.BorderLayout());
      mainSplitPane.setRightComponent(mapPanel);

      getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void newLocationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newLocationButtonActionPerformed
   {//GEN-HEADEREND:event_newLocationButtonActionPerformed
		int row = this.database.getLocations ().size ();
		this.database.insertLocation (DEFAULT_LATITUDE, DEFAULT_LONGITUDE, "");
		this.locationTableModel.fireTableRowsInserted (row, row);
   }//GEN-LAST:event_newLocationButtonActionPerformed

   private void deleteLocationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteLocationButtonActionPerformed
   {//GEN-HEADEREND:event_deleteLocationButtonActionPerformed
		int row = this.locationsTable.getSelectedRow ();
		if (row != -1) {
			this.database.removeLocation (this.locationTableModel.getLocation (row));
			this.locationTableModel.fireTableRowsDeleted (row, row);
		}
   }//GEN-LAST:event_deleteLocationButtonActionPerformed

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JTable audioStoriesTable;
   private javax.swing.JButton deleteLocationButton;
   private javax.swing.JButton deleteStoryButton;
   private javax.swing.JButton insertStoryButton;
   private javax.swing.JTable locationsTable;
   private javax.swing.JPanel mapPanel;
   private javax.swing.JButton newLocationButton;
   private javax.swing.JTable storiesTable;
   // End of variables declaration//GEN-END:variables

	@Override
	public void mapInitialized ()
	{
		LinkedList<Marker> markers = new LinkedList<> ();
		float center_latitude = 0, center_longitude = 0;
		for (Location l : this.database.getLocations ()) {
			center_latitude += l.latitude;
			center_longitude += l.longitude;
			LatLong ll = new LatLong (l.latitude, l.longitude);
			MarkerOptions markerOptions = new MarkerOptions ();
			markerOptions
			  .position (ll)
			  .title (l.name)
			  .visible (true)
			;
			markers.add (new Marker (markerOptions));
		}
		center_latitude = center_latitude / this.database.getLocations ().size ();
		center_longitude = center_longitude / this.database.getLocations ().size ();
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
		for (Marker m : markers) {
			this.map.addMarker (m);
		}
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
		return super.getListCellRendererComponent (list, l.name, index, isSelected, cellHasFocus);
	}
}

class LocationInTableRenderer
  extends DefaultTableCellRenderer
{
	@Override
	public void setValue (Object value)
	{
		Location l = (Location) value;
		this.setText (l.name);
	}
}
