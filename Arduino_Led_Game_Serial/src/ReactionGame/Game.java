package ReactionGame;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Game implements Runnable {

	// Constants:
	private static int Testloops = 25;

	// Variables:
	private int resault_RightRate;
	private int resault_Quickest;
	private int resault_Slowest;
	private int resault_AverageTime;
	private long[] resaults;
	private int rightNumber;
	private boolean finishedTF;

	// Objects from upper source:
	private GameLog log;
	private GameKeylistener keylistener;
	private OutputStream outStream;
	private JButton StartStop;
	private JLabel loopRound;

	public Game(OutputStream outStream, GameKeylistener keylistener, JButton StartStopFromUi,JLabel loopRound,GameLog log) {

		// Getting objects:
		this.log = log;
		this.loopRound = loopRound;
		this.keylistener = keylistener;
		this.outStream = outStream;
		this.StartStop = StartStopFromUi;

		// Reseting Values:
		this.resault_RightRate = 0;
		this.resault_Quickest = 0;
		this.resault_Slowest = 0;
		this.resault_AverageTime = 0;
		this.rightNumber=0;
		this.resaults = new long[Testloops];

	}
	
	public void reset(){
		sendSerial(0,0,0,0);
		this.resault_RightRate = 0;
		this.resault_Quickest = 0;
		this.resault_Slowest = 0;
		this.resault_AverageTime = 0;
		this.rightNumber=0;
		this.resaults = new long[Testloops];
		this.loopRound.setText("  Round: 0");
	}

	public void run() {
		try {
			ledsBlink(500, 500, 3);
			rightNumber = 0;
			for (int i = 0; i < Testloops; i++) {
				this.loopRound.setText("  Round: "+(i+1));
				Thread.sleep(500);
				int k = new Random().nextInt((4 - 1) + 1) + 1;
				switch (k) {
				case 1:
					sendSerial(1, 0, 0, 0);
					break;
				case 2:
					sendSerial(0, 1, 0, 0);
					break;
				case 3:
					sendSerial(0, 0, 1, 0);
					break;
				case 4:
					sendSerial(0, 0, 0, 1);
					break;
				}
				long starttime = new Date().getTime();
				long stoptime = new Date().getTime();
				resaults[i] = stoptime - starttime;
				boolean pressedTF = false;
				keylistener.resetKeyRecords();
				//while (!keylistener.ifLastPressedTF()||resaults[i] < 5000) {
				while (pressedTF == false) {
					Thread.yield();
					pressedTF  = keylistener.ifLastPressedTF();
					stoptime = new Date().getTime();
					resaults[i] = stoptime - starttime;
				}
				
				int keycode = keylistener.getLastPressedKey();
				if (keycode == (k + 48)){rightNumber++;}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendSerial(0, 0, 0, 0);
		calculateSpeed(resaults);
		StartStop.setText("Calculate");
	}

	public void calculateSpeed(long[] resault){
		long calculatResault[] = resault;
		
		// Calculate quickest Reaction:
		long slowest = 0;
		for(int i =0; i<calculatResault.length;i++){
			if(calculatResault[i]> slowest){
				slowest=calculatResault[i];
			}
		}
		resault_Slowest = (int) slowest;
		
		// Calculate slowest Reaction:
		long quickest = calculatResault[0];
		for(int i =0; i<calculatResault.length;i++){
			if(calculatResault[i]< quickest){
				quickest=calculatResault[i];
			}
		}
		resault_Quickest = (int) quickest;
		
		// Calculate average Time:
		long sum =0;
		for(int i =0; i<calculatResault.length;i++){
			sum = sum + calculatResault[i];
		}
		resault_AverageTime = (int)sum/calculatResault.length;
		
		// Calculate Accurace:
		resault_RightRate = (rightNumber*100)/calculatResault.length;
	}
	
	public void sendSerial(int a, int b, int c, int d) {
		String out = "a" + a + "b" + b + "c" + c + "d" + d + "#";
		try {
			outStream.write(out.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.writeTextArea("Serial Output: " + out);
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

	public boolean checkFinished() {
		return finishedTF;
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
}