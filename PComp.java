/****************************
 * Anna Whitaker
 * 102-20-343
 * 02/27/15
 * Assignment 5
 * 
 * This program implements ray tracing.
 * 
 * The PComp class is the paint component.
 * 
 **************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;

public class PComp extends JComponent
{

	//size of frame
	Dimension framesize;
	
	//create instances of checkerboard, blue sphere, greensphere
	Plane cb = new Plane();
	Sphere sg = new Sphere(-100, -200, 420, Color.green);
	Sphere sb = new Sphere(250, -100, 400, Color.blue);
	
	int d = 500;
	float vertices[][];
	
	//adjusted x and y to compensate for top left origin
	int adjustedX;
	int adjustedY;
	
	//starting points
	int xStart = 0;
	int yStart = 0;
	int zStart = -d;
	
	double rayX, rayY, rayZ;
	
	int depth = 5;
	
	int ir = 0;
	int ib = 0;
	int ig = 0;

	public void paintComponent(Graphics g)
	{	
		//super.paintComponent(g);
		//grab size of frame
		framesize = this.getSize();
		
		//grab vertices from plane
	//	vertices = p.getV();
		
		for(int x = 0; x <= framesize.width; x++)
		{
			//adjust the x coordinate for the move of the origin from the top left to center of screen
			adjustedX = x - (framesize.width/2);
			
			for(int y = 0; y <= framesize.height; y++)
			{
				//adjust the y coordinate for the move of the origin from the top left to center of screen
				adjustedY = (framesize.height/2) - y;
				
				//x,y,z components of the ray from the viewer to the pixel
				rayX = adjustedX - xStart;
				rayY = adjustedY - yStart;
				rayZ = 0 - zStart;
				
				g.setColor(RayTrace(depth, rayX, rayY, rayZ, xStart, yStart, zStart, ir, ig, ib));
			//	g.setColor(Color.black);
				g.drawLine(x, y, x, y);
				
			}//end forx
		}//end fory
	}//end paint
		
		public Color RayTrace(int level, double rayX, double rayY, double rayZ, int xStart, int yStart, int zStart, int ir, int ig, int ib)
		{
			//distance of closest object
	//		double t = 100000;			
			double[] t = new double[] {100000};
			
			//x, y, z intersection points of ray adn object, and the normal of the object at the intersect point
			double[] intersectXYZ = new double[] {0, 0, 0};
			
			double[] objNorm = new double[] {0, 1, 0};
			
			//flag to keep track of which object is being intersected, defaults to no object intersected
			int flag = -1;
						
			//return black if you go deeper than limit
			if(level == 0)
			{
				return Color.BLACK;
			}
			else //check which intersects
			{
				if(cb.Intersection(xStart, yStart, zStart, rayX, rayY, rayZ, t, intersectXYZ))
				{
					flag = 0;
				}
				if(sb.Intersection(xStart, yStart, zStart, rayX, rayY, rayZ, t, intersectXYZ, objNorm))
				{
					flag = 1;
				}
				if(sg.Intersection(xStart, yStart, zStart, rayX, rayY, rayZ, t, intersectXYZ, objNorm))
				{
					flag = 2;
				}
			}
			
			switch(flag) //call intensity function depending on intersection
			{
				case 0:
					return Intensity(level, rayX, rayY, rayZ, objNorm, cb.getCBcolor(intersectXYZ[0], intersectXYZ[2]), ir, ig, ib, intersectXYZ);
				case 1:
					return Intensity(level, rayX, rayY, rayZ, objNorm, sb.getColor(), ir, ig, ib, intersectXYZ);
				case 2:
					return Intensity(level, rayX, rayY, rayZ, objNorm, sg.getColor(), ir, ig, ib, intersectXYZ);
				default:
					return new Color(110, 165, 225);
			}
		}
		

		public Color Intensity(int level, double rayX, double rayY, double rayZ, double[] objNorm, Color col, int ir, int ig, int ib, double[] intersectXYZ)
		{
			int rComp = col.getRed();
			int gComp = col.getGreen();
			int bComp = col.getBlue();
			
			double magnitude;
			double rayXnorm, rayYnorm, rayZnorm; //normalized ray (incoming)
			double objnormX_norm, objnormY_norm, objnormZ_norm; //normalized normal? of surface vector
			double cosine_phi; //cosine of reflection angle
			double[] reflectionV = new double[3]; //reflection vector[x,y,z]
//			double rx;
//			double ry;
//			double rz;
//			
			//normalize incoming ray and surface normal
			magnitude = Math.sqrt(rayX * rayX + rayY * rayY + rayZ * rayZ);
			rayXnorm = rayX / magnitude;
			rayYnorm = rayY / magnitude;
			rayZnorm = rayZ / magnitude;
			
			magnitude = Math.sqrt(objNorm[0] * objNorm[0] + objNorm[1] * objNorm[1] + objNorm[2] * objNorm[2]);
			objnormX_norm = objNorm[0] / magnitude;
			objnormY_norm = objNorm[1] / magnitude;
			objnormZ_norm = objNorm[2] / magnitude;
			
			//calculate reflection vector
			cosine_phi = (-rayXnorm) * objnormX_norm + (-rayYnorm) * objnormY_norm + (-rayZnorm) * objnormZ_norm;
			
			if(cosine_phi > 0)
			{
				reflectionV[0] = objnormX_norm - (-rayXnorm) / (2 * cosine_phi);
				reflectionV[1] = objnormY_norm - (-rayYnorm) / (2 * cosine_phi);
				reflectionV[2] = objnormZ_norm - (-rayZnorm) / (2 * cosine_phi);
//				rx = objnormX_norm - (-rayXnorm) / (2 * cosine_phi);
//				ry = objnormY_norm - (-rayYnorm) / (2 * cosine_phi);
//				rz = objnormZ_norm - (-rayZnorm) / (2 * cosine_phi);
			}
			else if(cosine_phi == 0)
			{
				reflectionV[0] = rayXnorm;
				reflectionV[1] = rayYnorm;
				reflectionV[2] = rayZnorm;
//				rx = rayXnorm;
//				ry = rayYnorm;
//				rz = rayZnorm;
			}
			else//if(cosine_phi < 0)
			{
				reflectionV[0] = -objnormX_norm + (-rayXnorm) / (2 * cosine_phi);
				reflectionV[1] = -objnormY_norm + (-rayYnorm) / (2 * cosine_phi);
				reflectionV[2] = -objnormZ_norm + (-rayZnorm) / (2 * cosine_phi);
//				rx = -objnormX_norm + (-rayXnorm) / (2 * cosine_phi);
//				ry = -objnormY_norm + (-rayYnorm) / (2 * cosine_phi);
//				rz = -objnormZ_norm + (-rayZnorm) / (2 * cosine_phi);
			}
			
			//ROUND OFF QUESTION:
			// NO ROUNDOFF ADDITION = no reflection plus random lines
			// NEGATIVE ADDITION = no reflection but no random lines
			// POSITIVE ADDITION (even 0.00000001) PERFECT REFLECTION AND NO LINES, WHYYYY
			Color derp = RayTrace(level - 1, reflectionV[0], reflectionV[1], reflectionV[2], (int) (intersectXYZ[0] + 0.01), (int) (intersectXYZ[1] + 0.01), (int) (intersectXYZ[2] + 0.01), ir, ig, ib);
			//Color derp = RayTrace(level - 1, reflectionV[0], reflectionV[1], reflectionV[2], startX, startY, startZ, ir, ig, ib);

			
			//Color derp = RayTrace(level - 1, rx, ry, rz, intersectXYZ[0], intersectXYZ[1], intersectXYZ[2], ir, ig, ib);
			
			ir = (int)(0.6 * derp.getRed() + 0.4 * rComp);
			ig = (int)(0.6 * derp.getGreen() + 0.4 * gComp);
			ib = (int)(0.6 * derp.getBlue() + 0.4 * bComp);
			
			//return new Color(ir, ig, ib);
			Color tempreturn = new Color(ir, ig, ib);
			return illumination(tempreturn, objnormX_norm, objnormY_norm, objnormZ_norm, rayXnorm, rayYnorm, rayZnorm, intersectXYZ);
			
		}
		
		
		//Phong
		
		
		
		
		
		
		public Color illumination(Color tempreturn, double objnormX_norm, double objnormY_norm, double objnormZ_norm, double rayXnorm, 
				double rayYnorm, double rayZnorm, double[] intersectXYZ)
		{
			
			int Kdr = tempreturn.getRed();
			int Kdg = tempreturn.getGreen();
			int Kdb = tempreturn.getBlue();
			
			//constants
			float Ia = .6f; //intensity value - ambience
			float Ip = .6f; //intensity value - point
			float Ks = .8f; //specular - amount of light reflected
			int n = 9; //specular - spread of highlight
			
			
			//dot product of the normal vector and the normalized light vector (-1, -1, 1)
			double cosPhi = objnormX_norm * 0.577 + objnormY_norm * 0.577 + objnormZ_norm * -0.577;
			
			//R vector
			double Rx;
			double Ry;
			double Rz;
			double normR;
			
			//to find the R vector - depends on cosphi
			if (cosPhi > 0)
			{
				//refer to notes
				Rx = objnormX_norm - (-0.577 / 2 * cosPhi);
				Ry = objnormY_norm - (-0.577 / 2 * cosPhi);
				Rz = objnormZ_norm - (0.577 / 2 * cosPhi);
				
				normR = Math.sqrt(Rx*Rx + Ry*Ry + Rz*Rz);
				Rx = Rx/normR;
				Ry = Ry/normR;
				Rz = Rz/normR;
			}
			else if(cosPhi == 0)
			{
				Rx = 0.577;
				Ry = 0.577;
				Rz = -0.577;
			}
			else //if cosphi is negative, do the R math  - maybe set cosphi to 0 so it doesn't affect lighting?
			{
				Rx = -objnormX_norm + (-0.577 / 2*cosPhi);
				Ry = -objnormY_norm + (-0.577 / 2*cosPhi);
				Rz = -objnormZ_norm + (0.577 / 2*cosPhi);
//				normR = Math.sqrt(Rx*Rx + Ry*Ry + Rz*Rz);
//				Rx = Rx/normR;
//				Ry = Ry/normR;
//				Rz = Rz/normR;
//				cosPhi = 0; //so it's ambient light color, not darker - but probably doesn't really matter
			}
			
			normR = Math.sqrt(Rx*Rx + Ry*Ry + Rz*Rz);
			Rx = Rx/normR;
			Ry = Ry/normR;
			Rz = Rz/normR;
			
			//dot product of the R vector and view vector (negative because it's calculated in direction of eye to pixel, needs to be opposite)
			double cosTheta = Rx * -(rayXnorm) + Ry * -(rayYnorm) + Rz * -(rayZnorm);
			
				double max = ((Ia * 255) + (Ip * 255) + (Ip * Ks));
				double valr = ((Ia * Kdr) + (Ip * Kdr * cosPhi) + (Ip * Ks * Math.pow(cosTheta, n)));
				double valg = ((Ia * Kdg) + (Ip * Kdg * cosPhi) + (Ip * Ks * Math.pow(cosTheta, n)));
				double valb = ((Ia * Kdb) + (Ip * Kdb * cosPhi) + (Ip * Ks * Math.pow(cosTheta, n)));
				
//				double valr = ((Ia * Kdr) + (Ip * Kdr * cosPhi) + (Ip * Ks * Math.pow((Rx * -Vx + Ry * -Vy + Rz * -Vz), n)));
//				double valg = ((Ia * Kdg) + (Ip * Kdg * cosPhi) + (Ip * Ks * Math.pow((Rx * -Vx + Ry * -Vy + Rz * -Vz), n)));
//				double valb = ((Ia * Kdb) + (Ip * Kdb * cosPhi) + (Ip * Ks * Math.pow((Rx * -Vx + Ry * -Vy + Rz * -Vz), n)));

				
				return new Color((int) (255 * valr/max), (int) (255 * valg/max), (int) (255 * valb/max));
			
		}
		
		
}
