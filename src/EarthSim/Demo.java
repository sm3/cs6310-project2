package EarthSim;

public class Demo
{
	private int gridSpacing; //1 to 180- input provided by the user
	private int timeInterval=1; //1 to 1440 - input provided by the user
	
	public static void main(String[] args) {
	
		int blen = 0;
	    boolean  s, p, r, t, b; 
	    s = p = r = t = b = false; 
	   
		 for (int i = 0; i < args.length; i++) {
		        if (args[i].equalsIgnoreCase("-s")) {
		            s = true;
		        } else if (args[i].equalsIgnoreCase("-p")) {
		        	p = true;
		        } else if (args[i].equalsIgnoreCase("-r")) {
		            r = true;
		        } else if (args[i].equalsIgnoreCase("-t")) {
		            t = true;
		        } else if (args[i].equalsIgnoreCase("-b")) {
		            try {
		                blen = Integer.parseInt(args[i+1]);
		                b = true;
		            } catch (NumberFormatException e) {
		                System.out.println("Buffer size should be an integer between 0 and 100");
		                System.exit(1);
		            }
		            if (blen < 0 || blen > 100) {
		                System.out.println("Buffer size must be between 0 and 100");
		                System.exit(1);
		            }
		            
		        }
		    }
    
     //if -b option is not specified treat it as -b 1
     if (b == false)
     {
    	 blen =1;
     }
     //add code to call simulation
     if(r && t){
    	 System.out.println("Must specify only one component to have initiative.");
         System.exit(1);
     }
     String initiative ="";
     if(r)
    	 initiative="P";
     else if(t)
    	 initiative="S";
     else
    	 initiative="G";
    	 
     HeatedEarthGUI gui = new HeatedEarthGUI(p, s, initiative, blen);
     gui.displayGui();
	 }
     
     
 


}