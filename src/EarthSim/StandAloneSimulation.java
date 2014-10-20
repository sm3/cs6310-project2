package EarthSim;

import java.util.concurrent.BlockingQueue;

public class StandAloneSimulation extends HeatedEarthSimulation {

	
	
	public StandAloneSimulation(int gs, int interval,
			BlockingQueue<Message> queue) {
		super(gs, interval, queue);
		// TODO Auto-generated constructor stub
	}

	
	public static void main(String[] args) {
	
		
		StandAloneSimulation sim = new StandAloneSimulation(15, 15, null);
		
		diffuse(gridcellsSurface1, gridcellsSurface2);
			
		GridCell [][] temp = gridcellsSurface1;
		
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
		

		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			System.out.println(" ");
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				System.out.print(gridcellsSurface1[i][j].getTemp() + " ");
			}
		}
		
		System.out.println(" ");
		System.out.println(" ");
		
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			System.out.println(" ");
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				System.out.print(gridcellsSurface1[i][j].getLatitude() +", " + gridcellsSurface1[i][j].getLongtitude() + " ; ");
			}
		}
		
	}

}
