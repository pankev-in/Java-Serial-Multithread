package ReactionGame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeylistener implements KeyListener {

	private int lastPressed;
	private boolean SomethingPressedTF;

	public GameKeylistener() {
		lastPressed = 0;
		SomethingPressedTF = false;
	}
	
	public int getLastPressedKey(){
		SomethingPressedTF = false;
		return lastPressed;
	}

	public boolean ifLastPressedTF() {
		if (SomethingPressedTF == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public void resetKeyRecords(){
		lastPressed = 0;
		SomethingPressedTF = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		lastPressed = e.getKeyCode();
		SomethingPressedTF = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("Released:" + e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
