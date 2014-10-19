/**
 * Created by Subha Melapalayam
 */
package EarthSim;
import java.util.concurrent.BlockingQueue;

public class HeatedEarthSimulation implements Runnable
{
	GridCell[][] gridcellsSurface1;
	GridCell[][] gridcellsSurface2;
	private BlockingQueue<Message> queue;
	
	int timeInterval=0;
	EarthRepresentation earthRepresentation;
	GridCell gc;
	private boolean running; //copied this from TestSimulator
	
	
	public HeatedEarthSimulation(int gs, int interval, BlockingQueue<Message> queue)
	{
		 this.queue=queue;
		 
		 timeInterval = interval;
		 earthRepresentation = new EarthRepresentation(gs);
		 gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		 gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation.getCols()];
		
		 Initialize();
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
		 
		 //TODO: set previous temperature of the cell, current temperature, and neighbors.
		
		grid[i][j] = cell;
	}
	 
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Add code to compute diffusion
		// What is the stabilization criteria?
		
		while(running){
			
			
			
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
	
	
	private void diffuse(GridCell[][] grid1, GridCell[][] grid2)
	{
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		

				grid2[i][j].setTemp(earthRepresentation.calculateCellTemperature(grid1[i][j]));
				
			}
		}
		
		SunRepresentation.sunLocation = SunRepresentation.sunLocation + 15;
		
		if (SunRepresentation.sunLocation > 180)
		{
			SunRepresentation.sunLocation = 0;
		}
		
	}
	
	//copied this from TestSimulator
	public void setRunning(boolean running){
		this.running=running;
	}
	
}