import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

public class Game implements Runnable, KeyListener{

	private int resault_RightRate;
	private int resault_Quickest;
	private int resault_Slowest;
	private int resault_AverageTime;
	private OutputStream outStream;
	private int Testloops = 10;
	private String output;
	private boolean keyPressed;
	private long[] resaults;

	public Game(OutputStream outStream) {
		this.resault_RightRate = 0;
		this.resault_Quickest = 0;
		this.resault_Slowest = 0;
		this.resault_AverageTime = 0;
		this.outStream = outStream;
		this.keyPressed=false;
		this.output="";
		resaults = new long[Testloops];
		
	}

	public void run() {
		try {
			ledsBlink(500, 500, 3);

			for (int i = 0; i < Testloops; i++) {
				Thread.sleep(500);
				int k = new Random().nextInt((4 - 1) + 1) + 1;
				switch(k){
				case 1:sendSerial(1,0,0,0);break;
				case 2:sendSerial(0,1,0,0);break;
				case 3:sendSerial(0,0,1,0);break;
				case 4:sendSerial(0,0,0,1);break;
				}
				long starttime = new Date().getTime();
				long stoptime = new Date().getTime();
				resaults[i] = stoptime - starttime;
				while (resaults[i]<5000){
					stoptime = new Date().getTime();
					resaults[i] = stoptime - starttime;
				}
				System.out.println(resaults[i]);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendSerial(int a, int b, int c, int d) {
		String out = "a" + a + "b" + b + "c" + c + "d" + d + "#";
		try {
			outStream.write(out.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Serial Output: " + out);
	}

	public void ledsBlink(long Ontime, long Offtime, int loops) {
		try {
			for (int i = 0; i < loops; i++) {
				sendSerial(1, 1, 1, 1);
				Thread.sleep(Ontime);
				sendSerial(0, 0, 0, 0);
				Thread.sleep(Offtime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getResault_RightRate() {
		return resault_RightRate;
	}

	public void setResault_RightRate(int resault_RightRate) {
		this.resault_RightRate = resault_RightRate;
	}

	public int getResault_Quickest() {
		return resault_Quickest;
	}

	public void setResault_Quickest(int resault_Quickest) {
		this.resault_Quickest = resault_Quickest;
	}

	public int getResault_Slowest() {
		return resault_Slowest;
	}

	public void setResault_Slowest(int resault_Slowest) {
		this.resault_Slowest = resault_Slowest;
	}

	public int getResault_AverageTime() {
		return resault_AverageTime;
	}

	public void setResault_AverageTime(int resault_AverageTime) {
		this.resault_AverageTime = resault_AverageTime;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar());
		keyPressed=true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}