/****************************
 * Anna Whitaker
 * 102-20-343
 * 02/23/15
 * Assignment 5
 * 
 * This program implements ray tracing.
 * 
 * The Sphere class defines a sphere (a point and a radius) and defines appropriate functions for it.
 * 
 **************************/

import java.awt.Color;

public class Sphere
{
	int[] scenter;
	int r;
	Color col;
	
	public Sphere(int x, int y, int z, Color scolor)
	{
		//central point - the (l, m, n)
		scenter = new int[] {x, y, z};
		//radius
		r = 150;
		col = scolor;
		
	}
	
	public Color getColor()
	{
		return col;
	}
	
	public boolean Intersection(int xStart, int yStart, int zStart, double rayX, double rayY, double rayZ, double[] t, double[] intersectXYZ, double[] objNorm)
	{
		//disc - discriminate -- ts1, ts2 are entry and exit point
		double disc, ts1, ts2;
		
		//t - distance to object. abc - a, b, c of quadratic formula
		double a, b, c, tsphere;
		
		//l = scenter[0], m = scenter[1], n = scenter[2]
		//computing intersection of ray with sphere - all from notes
		a = rayX * rayX + rayY * rayY + rayZ * rayZ;
		b = 2 * rayX * (xStart - scenter[0]) + 2 * rayY * (yStart - scenter[1]) + 2 * rayZ * (zStart - scenter[2]);
		c = scenter[0] * scenter[0] + scenter[1] * scenter[1] + scenter[2] * scenter[2] +
				xStart * xStart + yStart * yStart + zStart * zStart +
				2 * (-scenter[0] * xStart - scenter[1] * yStart - scenter[2] * zStart) - r * r;
		
		disc = b * b - 4 * a * c;
		
		if(disc < 0) //doesn't intersect with sphere at all
			return false;
		else
		{
			ts1 = (-b + Math.sqrt(disc)) / (2 * a);
			ts2 = (-b - Math.sqrt(disc)) / (2 * a);
			if(ts1 >= ts2) //seeing which is closer
				tsphere = ts2;
			else
				tsphere = ts1;
			if(t[0] < tsphere) //another object closer
				return false;
			else if(tsphere < 0) //no visible intersection
				return false;
			else
			{
				t[0] = tsphere;
				intersectXYZ[0] = xStart + rayX * tsphere;
				intersectXYZ[1] = yStart + rayY * tsphere;
				intersectXYZ[2] = zStart + rayZ * tsphere;
				objNorm[0] = intersectXYZ[0] - scenter[0];
				objNorm[1] = intersectXYZ[1] - scenter[1];
				objNorm[2] = intersectXYZ[2] - scenter[2];
				return true;
			}

		}
		
	}
	
}
