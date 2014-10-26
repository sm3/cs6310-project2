
package EarthSim;
import java.util.concurrent.BlockingQueue;

public class HeatedEarthSimulation implements Runnable
{
	static GridCell[][] gridcellsSurface1;
	static GridCell[][] gridcellsSurface2;
	private BlockingQueue<Message> queue;
	static int timeInterval=0;
	static int timeOfDay=720;
	private HeatedEarthPresentation presentation=null;
	static EarthRepresentation earthRepresentation;
	GridCell gc;
	int gridSize;
	private boolean running; //copied this from TestSimulator
	private boolean paused;
	
	
	public HeatedEarthSimulation(int gs, int interval, BlockingQueue<Message> queue)
	{
		 this.queue=queue;
		 this.gridSize=gs;
		 timeInterval = interval;
		 earthRepresentation = new EarthRepresentation(gs, interval);
		 gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		 gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
		 Initialize();
		 running = true;
	}
	public void setGridSize(Integer size){
		this.gridSize=size;
	}
	public void setPaused(boolean paused){
		this.paused=paused;
	}
	public void reset(){
		earthRepresentation = new EarthRepresentation(gridSize, timeInterval);
		gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
		Initialize();
		running = true;
		paused = false;
	}

	//Initialize GridCells.
	public void Initialize()
	{
	
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				setGridData(gridcellsSurface1, i, j );
				setGridData(gridcellsSurface2, i, j );	 
			}
		}
		
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				setNeighborsData(gridcellsSurface1, i, j );
				setNeighborsData(gridcellsSurface2, i, j );	 
			}
		}
		

	}
	
	

	private void setNeighborsData(GridCell [][] grid, int i, int j)
	{
		GridCell cell = grid[i][j];
		
		cell.setNeighbors(grid);
	}
	
	private void setGridData(GridCell [][] grid, int i, int j)
	{
		
		GridCell cell =  new GridCell();
		
		
		
		
		cell.setLatitude(earthRepresentation.getOriginLatitude(i));
		cell.setCenterLatitude(earthRepresentation.getOriginLatitude(i)+(gridSize/2));
		cell.setLongtitude(earthRepresentation.getOriginLongtitude(j));
		cell.setLt(earthRepresentation.calcCTop(i));
		cell.setLb(earthRepresentation.calcCBase(i));
		cell.setLv(earthRepresentation.calcCVerticalSide());
		cell.setArea(earthRepresentation.calcCArea(i));
		cell.setPerimeter(earthRepresentation.calcCPerimeter(i));
		cell.setH(earthRepresentation.calcCHeight(i));
		cell.setProportion(earthRepresentation.calcCSurfaceArea(i));
		cell.setxCoordinate(i);
		cell.setyCoordinate(j);
		cell.setTemp(288);
		 
		 //TODO: set previous temperature of the cell, current temperature, and neighbors.
		
		grid[i][j] = cell;
		
		System.out.println("*********************************");
	    System.out.println("gs , rows, cols : " + earthRepresentation.getGS() + " " + earthRepresentation.getRows() + " " + earthRepresentation.getCols());
		System.out.println("Latitude :" + cell.getLatitude());
		System.out.println("Longtitude :" + cell.getLongtitude());
		double lat_att, lon_att;
		lat_att = Math.cos(Math.toRadians(cell.getLatitude()));
		lon_att = Math.cos(Math.toRadians(cell.getLongtitude())) < 0 ?  0 : Math.cos(Math.toRadians(cell.getLongtitude()));
		System.out.println("Lat Attenuation:" + lat_att);
		System.out.println("Lon Attenuation:" + lon_att);
		System.out.println("Total Attenuation:" + lat_att * lon_att);
		System.out.println("top :" + cell.getLt());
		System.out.println("base :" + cell.getLb());
		System.out.println("vertical side :" + cell.getLv());
		System.out.println("area :" + cell.getArea());
		System.out.println("perimeter :" + cell.getPerimeter());
		System.out.println("Height :" + cell.getH());
		System.out.println("Surface area :" + cell.getProportion());
		System.out.println("xCoordinate :" + cell.getxCoordinate());
		System.out.println("yCoordinate :" + cell.getyCoordinate());
		System.out.println("*********************************");
	}
	 

	public void setPresentation(HeatedEarthPresentation p){
		presentation = p;
	}
	public void update(){
		System.out.println("Simulation updating.");
		this.diffuse(gridcellsSurface1, gridcellsSurface2);
		
		
		try {
			queue.put(new Message(prepareOutput(gridcellsSurface2),SunRepresentation.sunLocation));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		GridCell [][] temp = gridcellsSurface1;
		
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		running=true;
		paused = false;
		while(running){
			while(!paused){
			
			
		long currentSunLocation = SunRepresentation.sunLocation;
		
		diffuse(gridcellsSurface1, gridcellsSurface2);
		
			
		try {
			
			queue.put(new Message(prepareOutput(gridcellsSurface2),currentSunLocation));
			if(presentation!=null){
				System.out.println("Presentation update");
				presentation.update();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		GridCell [][] temp = gridcellsSurface1;
		
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
			
			}
		}
		
	}
	
	private double [][] prepareOutput(GridCell [][] grid)
	{
		
		double[][] gridOutput = new double[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		

				gridOutput[i][j] = grid[i][j].getTemp();
				//System.out.println(" temp for grid["+ i + "]["+ j+"]  "+ grid[i][j].getTemp());
				
			}
		}
		
		
		return gridOutput;
		
	}
	
	
	protected static void diffuse(GridCell[][] grid1, GridCell[][] grid2)
	{
		
		earthRepresentation.calculateAverageTemperature(grid1);
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		

				grid2[i][j].setTemp(earthRepresentation.calculateCellTemperature(grid1[i][j]));
				//System.out.println("Diffusion - cell temp :" + grid2[i][j].getTemp());
				
			}
		}
		
		//advance sun according to interval
		timeOfDay = (timeOfDay +  timeInterval) % 1440;
		SunRepresentation.sunLocation = 180-(timeOfDay/4 );
		
//		if (SunRepresentation.sunLocation > 179)
//		{
//			SunRepresentation.sunLocation = SunRepresentation.sunLocation - 360; 
//		}
		
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	
}
