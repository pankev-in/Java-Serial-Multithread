package ReactionGame;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartConnectUi extends JFrame implements WindowListener,
		ActionListener, KeyListener {

	/**
	 * StartConnectUI ist responsibel for Connect UI and Passing Serial
	 * Information (location, bandwidth) to GameOnUi Class
	 */
	private static final long serialVersionUID = -7269215348397080470L;
	
	// GUI Components:
	private JLabel programInformation;
	private JButton Connect;
	private JTextField deviceLocation;
	private JPanel startConnectPanel;
	private Container startConnectContainer;
	private JComboBox bandWidthList;
	

	// Some Variabels:
	private static String bandWidth[] = { "9600", "19200", "38400", "57600",
			"115200" };
	private int bandRate = 9600;

	// Other:
	private GameLog log;
	private GameOnUi gameUi;

	public StartConnectUi(GameLog log) {

		// Describing Components:
		programInformation = new JLabel();
		startConnectContainer = new Container();
		startConnectPanel = new JPanel();
		deviceLocation = new JTextField();
		Connect = new JButton();
		bandWidthList = new JComboBox(bandWidth);
		bandWidthList.setSelectedIndex(0);
		programInformation
				.setText("   Please enter the device location of Arduino");
		Connect.setText("Connect");
		Connect.setEnabled(true);
		Connect.addActionListener(this);
		deviceLocation.setText("Win: COMx | Linux: /dev/ttyxx");
		bandWidthList.addKeyListener(this);
		deviceLocation.addKeyListener(this);
		this.log = log;
		gameUi = new GameOnUi();
		
		// Setup JPanel Layout and adds Components:
		startConnectPanel.setLayout(new GridLayout(4, 1, 10, 10));
		startConnectPanel.add(programInformation);
		startConnectPanel.add(deviceLocation);
		startConnectPanel.add(bandWidthList);
		startConnectPanel.add(Connect);

		// Container and Window Setup:
		startConnectContainer = this.getContentPane();
		startConnectContainer.add(startConnectPanel);
		this.addWindowListener(this);
		this.setTitle("Arduino LED Game | Connect ");
		this.setLocation(550, 300);
		this.setSize(300, 200);
		this.setVisible(true);
	}

	private void connect() {

		// Get the device Location
		String portName = deviceLocation.getText();

		// Check the Bandwidth SelectionBox and convert them:
		int bandWidthIndex = bandWidthList.getSelectedIndex();
		switch (bandWidthIndex) {
		case 0:
			bandRate = 9600;
			break;
		case 1:
			bandRate = 19200;
			break;
		case 2:
			bandRate = 38400;
			break;
		case 3:
			bandRate = 57600;
			break;
		case 4:
			bandRate = 115200;
			break;
		}

		// System Print Line: Connection information:
		log.write("##############################");
		log.write("System Message: Trying to connect to:" + portName
				+ " with " + bandRate + " Bandwidth");

		// Try to connect gameUi Class with specific device location and
		// bandwidth:
		if (!gameUi.connectSerial(portName, bandRate, this.log)) {

			// If Connection fails, shows an Error Message
			log.write("System Message: Fails to connect to:"
					+ portName + " with " + bandRate + " Bandwidth");
			log.write("##############################");
			JOptionPane.showMessageDialog(null,
					"Can't connect to this device, please check again.",
					"Connection Error", JOptionPane.ERROR_MESSAGE);

		} else {

			// If Connection Success, then hide this connection Ui and
			// Change into GameUi:
			log.write("System Message: Connect to:" + portName
					+ " with " + bandRate + " Bandwidth Seccessesfully");
			log.write("##############################");
			JOptionPane.showMessageDialog(null,
					"Device Connected, have fun! :)",
					"Connection Success", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			gameUi.setVisible(true);
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Check the Event Source, if it is from Connect Button,then:
		if (e.getSource() == Connect) {
			connect();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Check the Keysource, if it is from 'Enter' Button, then:
		if(arg0.getKeyCode()==10){
			connect();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
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
