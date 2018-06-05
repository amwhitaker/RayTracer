/****************************
 * Anna Whitaker
 * 102-20-343
 * 02/23/15
 * Assignment 5
 * 
 * This program implements ray tracing.
 * 
 * The Plane class defines a plane, or our "checkerboard" and defines appropriate functions for it.
 * 
 **************************/

import java.awt.*;

import javax.swing.*;


public class Plane extends JPanel
{
	float[][] vertices;

	//constructor
	public Plane()
	{
		//plane vertices
		vertices = new float[][] {{-400, -400, 0}, {400, -400, 0}, {-400, -400, 800}, {400, -400, 800}};

	}

	//get checkerboard color
	public Color getCBcolor(double x, double z)
	{
		//boolean endx = x > -400 && x < 400;
		boolean endz = z > 0 && z < 800;
		
		//checking switching points for red/black
		//if( ((x+400) % 200) < 100 && endx)
		if(Math.abs(x) % 200 < 100)
		{
			if( (z % 200) < 100 && endz)
			{
				return Color.red;
			}
			else if (endz)
			{
				return Color.white;
			}
			
		}
		else //if (endx)
		{
			if( (z % 200) < 100 && endz)
			{
				return Color.white;
			}
			else if (endz)
			{
				return Color.red;
			}
			
		}//end else if
		
		//else, if not within plane
		return new Color(110, 165, 225);
	}
	
	public boolean Intersection(int xStart, int yStart, int zStart, double rayX, double rayY, double rayZ, double[] t, double[] intersectXYZ)
	{
		double x1, y1, z1, denom, t_obj, d, x, y, z;
		
		//normal of plane
		double a = 0;
		double b = 1;
		double c = 0;
		
//		//point on plane
//		x1 = 0;
//		y1 = -400;
//		z1 = 0;
		
		//point on plane
		x1 = vertices[0][0];
		y1 = vertices[0][1];
		z1 = vertices[0][2];
		
		//computer intersection of ray with plane - denominator for intersection math
		denom = a * rayX + b * rayY + c * rayZ;
		
		if(Math.abs(denom) <= 0.001) //not 0 because of round off
			return false;
		else
		{
			d = a * x1 + b * y1 + c * z1;
			t_obj = -(a * xStart + b * yStart + c * zStart - d)/denom;
			x = xStart + rayX * t_obj;
			y = yStart + rayY * t_obj;
			z = zStart + rayZ * t_obj;
			
			//horizon effect stuffz - t_obj check just for double checking, z < 0 not in view
			if((z < 0) || (z > 800) || (t_obj < 0))
				return false; //no visible intersection
			else if(t[0] < t_obj)
				return false;
			else
			{
				t[0] = t_obj;
				intersectXYZ[0] = x;
				intersectXYZ[1] = y;
				intersectXYZ[2] = z;
				return true;
			}
		}		
	}
	
}
