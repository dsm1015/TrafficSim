package edu.sru.dsm1015.TrafficSim.engine;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLayeredPane;
import javax.swing.Timer;

import edu.sru.dsm1015.TrafficSim.agent.Citizen;
import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.gui.AttractionOverlay;
import edu.sru.dsm1015.TrafficSim.gui.TrafficDensityOverlay;

/*
 * TrafficSim - SimEngine
 * edu.sru.dsm1015.TrafficSim.engine.SimEngine
 * The main simulation engine responsible for controlling the simulation, 
 * managing citizens, and updating overlays.
 */

public class SimEngine {
 
	private int DELAY = 500; // delay between steps in ms
	
	private boolean isOn; //is the engine running
	private Grid grid;
	private Timer timer = null;
	private TrafficDensityOverlay trafficOverlay;
	private AttractionOverlay attractionOverlay;
	protected JLayeredPane circleLayer;
	protected Set<Citizen> citizens;
	
	
	public SimEngine(Grid grid, JLayeredPane circleLayer, TrafficDensityOverlay trafficOverlay, AttractionOverlay waypointOverlay) {
		this.isOn = false;
		this.grid = grid;
		this.circleLayer = circleLayer;
		this.trafficOverlay = trafficOverlay;
		this.attractionOverlay = waypointOverlay;
		this.citizens = new HashSet<Citizen>();
		resetCitizens();
		
		this.timer = new Timer(DELAY, (ActionListener) new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Citizen citizen : citizens) {
					moveCitizens(citizen);
				}
				if(trafficOverlay.isVisible()) {
					trafficOverlay.repaint();
				}
				if(waypointOverlay.isVisible()) {
					waypointOverlay.repaint();
				}
				//circleLayer.paintImmediately(circleLayer.getBounds());
			}
		});
	}
	
	public void start() {
		this.isOn = true;
		this.timer.start();
		//TODO
		//on start, engine should run through the list of citizens and move them accordingly.
		resetCitizens();
	}
	
	public void stop() {
		this.isOn = false;
		this.timer.stop();
		this.trafficOverlay.setVisibility(false);
		this.attractionOverlay.setVisibility(false);
	}
	
	
	public void moveCitizens(Citizen citizen) {
		if(!this.isOn) {
			return;
		}
		
		citizen.move();
	}
	
	public void resetCitizens() {
		for (int i = 0; i < Grid.getWidth(); i++) {
            for (int j = 0; j < Grid.getHeight(); j++) {
                Cell cell = grid.getCell(i, j);
                if(!cell.getCitizens().isEmpty()) {
                	this.citizens.addAll(cell.getCitizens());
                }
            }
        }
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public void resetEngine() {
		this.citizens.clear();
	}
	
	
}
