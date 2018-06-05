/****************************
 * Anna Whitaker
 * 102-20-343
 * 02/27/15
 * Assignment 5
 * 
 * This program implements ray tracing.
 * 
 * The Main class sets up the canvas used for "painting".
 * 
 **************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;


public class Main extends JPanel
{
	//declarations
	PComp p;
	
	public Main()
	{
		//initialize new instance of paint component
		p = new PComp();
		//initialize canvas, set a few characteristics, add object to canvas
		JFrame canvas = new JFrame("Fancy Title");
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setSize(800, 800);
		canvas.setVisible(true);
		canvas.add(p);
		
	}
	
	//Main function
	public static void main(String[] args)
	{
		//puts runnable object into queue which will run main constructor
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				new Main().setVisible(true);
			}
		});
	}	
}
