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
	private Dimension size;
	private int lon= 360;
	private int lat = 180;
	private boolean testing;
	private Integer sunsLongitude=0;

	private static final long serialVersionUID = 1L;
	private String path="images/worldmap.png";
	
	public HeatedEarthPresentation(int gridSpacing, BlockingQueue<Message> queue, JTextField displayRate,boolean paused){
		super();
		this.queue=queue;
		this.displayRate=displayRate;
		this.gridSize=gridSpacing;
		this.paused=paused;
		

		img = new ImageIcon(path).getImage(); 
		  size = new Dimension(img.getWidth(null), img.getHeight(null));
		    setPreferredSize(size);
		    setMinimumSize(size);
		    setMaximumSize(size);
		    setSize(size);
		    setLayout(null);
	 }
	public void setTesting(boolean testing){
		this.testing=testing;
	}
	public void setGridSize(int gridSize){
		this.gridSize=gridSize;
		initGrid(gridSize);
	}
	public void setPaused(boolean paused){
		this.paused=paused;
	}
	public void setRunning(boolean running){
		this.running = running;
	}
	 private void initGrid(int gridSpacing) {
		 if(testing)
			grid = new double[gridSize][gridSize];
		 else
			 grid = new double[lon/gridSpacing][lat/gridSpacing];
		for(int i=0; i<grid.length; i++){
			for(int j=0; j<grid[0].length; j++){
				grid[i][j]=0;
			}
			
		}
		
	}
	 
	 //Starts the presentation updating
	 public void run(){
		 initGrid(gridSize);
		 running=true;
		 paused=false;
		 while(running){
			if(!paused){
			try {
				Message update=queue.take();
				grid =update.getGrid();
				sunsLongitude=update.getSunsLongitude().intValue();
				this.repaint();
				this.revalidate();
			    Thread.sleep(Integer.valueOf(displayRate.getText()));   
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			}
		 
		 }
	 }
	 public void update(){
		 try {
				Message update=queue.take();
				grid =update.getGrid();
				this.repaint();
				this.revalidate();  
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
	 }
	 
	 
     //draws the grid everytime repaint() or revalidate() are called on this obect
     public void paintComponent(Graphics g){
    	 super.paintComponent(g);
    	 g.drawImage(img, 0, 0, null);
         Graphics2D g2d = (Graphics2D)g;
         if(gridSize!=0){
	         Long height=new Long(size.height)/new Long(grid[0].length);
	         Long width= new Long(size.width)/new Long(grid.length);
	         for(int i=0; i<grid.length; i++){
	 			for(int j=0; j<grid[0].length; j++){
	 				Double v = (grid[i][j]-100)/100*(-1);
	 				Color c = Color.getHSBColor(.666f*v.floatValue(),1f,1f);
	 				
	 				g2d.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),120));
	 				
	 				g2d.fillRect(i*width.intValue(), j*height.intValue(), width.intValue(), height.intValue());
	 				g2d.setColor(Color.black);
	 				g2d.draw(new Rectangle(i*width.intValue(), j*height.intValue(), width.intValue(), height.intValue()));
	 			}
	         }
	         g2d.setColor(new Color(255,255,0,100));
	         Long newLong = (long)((((float)sunsLongitude+180)/360)*size.width);
	         System.out.println(newLong);
	         g2d.fillOval(newLong.intValue(),size.height/2,100,100);
         }
     }
	public boolean getRunning() {
		return running;
	}
}
