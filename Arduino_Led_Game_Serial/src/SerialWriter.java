import java.io.IOException;
import java.io.OutputStream;


public class SerialWriter implements Runnable{

	 	OutputStream out;
	 	String output;
	 
	    public SerialWriter(OutputStream i, String outString) {
	    	this.out = i;
	    	this.output = outString;
	    }

		@Override
		public void run() {
			try {
				out.write(output.getBytes());
			} catch (IOException e) {
				System.out.println("Can't send "+output);
			}
			
		}

}
