package EarthSim;

public class Demo
{
	
	public static void main(String[] args) {
	
		int blen = 0;
	    boolean  s, p, r, t, b; 
	    s = p = r = t = b = false; 
	   
		 for (int i = 0; i < args.length-1; i++) {
		        if (args[i].equals("-s")) {
		            s = true;
		        } else if (args[i].equals("-p")) {
		        	p = true;
		        } else if (args[i].equals("-r")) {
		            r = true;
		        } else if (args[i].equals("-t")) {
		            t = true;
		        } else if (args[i].equals("-b")) {
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
     
	 }
     
     
 


}