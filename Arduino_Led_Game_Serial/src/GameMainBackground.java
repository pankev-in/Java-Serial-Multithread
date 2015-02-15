
public class GameMainBackground extends Thread {

	public GameMainBackground() {
	}
		
	public void run(){
		try {
	         for(int i = 5; i > 0; i--) {
	            System.out.println("Child Thread: " + i);
	            Thread.sleep(1000);
	         }
	      } catch (InterruptedException e) {
	         System.out.println("Child interrupted.");
	         return;
	      }
	      System.out.println("Exiting child thread.");
	 }
	
	public void gameStart(){
		
	}
	public void gameStop(){
		
	}
	
}
