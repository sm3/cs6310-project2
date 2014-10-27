
package EarthSim;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private final static Logger LOGGER = Logger.getLogger(HeatedEarthSimulation.class.getName()); 
	
	public HeatedEarthSimulation(int gs, int interval, BlockingQueue<Message> queue)
	{
		 this.queue=queue;
		 this.gridSize=gs;
		 timeInterval = interval;
		 earthRepresentation = new EarthRepresentation(gs, interval);
		 gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		 gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
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
		timeOfDay=720;
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
		grid[i][j] = cell;
		

	}
	 

	public void setPresentation(HeatedEarthPresentation p)
	{
		presentation = p;
	}
	public void update(){
		System.out.println("Simulation updating.");

		this.rotateEarth();
	}

	public void rotateEarth()
	{
		
		long currentSunLocation = SunRepresentation.sunLocation;
		
		this.diffuse(gridcellsSurface1, gridcellsSurface2);
		
		
		try {
			queue.put(new Message(prepareOutput(gridcellsSurface2), currentSunLocation));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		GridCell [][] temp = gridcellsSurface1;
		
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
		
		
		LOGGER.log(Level.INFO, Analyzer.getMemoryStats());
		
		
	}

	
	@Override
	public void run() {

		running=true;
		paused = false;
		while(running){
			while(!paused){
			
		
				this.rotateEarth();
				if(presentation!=null){
					System.out.println("Simulation update");
					presentation.update();
				}
			
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
				
			}
		}
		
		
		return gridOutput;
		
	}
	
	
	
	protected void diffuse(GridCell[][] grid1, GridCell[][] grid2)
	{
		
		earthRepresentation.calculateAverageTemperature(grid1);
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		

				grid2[i][j].setTemp(earthRepresentation.calculateCellTemperature(grid1[i][j]));
	
			}
		}
		
		//advance sun according to interval
		timeOfDay = (timeOfDay +  timeInterval) % 1440;
		SunRepresentation.sunLocation = 180-(timeOfDay/4 );
		
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	public void setTimeStep(Integer interval) {
		 timeInterval = interval;
		
	}
	
}
