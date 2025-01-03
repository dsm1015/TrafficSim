package edu.sru.dsm1015.TrafficSim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import edu.sru.dsm1015.TrafficSim.agent.Citizen;
import edu.sru.dsm1015.TrafficSim.engine.SimEngine;
import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.grid.RoadCell;
import edu.sru.dsm1015.TrafficSim.grid.ZoneCell;
import java.awt.Color;
import java.awt.Component;

/*
 * Douglas Maxwell
 * A01341863
 */

/*
 * TrafficSim - GUI
 * edu.sru.dsm1015.TrafficSim.gui.GUI
 * This class represents the Graphical User Interface for the TrafficSim program. It manages the display of the grid, 
 * menu options, and overlays for traffic density and attraction rate. It also handles user interactions such as 
 * cell selection, adding/removing roads or zones, managing citizens, and controlling the simulation.
 */

public class GUI extends JFrame {

	// Grid and panel fields
	protected Grid grid;
	private JPanel gPanel;
	private JPanel combinedPanel;
	private JLayeredPane lPane;
	private JLayeredPane circleLayer;

	// Overlay fields
	private TrafficDensityOverlay trafficOverlay;
	private AttractionOverlay attractionOverlay;

	// Menu fields
	private JMenuBar menuBar;
	private JMenu gridMenu, cellMenu, zoneSubMenu, citizenMenu, searchAlgorithmMenu, playMenu, overlayMenu;
	private JMenuItem roadMenuItem, zoneItem1, zoneItem2, zoneItem3, clearItem, addCitizen, fillCitizens, playItem, stopItem, viewItem, updateGridItem;
	private AbstractButton clearGridItem;
	private ButtonGroup searchAlgorithmGroup;
	private JRadioButtonMenuItem BFSItem, DFSItem, aStarItem;
	private JCheckBoxMenuItem TrafficDensityItem, AttractionItem, CitizenItem;

	// Traffic density submenu fields
	private JMenu trafficDensitySubMenu;
	private JRadioButtonMenuItem noneTrafficDensityItem;
	private JRadioButtonMenuItem currentTrafficDensityItem;
	private JRadioButtonMenuItem historicalTrafficDensityItem;
	private ButtonGroup trafficDensityGroup;

	// Selection and dialog fields
	private JLabel selectionIndicator;
	private JDialog cellInfoDialog;

	// SimEngine field
	private SimEngine engine;

	// Other fields
	private String currentSelection;
	

	
    
