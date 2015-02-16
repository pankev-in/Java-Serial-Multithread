/*
 * Program Name: Arduino LED Reaction Game
 * 
 * Author: Alexander Wurm, Kevin Pan
 * 
 * Last Update: 02.2015
 * 
 * Description: 
 * 	A Java Program which implements RXTX library and 
 * 	communicate with an Arduino through Serial IO. It 
 * 	is game and can test your Reaction speed.
 * 
 * Contact: kpan@student.tgm.ac.at 
 * 			awurm@student.tgm.ac.at
 * */



public class Start_main {
	
	public static void main(String[] args) {
		
		GameLog log = new GameLog();
		
		// Printing out some Program informations:
		log.write("Arduino LED Reaction Game ");
		log.write("Autor: Alexander Wurm, Kevin Pan");
		log.write("Last Update: 02.2015");
		
		// Starting Program:
		new StartConnectUi(log);
	}

}
