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
import java.io.File;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

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
	private GoogleMapView gmc;
	private GoogleMap map;
	/**
	 * Creates new form DatabaseEditorJFrame
	 * @param file
	 * @param database
	 */
	public DatabaseEditorJFrame (File file, JDBCDatabase database)
	{
		this.database = database;
		this.locationTableModel = new LocationTableModel (database);
		initComponents ();
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
				getContentPane ().add(panel, java.awt.BorderLayout.EAST);
			}
		});
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

      controlPanel = new javax.swing.JPanel();
      newLocationButton = new javax.swing.JButton();
      deleteLocationButton = new javax.swing.JButton();
      javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
      javax.swing.JLabel dummyLabel = new javax.swing.JLabel();
      javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
      locationsTable = new javax.swing.JTable();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.X_AXIS));

      newLocationButton.setText(Utilities.getString("NewLocation")); // NOI18N
      controlPanel.add(newLocationButton);

      deleteLocationButton.setText(Utilities.getString("DeleteLocation")); // NOI18N
      controlPanel.add(deleteLocationButton);
      controlPanel.add(jSeparator1);

      dummyLabel.setText("...");
      controlPanel.add(dummyLabel);

      getContentPane().add(controlPanel, java.awt.BorderLayout.SOUTH);

      locationsTable.setModel(this.locationTableModel);
      jScrollPane1.setViewportView(locationsTable);

      getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JPanel controlPanel;
   private javax.swing.JButton deleteLocationButton;
   private javax.swing.JTable locationsTable;
   private javax.swing.JButton newLocationButton;
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

