import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GameLog {

	private FileOutputStream out;
	private JTextArea textArea;
	private boolean textAreaSetTF;
	
	public GameLog(){
		try {
			out = new FileOutputStream("log_"+new Date().getTime()+".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.textAreaSetTF = false;
	}
	
	public void SetJTextArea(JTextArea textArea){
		this.textArea = textArea;
		textAreaSetTF = true;
	}
	
	public boolean getTextAreaSetTF(){
		return textAreaSetTF;
	}
	
	public void write(String log){
		try {
			out.write(log.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(log);
	}

	public void writeTextArea(String log){
		try {
			out.write(log.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(textAreaSetTF == true){
			textArea.append(log+"\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		System.out.println(log);
	}
	
	public void close(){
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
