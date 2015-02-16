package ReactionGame;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameOnUi extends JFrame implements WindowListener, ActionListener {

	/**
	 * GameOnUi is responsible for Game GUI and passes information to Background
	 * Game Thread.
	 */
	private static final long serialVersionUID = 1L;
	
	// GUI Components:
	private JPanel GameOnUiMainPanel;
	private JPanel CenterPanel;
	private JPanel SouthPanel;
	private JPanel EastPanel;
	private Container GameOnUiContainer;
	private BorderLayout GameOnUiLayout;
	private JButton StartStop;
	private JLabel resaultRightRate;
	private JLabel resaultQuickest;
	private JLabel resaultSlowest;
	private JLabel resaultAverageTime;
	private JLabel loopRound;
	private JTextArea gameLogTextArea;
	private JScrollPane scrollLogTextArea;

	// SerialIO Objects:
	private SerialPort serialPort;

	// Other Variables:
	private GameLog log; //Inherit log Class from main and to Background Thread
	private Game backgroundgame; //Background running object
	private GameKeylistener keylistener; //GameKeylistener Class
	private boolean RunningTF;
	private Thread gameThread;//Background Thread

	public GameOnUi() {

		// Describing Components:
		loopRound = new JLabel("  Round: 0");
		GameOnUiLayout = new BorderLayout();
		GameOnUiMainPanel = new JPanel();
		GameOnUiContainer = new Container();
		CenterPanel = new JPanel();
		SouthPanel = new JPanel();
		EastPanel = new JPanel();
		EastPanel.setSize(200, 250);
		StartStop = new JButton("Start");
		StartStop.addActionListener(this);
		resaultRightRate = new JLabel("0");
		resaultQuickest = new JLabel("0");
		resaultSlowest = new JLabel("0");
		resaultAverageTime = new JLabel("0");
		gameLogTextArea = new JTextArea(7,1);
		gameLogTextArea.setSize(400, 100);
		gameLogTextArea.setLineWrap(true);
		gameLogTextArea.setEditable(false);
		gameLogTextArea.setVisible(true);
		scrollLogTextArea = new JScrollPane (gameLogTextArea);
		scrollLogTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Setup Variables:
		RunningTF = false;
		keylistener = new GameKeylistener();
		StartStop.addKeyListener(keylistener);

		// Sub-Panels Setup:
		SouthPanel.setLayout(new GridLayout(1,2));
		SouthPanel.add(loopRound);
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
		GameOnUiMainPanel.add(scrollLogTextArea, BorderLayout.NORTH);
		GameOnUiMainPanel.add(SouthPanel, BorderLayout.SOUTH);
		GameOnUiMainPanel.add(CenterPanel, BorderLayout.CENTER);
		GameOnUiMainPanel.add(EastPanel, BorderLayout.EAST);

		// Container and Window Setup:
		GameOnUiContainer = this.getContentPane();
		GameOnUiContainer.add(GameOnUiMainPanel);
		this.addWindowListener(this);
		this.addKeyListener(keylistener);
        this.setFocusable(true);
		this.setTitle("Arduino LED Game: Connect Arduino");
		this.setLocation(500, 300);
		this.setSize(400, 250);
		this.setVisible(false);

	}

	public boolean connectSerial(String portName, int bandRate,GameLog log) {

		// Try to connect this Serial IO Device:
		try {

			// Obtain a CommPortIdentifier object for the port you want to open:
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(portName);

			// Get the port's ownership, default timeout is 5 sec:
			serialPort = (SerialPort) portId.open("ArduinoLedGame", 5000);

			// Set the parameters of the connection:
			serialPort.setSerialPortParams(bandRate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			// Setup Output Stream for Background Thread:
			this.log = log;
			log.SetJTextArea(gameLogTextArea);
			backgroundgame = new Game(serialPort.getOutputStream(),keylistener,StartStop,loopRound,this.log);

		} catch (Exception cause) {

			// If anything goes wrong:
			return false;
		}

		// If everything above works:
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If Button is set to Start:
		if(StartStop.getText()=="Start"){
			if (RunningTF == false) {
				StartStop.setText("Stop");
				RunningTF = true;
				gameThread = new Thread(backgroundgame);
				gameThread.start();
			}
		}
		// IF Button is set to Calculate:
		else if(StartStop.getText()=="Calculate"){
			
				//Set Jbutton to Calculating:
				StartStop.setText("Calculating");
				StartStop.setEnabled(false);
				
				resaultRightRate.setText(Integer.toString(backgroundgame.getResault_RightRate()));
				resaultQuickest.setText(Integer.toString(backgroundgame.getResault_Quickest()));
				resaultSlowest.setText(Integer.toString(backgroundgame.getResault_Slowest()));
				resaultAverageTime.setText(Integer.toString(backgroundgame.getResault_AverageTime()));
				
				log.write("Right Rate(%): "+Integer.toString(backgroundgame.getResault_RightRate()));
				log.write("Quickest(ms): "+Integer.toString(backgroundgame.getResault_Quickest()));
				log.write("Slowest(ms): "+Integer.toString(backgroundgame.getResault_Slowest()));
				log.write("Average(ms):"+Integer.toString(backgroundgame.getResault_AverageTime()));
				log.write("------------------------------");
				
				backgroundgame.reset();
				StartStop.setText("Start");
				StartStop.setEnabled(true);
				RunningTF = false;
				
		}
		// If button is set to Stop:
		else if(StartStop.getText()=="Stop"){
			if (RunningTF == true){
				
				// Stop Thread:
				gameThread.stop(); //<-- Sorry, But i have to use it.
				
				// Reseting JTextfield:
				resaultRightRate.setText("0");
				resaultQuickest.setText("0");
				resaultSlowest.setText("0");
				resaultAverageTime.setText("0");
				
				// Reseting Background Object
				backgroundgame.reset();
				
				// Set button back to START:
				StartStop.setText("Start");
				StartStop.setEnabled(true);
				RunningTF = false;
			}
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {

		// System Existing, Close the serial connection and exit:
		serialPort.close();
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
