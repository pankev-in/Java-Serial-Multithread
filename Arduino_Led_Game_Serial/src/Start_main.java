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
		
		//Printing out some Program informations:
		System.out.println("Arduino LED Reaction Game ");
		System.out.println("Autor: Alexander Wurm, Kevin Pan");
		System.out.println("Last Update: 02.2015");
		System.out.println("##############################");
		
		//Starting Program:
		new StartConnectUi();
	}

}
