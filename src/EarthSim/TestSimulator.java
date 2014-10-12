package EarthSim;

import java.util.concurrent.BlockingQueue;

public class TestSimulator {

	public TestSimulator(int dim, BlockingQueue<Message> queue) {
			this.dimension = dim;
			this.queue=queue;

			counter = 0;
		}
	public void setTimeStep(Integer timeStep){
		this.timeStep=timeStep;
	}
		private Integer sunsLongitude=0;
		private boolean running;
		private BlockingQueue<Message> queue;
		private Integer timeStep;
		private double[][] oldPlate;
		private double[][] newPlate;
		private int dimension;
		private int counter;

		// Simulates the diffusion of heat throught the plate
		public void simulate() {
			oldPlate = new double[dimension][dimension];
			newPlate = new double[dimension][dimension];

			// Initialize the temperatures of the edge values and the plate itself
			initialize(oldPlate, 30, 100, 10, 90);
			initialize(newPlate, 30, 100, 10, 90);
			running=true;
			// Loop until exit criteria are met, updating each newPlate cell from
			// the
			// average temperatures of the corresponding neighbors in oldPlate
			do {
				counter++;
				for (int i = 1; i <= dimension - 2; i++) {
					for (int j = 1; j <= dimension - 2; j++) {
						//updateGridCell(i, j, newPlate[i][j]);
						newPlate[i][j] = (oldPlate[i + 1][j] + oldPlate[i - 1][j]
								+ oldPlate[i][j + 1] + oldPlate[i][j - 1]) / 4.0;

					}
				}
				// Swap the plates and continue
				swap(oldPlate, newPlate);
				try {
					sunsLongitude=(sunsLongitude-1);
					if(sunsLongitude<-180)
						sunsLongitude=180;
					Message update =new Message(oldPlate,new Long(sunsLongitude));
					queue.put(update);
					Thread.sleep(timeStep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} while (!done() && running);
		}
		public void setRunning(boolean running){
			this.running=running;
		}
		// Initializes starting temperatures on the edges of the plate
		protected void initialize(double[][] plate, int top, int bot, int left,
				int right) {
			// initialize the top temp
			for (int i = 0; i < dimension; i++) {
				if (i == 0) {
					plate[0][i] = (top+left)/2;
				}else if(i == dimension - 1){
					plate[0][i] = (top+right)/2;
				}else{
					plate[0][i] = top;
				}
			}
			// initialize the bottom
			for (int i = 0; i < dimension; i++) {
				if (i == 0) {
					plate[dimension - 1][i] =  (bot+left)/2;
				}else if(i == dimension - 1){
					plate[dimension - 1][i] =  (bot+right)/2;
				}else{
					plate[dimension - 1][i] = bot;
				}
			}
			// initialize the left
			for (int i = 0; i < dimension; i++) {
				if (i == 0) {
					plate[i][0] =  (left+top)/2;
				}else if(i == dimension - 1){
					plate[i][0] =  (left+bot)/2;
				}else{
					plate[i][0] = left;
				}
			}
			// initialize right
			for (int i = 0; i < dimension; i++) {
				if (i == 0) {
					plate[i][dimension - 1] =  (right+top)/2;
				}else if(i == dimension - 1){
					plate[i][dimension - 1] =  (right+bot)/2;
				}else{
					plate[i][dimension - 1] = right;
				}
			}

		}

		// Switches the old plate with the new plate
		protected void swap(double[][] oldPlate, double[][] newPlate) {
			double[][] tempPlate = oldPlate;
			
			this.oldPlate = newPlate;
			this.newPlate = tempPlate;
		
		}

		// prints out the plate
		public void printResults() {
			System.out.println();
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					System.out.printf("[%.1f]", oldPlate[i][j]);
				}
				System.out.println();
			}
		}

		// returns true if counter is over 3000 or the change in values was
		// negligible last iteration
		public boolean done() {
			if (counter > 30000) {
				return true;
			}
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					if (oldPlate[i][j] < newPlate[i][j] - .001
							|| oldPlate[i][j] > newPlate[i][j] + .001) {
						return false;
					}
				}
			}
			return true;
		}

	        public StringBuffer exportResults()
	        {
	            StringBuffer ret = new StringBuffer();
	            ret.append("\n");
	            for (int i = 1; i < dimension - 1; i++)
	            {
	                for (int j = 1; j < dimension - 1; j++)
	                {
	                    ret.append(String.format("[%.2f]", oldPlate[i][j]));
	                }
	                ret.append("\n");
	            }
	            return ret;
	        }
	}