	public GUI(Grid grid) {
        this.grid = grid;
        // Set up the main window
        setTitle("Traffic Sim");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 1000));
        

        // Create a layered pane to hold the grid display and the circles
        this.lPane = new JLayeredPane();
        lPane.setLayout(null);
        getContentPane().add(lPane, BorderLayout.CENTER);
        
        // Create a layer to hold the circles
        this.circleLayer = new JLayeredPane();
        circleLayer.setBounds(0, 0, 900, 900);
        circleLayer.setBackground(Color.blue);
        circleLayer.setLayout(null);
        lPane.add(circleLayer, JLayeredPane.PALETTE_LAYER);
        
        // Create a panel to hold the grid display
        this.gPanel = new JPanel();
        gPanel.setLayout(new GridLayout(grid.getWidth(), grid.getHeight(), 0, 0));
        gPanel.setBackground(new Color(40, 122, 23));
        gPanel.setPreferredSize(new Dimension(900,900));
        gPanel.setBounds(0,0,900,900);
        
        this.combinedPanel = new JPanel();
        combinedPanel.setLayout(new OverlayLayout(combinedPanel));
        combinedPanel.setBounds(0, 0, 900, 900);
        lPane.add(combinedPanel, JLayeredPane.DEFAULT_LAYER);

        //traffic overlay layer
        this.trafficOverlay = new TrafficDensityOverlay(grid);
        trafficOverlay.setBounds(0, 0, lPane.getWidth(), lPane.getHeight());
        lPane.add(trafficOverlay, JLayeredPane.MODAL_LAYER); // Add it above the default layer
        
        // Destination overlay layer
        this.attractionOverlay = new AttractionOverlay(grid);
        attractionOverlay.setBounds(0, 0, lPane.getWidth(), lPane.getHeight());
        lPane.add(attractionOverlay, JLayeredPane.MODAL_LAYER); // Add it above the default layer
        
        // Initialize selection indicator
        selectionIndicator = new JLabel("Current selection: None");
        selectionIndicator.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(selectionIndicator, BorderLayout.NORTH);
        
        //initialize cell info dialog
        this.cellInfoDialog = new JDialog(this, "Cell Information", false);
        cellInfoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        combinedPanel.add(trafficOverlay);
        combinedPanel.add(attractionOverlay);
        combinedPanel.add(gPanel);
        
        // Add each cell to the panel
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                Cell cell = grid.getCell(i, j);
                Coordinate cellCoordinates = cell.getCoordinates();
                JButton button = new JButton();
                button.setBounds(i*100,j*100,100,100);
                button.setBackground(cell.getColor());
                cell.setButton(button);
                // Add a mouse adapter for changing cell types on click and drag
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleCellChange(cellCoordinates);
                    }
                });

                button.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        JButton sourceButton = (JButton) e.getSource();
                        Point locationOnScreen = e.getLocationOnScreen();
                        SwingUtilities.convertPointFromScreen(locationOnScreen, gPanel);
                        Component targetComponent = SwingUtilities.getDeepestComponentAt(gPanel, locationOnScreen.x, locationOnScreen.y);
                        if (targetComponent != null && targetComponent instanceof JButton && targetComponent != sourceButton) {
                            JButton targetButton = (JButton) targetComponent;
                            Coordinate targetCellCoordinates = getCellCoordinatesFromButton(targetButton);
                            if (targetCellCoordinates != null) {
                                handleCellChange(targetCellCoordinates);
                            }
                        }
                    }
                });
                gPanel.add(button);
            }
        }
        

        // initialize current selection
        this.currentSelection = "none";

        // initialize engine
        this.engine = new SimEngine(this.grid, this.circleLayer, this.trafficOverlay, this.attractionOverlay);
        
        // add menus ***
        this.menuBar = new JMenuBar();
        
        // Grid Menu
        gridMenu = new JMenu("Grid");
        menuBar.add(gridMenu);
        updateGridItem = new JMenuItem("Update Grid");
        gridMenu.add(updateGridItem);
        updateGridItem.addActionListener(e -> {
            showUpdateGridDialog();
        });
        clearGridItem = new JMenuItem("Clear Grid");
        gridMenu.add(clearGridItem);
        clearGridItem.addActionListener(e -> {
            clearGrid();
        });
        
        //Cell Menu
        cellMenu = new JMenu("Cell");
        zoneSubMenu = new JMenu("Change Zone");
        roadMenuItem = new JMenuItem("Add Road");
        roadMenuItem.addActionListener(e -> {
            updateSelectionIndicator("road");
        });
        zoneItem1 = new JMenuItem("Commercial");
        zoneItem1.addActionListener(e -> {
        	updateSelectionIndicator("commercial");
        });
        zoneItem2 = new JMenuItem("Residential");
        zoneItem2.addActionListener(e -> {
        	updateSelectionIndicator("residential");
        });
        zoneItem3 = new JMenuItem("Offices");
        zoneItem3.addActionListener(e -> {
        	updateSelectionIndicator("offices");
        });
        zoneSubMenu.add(zoneItem1);
        zoneSubMenu.add(zoneItem2);
        zoneSubMenu.add(zoneItem3);
        clearItem = new JMenuItem("Remove");
        clearItem.addActionListener(e -> {
        	updateSelectionIndicator("none");
        });
        cellMenu.add(roadMenuItem);
        cellMenu.add(zoneSubMenu);
        cellMenu.add(clearItem);
        menuBar.add(cellMenu);
        
        // Citizen Menu
        citizenMenu = new JMenu("Citizens");

        // Add Citizen
        addCitizen = new JMenuItem("Add Citizen");
        addCitizen.addActionListener(e -> {
            updateSelectionIndicator("addCitizen");
        });
        citizenMenu.add(addCitizen);
        
        // Fill Citizen
        fillCitizens = new JMenuItem("Fill Citizens");
        fillCitizens.addActionListener(e -> {
            fillCitizens();
        });
        citizenMenu.add(fillCitizens);

        // Search Algorithm submenu
        searchAlgorithmMenu = new JMenu("Search Algorithm");
        searchAlgorithmGroup = new ButtonGroup();
        
        //A* search
        aStarItem = new JRadioButtonMenuItem("A* Search");
        aStarItem.addActionListener(e -> {
            this.grid.setSearchAlgorithm("astar");
        });
        searchAlgorithmGroup.add(aStarItem);
        searchAlgorithmMenu.add(aStarItem);

        // Breadth First Search
        BFSItem = new JRadioButtonMenuItem("Breadth First Search");
        BFSItem.addActionListener(e -> {
            this.grid.setSearchAlgorithm("bfs");
        });
        BFSItem.setSelected(true); // Set BFS as the default selected option
        searchAlgorithmGroup.add(BFSItem);
        searchAlgorithmMenu.add(BFSItem);

        // Depth First Search
        DFSItem = new JRadioButtonMenuItem("Depth First Search");
        DFSItem.addActionListener(e -> {
            this.grid.setSearchAlgorithm("dfs");
        });
        searchAlgorithmGroup.add(DFSItem);
        searchAlgorithmMenu.add(DFSItem);

        // Add Search Algorithm submenu to Citizen menu
        citizenMenu.add(searchAlgorithmMenu);

        menuBar.add(citizenMenu);
        
        //Play
        playMenu = new JMenu("Play");
        playItem = new JMenuItem("Start");
        playItem.addActionListener(e -> {
            this.engine.start();
        });
        stopItem = new JMenuItem("Stop");
        stopItem.addActionListener(e -> {
            this.engine.stop();
        });
        viewItem = new JMenuItem("View Cell");
        viewItem.addActionListener(e -> {
            updateSelectionIndicator("viewCell");
        });
        
        
        // Overlay Menu
        overlayMenu = new JMenu("Toggle Overlays");
        menuBar.add(overlayMenu);

        // Traffic Density Overlay
        trafficDensitySubMenu = new JMenu("Traffic Density");

        trafficDensityGroup = new ButtonGroup();

        noneTrafficDensityItem = new JRadioButtonMenuItem("None");
        noneTrafficDensityItem.addActionListener(e -> {
        	this.trafficOverlay.setVisibility(false);
        });
        trafficDensityGroup.add(noneTrafficDensityItem);
        trafficDensitySubMenu.add(noneTrafficDensityItem);

        currentTrafficDensityItem = new JRadioButtonMenuItem("Current");
        currentTrafficDensityItem.addActionListener(e -> {
            this.trafficOverlay.setVisibility(true);
            this.trafficOverlay.setHistoricalFlag(false);
        });
        trafficDensityGroup.add(currentTrafficDensityItem);
        trafficDensitySubMenu.add(currentTrafficDensityItem);

        historicalTrafficDensityItem = new JRadioButtonMenuItem("Historical");
        historicalTrafficDensityItem.addActionListener(e -> {
        	this.trafficOverlay.setVisibility(true);
            this.trafficOverlay.setHistoricalFlag(true);
        });
        trafficDensityGroup.add(historicalTrafficDensityItem);
        trafficDensitySubMenu.add(historicalTrafficDensityItem);

        // Set the default option to be "None"
        noneTrafficDensityItem.setSelected(true);

        overlayMenu.add(trafficDensitySubMenu);
        

        // Attraction Rate Overlay
        AttractionItem = new JCheckBoxMenuItem("Attraction Rate");
        AttractionItem.addActionListener(e -> {
            this.attractionOverlay.toggleAttractionVisibility();
        });
        overlayMenu.add(AttractionItem);

        // Citizens Overlay
        CitizenItem = new JCheckBoxMenuItem("Citizens");
        CitizenItem.setSelected(true); // Set Citizens as the default selected option
        CitizenItem.addActionListener(e -> {
            this.circleLayer.setVisible(CitizenItem.isSelected());
        });
        overlayMenu.add(CitizenItem);
        
        playMenu.add(playItem);
        playMenu.add(stopItem);
        playMenu.add(viewItem);
        menuBar.add(playMenu);
        
        setJMenuBar(menuBar);


        // Pack the window and display it
        pack();
        setVisible(true);     
    }
	
	private void handleCellChange(Coordinate cellCoordinates) {
		if (currentSelection.equals("viewCell")) {
			Cell cell = grid.getCell(cellCoordinates);

	        if (cellInfoDialog.isVisible()) {
	            cellInfoDialog.dispose();
	        }

	        JTextArea cellInfoTextArea = new JTextArea(cell.toString());
	        cellInfoTextArea.setEditable(false);
	        cellInfoDialog.getContentPane().add(new JScrollPane(cellInfoTextArea));
	        cellInfoDialog.setSize(300, 200);
	        cellInfoDialog.setVisible(true);

	        // Update the JDialog content periodically using a javax.swing.Timer
	        Timer timer = new Timer(1000, e -> {
	            cellInfoTextArea.setText(cell.toString());
	        });
	        timer.setRepeats(true);
	        timer.start();

	        cellInfoDialog.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                timer.stop();
	            }
	        });
	    } else {
		    changeCell(cellCoordinates);
		    this.engine.setGrid(this.grid); //update engine's grid
	    }
	}
	
	private void showUpdateGridDialog() {
	    JDialog updateGridDialog = new JDialog(this, "Update Grid", true);
	    updateGridDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    updateGridDialog.setLayout(new GridLayout(3, 2));

	    // Input fields for new grid dimensions
	    JLabel widthLabel = new JLabel("Width:");
	    JTextField widthField = new JTextField();
	    JLabel heightLabel = new JLabel("Height:");
	    JTextField heightField = new JTextField();

	    // Add the input fields to the dialog
	    updateGridDialog.add(widthLabel);
	    updateGridDialog.add(widthField);
	    updateGridDialog.add(heightLabel);
	    updateGridDialog.add(heightField);

	    // Confirm button to update the grid
	    JButton confirmButton = new JButton("Update Grid");
	    confirmButton.addActionListener(e -> {
	        int newWidth = Integer.parseInt(widthField.getText());
	        int newHeight = Integer.parseInt(heightField.getText());

	        // Update the grid with new dimensions
	        updateGrid(newWidth, newHeight);

	        // Close the dialog
	        updateGridDialog.dispose();
	    });
	    updateGridDialog.add(confirmButton);

	    // Cancel button to close the dialog without updating the grid
	    JButton cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener(e -> {
	        updateGridDialog.dispose();
	    });
	    updateGridDialog.add(cancelButton);

	    // Pack and display the dialog
	    updateGridDialog.pack();
	    updateGridDialog.setLocationRelativeTo(this);
	    updateGridDialog.setVisible(true);
	}

	
	private void updateGrid(int newWidth, int newHeight) {
		this.engine.stop();
		this.engine.resetEngine();
		
	    // Create a new grid with the new dimensions
	    this.grid = new Grid(newWidth, newHeight);
	    this.grid.initialize();

	    // Update the gPanel
	    gPanel.removeAll();
	    gPanel.setLayout(new GridLayout(grid.getWidth(), grid.getHeight(), 0, 0));
	    gPanel.setPreferredSize(new Dimension(grid.getWidth() * 100, grid.getHeight() * 100));
	    
	    circleLayer.removeAll();

	    // Add cells to the updated gPanel
	    // (reuse the code for adding cells to the panel from the GUI constructor)
	    for (int i = 0; i < grid.getWidth(); i++) {
	        for (int j = 0; j < grid.getHeight(); j++) {
	        	Cell cell = grid.getCell(i, j);
                Coordinate cellCoordinates = cell.getCoordinates();
                JButton button = new JButton();
                button.setBounds(i*100,j*100,100,100);
                button.setBackground(cell.getColor());
                cell.setButton(button);
                // Add a mouse adapter for changing cell types on click and drag
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleCellChange(cellCoordinates);
                    }
                });

                button.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        JButton sourceButton = (JButton) e.getSource();
                        Point locationOnScreen = e.getLocationOnScreen();
                        SwingUtilities.convertPointFromScreen(locationOnScreen, gPanel);
                        Component targetComponent = SwingUtilities.getDeepestComponentAt(gPanel, locationOnScreen.x, locationOnScreen.y);
                        if (targetComponent != null && targetComponent instanceof JButton && targetComponent != sourceButton) {
                            JButton targetButton = (JButton) targetComponent;
                            Coordinate targetCellCoordinates = getCellCoordinatesFromButton(targetButton);
                            if (targetCellCoordinates != null) {
                                handleCellChange(targetCellCoordinates);
                            }
                        }
                    }
                });
                gPanel.add(button);
	        }
	    }

	    // Update the traffic and attraction overlays
	    trafficOverlay.setGrid(grid);
	    attractionOverlay.setGrid(grid);
	    engine.setGrid(grid);
	    // Repaint the window and pack its contents
	    revalidate();
	    repaint();
	    pack();
	}
	
	private void clearGrid() {
		this.engine.stop();
		this.engine.resetEngine();
		
	    // Loop through each cell in the grid
		grid.reset();
		grid.initialize();
		updateGrid(this.grid.getWidth(), grid.getHeight());
	}


	
	private Coordinate getCellCoordinatesFromButton(JButton button) {
	    for (int i = 0; i < grid.getWidth(); i++) {
	        for (int j = 0; j < grid.getHeight(); j++) {
	            Cell cell = grid.getCell(i, j);
	            if (cell.getButton() == button) {
	                return cell.getCoordinates();
	            }
	        }
	    }
	    return null;
	}

	
	private void updateSelectionIndicator(String text) {
		this.currentSelection = text;
	    selectionIndicator.setText("Current selection: " + currentSelection);
	}
	
	public void upgradeCellToRoad(Cell cell){

        if (cell instanceof RoadCell){
            return;
        }
        // Update the button background color to reflect the new cell type
        JButton button = (JButton) gPanel.getComponent(cell.getCoordinates().getX() * grid.getHeight() + cell.getCoordinates().getY());
        
        // Create a new RoadCell with the same coordinates as the current cell
        RoadCell roadCell = new RoadCell(cell.getCoordinates());
        button.setBackground(roadCell.getColor());
        roadCell.setButton(button);

        // Replace the current cell with the RoadCell in the Grid
        grid.setCell(cell.getCoordinates().getX(), cell.getCoordinates().getY(), roadCell);

    }

    public void upgradeCellToZone(Cell cell, String zoneType){

    	JButton button = (JButton) gPanel.getComponent(cell.getCoordinates().getX() * grid.getHeight() + cell.getCoordinates().getY());
    	
        // Create a new RoadCell with the same coordinates as the current cell
        ZoneCell zoneCell = new ZoneCell(cell.getCoordinates(), zoneType);
        button.setBackground(zoneCell.getColor());
        zoneCell.setButton(button);
 
        // Replace the current cell with the RoadCell in the Grid
        grid.setCell(cell.getCoordinates().getX(), cell.getCoordinates().getY(), zoneCell);
        
    }

    public void clearCell(Cell cell){

        // Create a new RoadCell with the same coordinates as the current cell
        Cell newCell = new Cell(cell.getCoordinates());

        // Replace the current cell with the RoadCell in the Grid
        grid.setCell(cell.getCoordinates().getX(), cell.getCoordinates().getY(), newCell);

        // Update the button background color to reflect the new cell type
        JButton button = (JButton) gPanel.getComponent(cell.getCoordinates().getX() * grid.getHeight() + cell.getCoordinates().getY());
        button.setBackground(newCell.getColor());
    }
    
    public void addCitizen(Cell cell) {
    	
    	Citizen citizen = new Citizen(this.grid, cell, circleLayer);
    	cell.addCitizen(citizen);
        circleLayer.add(citizen.getCircle(), JLayeredPane.PALETTE_LAYER);
        this.engine.resetCitizens();
        
    }
    
    public void fillCitizens() {
    	 for (int i = 0; i < Grid.getWidth(); i++) {
    	        for (int j = 0; j < Grid.getHeight(); j++) {
    	            Cell cell = grid.getCell(i, j);
    	            if (cell instanceof ZoneCell) {
    	            	if(((ZoneCell) cell).getZoneType() == "residential") {
    	            		Citizen citizen = new Citizen(this.grid, cell, circleLayer);
    	                	cell.addCitizen(citizen);
    	                    circleLayer.add(citizen.getCircle(), JLayeredPane.PALETTE_LAYER);
    	            	}
    	            }
    	        }
    	    }
    	 this.engine.resetCitizens();
    }
   

    public void changeCell(Coordinate cellCoordinates){
    	Cell cell = grid.getCell(cellCoordinates);
        switch (this.currentSelection){
            case "none":
                clearCell(cell);
                break;
            case "road":
                upgradeCellToRoad(cell);
                break;
            case "residential":
                upgradeCellToZone(cell, "residential");
                break;
            case "commercial":
                upgradeCellToZone(cell, "commercial");
                break;
            case "offices":
                upgradeCellToZone(cell, "offices");
                break;
            case "addCitizen":
            	if (cell instanceof ZoneCell && ((ZoneCell) cell).getZoneType().equals("residential")) {
                    addCitizen(cell);
                } else {
                    JOptionPane.showMessageDialog(this, "Citizens can only be added to residential cells.");
                }
      
        }
    }
    
    
    /**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Create a grid
			        Grid grid = new Grid(20, 20);
			        grid.initialize();

			        // Create the GUI and display the grid
			        GUI gui = new GUI(grid);
			        
			        
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
