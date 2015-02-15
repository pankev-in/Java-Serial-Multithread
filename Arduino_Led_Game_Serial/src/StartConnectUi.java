
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		ActionListener {

	private JLabel programInformation;
	private JButton Connect;
	private JTextField deviceLocation;
	private JPanel startConnectPanel;
	private Container startConnectContainer;
	private SerialPort serialPort;
	private JComboBox bandWidthList;
	private String bandWidth[] = { "9600", "19200", "38400", "57600","115200" };
	private int bandRate = 9600;
	private GameOnUi gameUi = new GameOnUi();

	public StartConnectUi() {

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
		deviceLocation.setText("Windows: COMx | Linux:/dev/...");

		startConnectPanel.setLayout(new GridLayout(4, 1, 10, 10));
		startConnectPanel.add(programInformation);
		startConnectPanel.add(deviceLocation);
		startConnectPanel.add(bandWidthList);
		startConnectPanel.add(Connect);

		startConnectContainer = this.getContentPane();
		startConnectContainer.add(startConnectPanel);
		this.addWindowListener(this);
		this.setTitle("Arduino LED Game | Connect ");
		this.setLocation(800, 500);
		this.setSize(300, 200);
		this.setVisible(true);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Connect) {

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

			String portName = deviceLocation.getText();
			System.out.println("Connecting to:" + portName + " with "
					+ bandRate + " Bandwidth");

			boolean seccuss = gameUi.connectSerial(portName, bandRate);

			if (!seccuss) {
				JOptionPane.showMessageDialog(null,
						"Can't connect to this device, please check again.",
						"Connection Error", JOptionPane.ERROR_MESSAGE);
			}else{
				System.out.println("Connected!!!");
				this.setVisible(false);
				gameUi.setVisible(true);
			}

		}

	}

}
