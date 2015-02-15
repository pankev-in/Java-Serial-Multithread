import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOnUi extends JFrame implements WindowListener, ActionListener,
		KeyListener {

	private JPanel GameOnUiMainPanel;
	private JPanel WestPanel;
	private JPanel CenterPanel;
	private JPanel SouthPanel;
	private Container GameOnUiContainer;
	private BorderLayout GameOnUiLayout;
	private JLabel led1Status;
	private JLabel led2Status;
	private JLabel led3Status;
	private JLabel led4Status;
	private JButton StartStop;
	private JLabel resaultRightRate;
	private JLabel resaultQuickest;
	private JLabel resaultSlowest;
	private JLabel resaultAverageTime;
	private SerialPort serialPort;
	private OutputStream outStream;
	//private InputStream inStream;
	private boolean connectedTF;
	private boolean runningTF;
	private int Testloops = 10;


	public GameOnUi() {

		GameOnUiLayout = new BorderLayout();
		GameOnUiMainPanel = new JPanel();
		GameOnUiContainer = new Container();
		WestPanel = new JPanel();
		CenterPanel = new JPanel();
		SouthPanel = new JPanel();
		led1Status = new JLabel("OFF");
		led2Status = new JLabel("OFF");
		led3Status = new JLabel("OFF");
		led4Status = new JLabel("OFF");
		StartStop = new JButton("Start");
		StartStop.addActionListener(this);
		resaultRightRate = new JLabel("0");
		resaultQuickest = new JLabel("0");
		resaultSlowest = new JLabel("0");
		resaultAverageTime = new JLabel("0");
		connectedTF = false;
		runningTF = false;

		// Components Setup:

		// Sub-Panels Setup:
		WestPanel.setLayout(new GridLayout(4, 2));
		WestPanel.add(new JLabel(" LED1: "));
		WestPanel.add(led1Status);
		WestPanel.add(new JLabel(" LED2: "));
		WestPanel.add(led2Status);
		WestPanel.add(new JLabel(" LED3: "));
		WestPanel.add(led3Status);
		WestPanel.add(new JLabel(" LED4: "));
		WestPanel.add(led4Status);
		SouthPanel.add(StartStop);
		CenterPanel.setLayout(new GridLayout(4, 3));
		CenterPanel.add(new JLabel("  Accuracy:"));
		CenterPanel.add(resaultRightRate);
		CenterPanel.add(new JLabel("%"));
		CenterPanel.add(new JLabel("  Quickest:"));
		CenterPanel.add(resaultQuickest);
		CenterPanel.add(new JLabel("ms"));
		CenterPanel.add(new JLabel("  Slowest:"));
		CenterPanel.add(resaultSlowest);
		CenterPanel.add(new JLabel("ms"));
		CenterPanel.add(new JLabel("  Average Time:"));
		CenterPanel.add(resaultAverageTime);
		CenterPanel.add(new JLabel("ms"));

		// MainPanel setup:
		GameOnUiMainPanel.setLayout(GameOnUiLayout);
		GameOnUiMainPanel.add(WestPanel, BorderLayout.WEST);
		GameOnUiMainPanel.add(SouthPanel, BorderLayout.SOUTH);
		GameOnUiMainPanel.add(CenterPanel, BorderLayout.CENTER);

		// Container Setup
		GameOnUiContainer = this.getContentPane();
		GameOnUiContainer.add(GameOnUiMainPanel);
		this.addWindowListener(this);
		this.setTitle("Arduino LED Game: Connect Arduino");
		this.setLocation(750, 525);
		this.setSize(400, 150);
		this.setVisible(false);

	}

	public boolean connectSerial(String portName, int bandRate) {

		try {
			// Obtain a CommPortIdentifier object for the port you want to open
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(portName);

			// Get the port's ownership
			serialPort = (SerialPort) portId.open("ArduinoLEDGame", 5000);

			// Set the parameters of the connection.
			serialPort.setSerialPortParams(bandRate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			/* Close Connection
			serialPort.close();
			System.out.println("Connection closed");
			*/
			
			outStream = serialPort.getOutputStream();
			//inStream = serialPort.getInputStream(); <- not necessary,we don't read anything.

		} catch (Exception cause) {
			StackTraceElement elements[] = cause.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				System.err.println("  " + elements[i].getFileName() + ":"
						+ elements[i].getLineNumber() + ">> "
						+ elements[i].getMethodName() + "()");
			}
			connectedTF = false;
			return false;
		}
		connectedTF = true;
		return true;
	}
	

	public void sendSerial(int a, int b, int c, int d){
		
		String out ="a"+a+"b"+b+"c"+c+"d"+d+"#";
		try {
			outStream.write(out.getBytes());
			System.out.println("out: "+out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void leds(int a,int b, int c, int d){
		
		if(a==0){led1Status.setText("OFF");}else if(a==1){led1Status.setText("ON");}
		else if(b==0){led2Status.setText("OFF");}else if(a==1){led2Status.setText("ON");}
		else if(c==0){led3Status.setText("OFF");}else if(a==1){led3Status.setText("ON");}
		else if(d==0){led4Status.setText("OFF");}else if(a==1){led4Status.setText("ON");}
		this.repaint();
		sendSerial(a,b,c,d);
	}
	
	public void ledsBlink(long Ontime,long Offtime, int loops){
		try{
			for(int i =0; i<loops;i++){
				leds(1,1,1,1);
				Thread.sleep(Ontime);
				leds(0,0,0,0);
				Thread.sleep(Offtime);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(runningTF == false){
			StartStop.setEnabled(false);
			runningTF = true;
			gameStart();
			gameStop();
		}
	}

	
	public void gameStart(){
		try {
			ledsBlink(500,500,3);
			
			for(int i=1;i<=Testloops;i++){
				Thread.sleep(500);
				int k = new Random().nextInt((4 - 1) + 1)+1;
				System.out.println(k);
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void gameCalculate(){
		
		
	}
	
	public void gameStop(){
		gameCalculate();
		StartStop.setEnabled(true);
		runningTF = false;
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	
	
	
	
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(connectedTF == true){
			serialPort.close();
		}
		System.exit(0);

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
