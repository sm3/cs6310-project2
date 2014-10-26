package EarthSim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HeatedEarthPresentation extends JPanel {

	private double[][] grid;
	private Image img;
	private int gridSize;
	private JTextField displayRate;
	private boolean running;
	private boolean paused;
	private BlockingQueue<Message> queue;
	private HeatedEarthSimulation simulation= null;
	private Long startTime;
	private Dimension size;
	double low=284;
	double high=290;
	private int lon = 360;
	private int lat = 180;
	private boolean testing;
	private Integer sunsLongitude = 0;
	private JLabel time;
	private static final long serialVersionUID = 1L;
	private String path = "images/worldmap.png";
	private Long lastupdate=null;

	public HeatedEarthPresentation(int gridSpacing,
			BlockingQueue<Message> queue, JTextField displayRate, boolean paused) {
		super();
		this.queue = queue;
		this.displayRate = displayRate;
		this.gridSize = gridSpacing;
		this.paused = paused;
		
		img = new ImageIcon(path).getImage();
		size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}
	public void setTime(JLabel time){
		this.time=time;
	}
	public void setTesting(boolean testing) {
		this.testing = testing;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
		
	}
	public void reset(){
		initGrid(gridSize);
	}
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setRunning(boolean running) {

		startTime=(new Date()).getTime();
		this.running = running;
	}

	private void initGrid(int gridSpacing) {
		grid = new double[180 / gridSpacing][360 / gridSpacing];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 0;
			}

		}

	}
	public void setSimulation(HeatedEarthSimulation s){
		simulation = s;
	}
	// Starts the presentation updating
	public void run() {
		initGrid(gridSize);
		running = true;
		paused = false;
		while (running) {
			if (!paused) {
				try {
					if(simulation!=null){
						System.out.println("Simulation update");
						simulation.update();
					}
					Message update = queue.take();
					grid = update.getGrid();
					sunsLongitude = update.getSunsLongitude().intValue();
					this.repaint();
					if(displayRate.getText().equals("")){
						displayRate.setText("1");
					}
					try{
					Thread.currentThread().sleep(Integer.valueOf(displayRate.getText()));
					}catch(NumberFormatException e){
						Thread.currentThread().sleep(1);
						
					}
					
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

		}
	}

	public void update() {
		try {
			Message update = queue.take();
			grid = update.getGrid();
			sunsLongitude = update.getSunsLongitude().intValue();
			this.repaint();
			System.out.println("presentation updating.");
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	// draws the grid everytime repaint() are called on this
	// obect
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		Graphics2D g2d = (Graphics2D) g;
		
		if (gridSize != 0) {
			high-=10;
			low+=10;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					Double value =grid[i][j];
					if(value==0){
						continue;
					}
					
					if(value>high){
						high=value;
					}
					if(value<low){
						low=value;
					}
				}
			}
			Long height = new Long(size.height) / new Long(grid.length);
			Long width = new Long(size.width) / new Long(grid[0].length);
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					Double value =grid[i][j];
					if(value==0){
						continue;
					}
					Double v = (high- grid[i][j]) / (high-low);
					Color c = Color.getHSBColor(.666f * v.floatValue(), 1f, 1f);

					g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 120));

					g2d.fillRect(j * width.intValue(), i * height.intValue(),width.intValue(), height.intValue());
					g2d.setColor(Color.black);
					g2d.draw(new Rectangle(j * width.intValue(), i* height.intValue(), width.intValue(), height.intValue()));
				}
			}
			g2d.setColor(new Color(255, 255, 0, 100));
			Long newLong = (long) ((((float) sunsLongitude + 180) / 360) * size.width);
			g2d.fillOval(newLong.intValue(), (size.height/2)-50, 100, 100);
		}
		if(time!=null && startTime!=null){
		Long runningTime = ((new Date()).getTime() - startTime)/1000;
		 time.setText(runningTime.intValue() +" s");
		 time.repaint();
		}
	}

	public boolean getRunning() {
		return running;
	}
}
