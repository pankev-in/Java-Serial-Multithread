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

public class GameOnUi extends JFrame implements WindowListener, ActionListener {

	private JPanel GameOnUiMainPanel;
	private JPanel CenterPanel;
	private JPanel SouthPanel;
	private Container GameOnUiContainer;
	private BorderLayout GameOnUiLayout;
	private JButton StartStop;
	private JLabel resaultRightRate;
	private JLabel resaultQuickest;
	private JLabel resaultSlowest;
	private JLabel resaultAverageTime;
	private SerialPort serialPort;
	private OutputStream outStream;
	// private InputStream inStream;
	private Game backgroundgame;
	private boolean RunningTF;

	public GameOnUi() {

		GameOnUiLayout = new BorderLayout();
		GameOnUiMainPanel = new JPanel();
		GameOnUiContainer = new Container();
		CenterPanel = new JPanel();
		SouthPanel = new JPanel();
		StartStop = new JButton("Start");
		StartStop.addActionListener(this);
		resaultRightRate = new JLabel("0");
		resaultQuickest = new JLabel("0");
		resaultSlowest = new JLabel("0");
		resaultAverageTime = new JLabel("0");
		RunningTF = false;

		// Components Setup:

		// Sub-Panels Setup:
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

			/*
			 * Close Connection serialPort.close();
			 * System.out.println("Connection closed");
			 */

			outStream = serialPort.getOutputStream();
			// inStream = serialPort.getInputStream(); <- not necessary,we don't
			// read anything.
			
			backgroundgame = new Game(outStream);
			this.StartStop.addKeyListener(backgroundgame);
			
		} catch (Exception cause) {
			StackTraceElement elements[] = cause.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				System.err.println("  " + elements[i].getFileName() + ":"
						+ elements[i].getLineNumber() + ">> "
						+ elements[i].getMethodName() + "()");
			}
			return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (RunningTF == false) {
			RunningTF = true;
			gameRun();
			this.StartStop.addActionListener(this);
		}
	}
	
	public synchronized void gameRun(){
		Thread gameThread = new Thread(backgroundgame);
		gameThread.setName("backgroundgameThread");
		gameThread.start();
		try {
			//gameThread.setDaemon(true);
			gameThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		resaultRightRate.setText(Integer.toString(backgroundgame
				.getResault_RightRate()));
		resaultQuickest.setText(Integer.toString(backgroundgame
				.getResault_Quickest()));
		resaultSlowest.setText(Integer.toString(backgroundgame
				.getResault_Slowest()));
		resaultAverageTime.setText(Integer.toString(backgroundgame
				.getResault_AverageTime()));
		RunningTF = false;
	}

	public void keyPressed(KeyEvent e) {
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
		serialPort.close();
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
