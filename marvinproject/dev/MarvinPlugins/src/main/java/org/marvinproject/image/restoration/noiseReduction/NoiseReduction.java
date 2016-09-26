/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.restoration.noiseReduction;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Restores noise from the degraded noisy image. 
 * Ref paper - Rudin Osher Fatemi:'Nonlinear TotalVariation based noise removal techniques'.
 * 
 * 
 * @version 1.0 02/28/2008
 * @author Smita Nair
 */

public class NoiseReduction extends MarvinAbstractImagePlugin {
	
	public void load(){
	}

	public MarvinAttributesPanel getAttributesPanel(){return null;}

	public void process
	(
	   	MarvinImage a_imageIn, 
	   	MarvinImage a_imageOut, 
	  	MarvinAttributes attrIn,
	  	MarvinAttributes attrOut,
	   	MarvinImageMask a_mask,
	   	boolean a_previewMode
	)
	{
        Attributes attr = new Attributes(a_imageIn);
		int iter=20; //no of iterations
		
		// Put the color values in double array
		for (int x = 0; x < attr.width; x++) {
			for (int y = 0; y < attr.height; y++) {
                attr.matr[x][y]= a_imageIn.getIntComponent0(x, y);
                attr.matg[x][y]= a_imageIn.getIntComponent1(x, y);
                attr.matb[x][y]= a_imageIn.getIntComponent2(x, y);
			}
		}
		
		// Call denoise function 		
        attr.matr=denoise(attr.matr,iter, attr);
        attr.matg=denoise(attr.matg,iter, attr);
        attr.matb=denoise(attr.matb,iter, attr);
		
				
		for (int x = 0; x < attr.width; x++) {
				for (int y = 0; y < attr.height; y++) {
					a_imageOut.setIntColor(x,y,(int)truncate(attr.matr[x][y]),(int)truncate(attr.matg[x][y]),(int)truncate(attr.matb[x][y]));
				}
		}
	}
	
	
    //Function : denoise - Restores noisy images, input- double array containing color data  
	public double[][] denoise(double mat[][],int iter, Attributes attr)
	{
        attr.img_org=new double[attr.width][attr.height];
		double img_res[][]=new double[attr.width][attr.height];
		double l_currentNum;
		double l_currentDen;
		int val=1;double lam=0;
		double dt=0.4;


        attr.img_org=mat;
	
		//Perform iterations
		
		for (int it = 0;it<iter;it++)
		{
			//compute derivatives  
            attr.img_x=diff_x(mat, attr);
            attr.img_y=diff_y(mat, attr);
            attr.img_xx=diff_xx(mat, attr);
            attr.img_yy=diff_yy(mat, attr);
            attr.img_xy=diff_xy(mat, attr);
					
			for(int i = 0;i<attr.width;i++)
			{
				for (int j=0;j<attr.height;j++)
				{	
					double a= attr.img_xx[i][j]*(val+Math.pow(attr.img_y[i][j],2));
					double b=(2*attr.img_x[i][j]*attr.img_y[i][j]*attr.img_xy[i][j]);
					double c=(attr.img_yy[i][j]*(val+Math.pow(attr.img_x[i][j],2)));
					l_currentNum= a-b+c;
					l_currentDen=Math.pow((val+Math.pow(attr.img_x[i][j],2)+Math.pow(attr.img_y[i][j],2)),(1.5));
					img_res[i][j]=(l_currentNum/l_currentDen)+ lam*(attr.img_org[i][j]-mat[i][j]);
					mat[i][j]=mat[i][j]+dt*img_res[i][j];//evolve image by dt.
				}		
			}
			
		}// end of iterations.
		return mat;
	}
	
	
	
	// Function : diff_x - To compute differntiation along x axis. 
	public double[][] diff_x(double matx[][], Attributes attr)
	{
        attr.mat3=new double[attr.width][attr.height];
		double mat1,mat2;
		for(int i = 0;i<attr.width;i++)
		  {
			for (int j=0;j<attr.height;j++)
			{				
			   if (j==0)
			   {
				   mat1=matx[i][j];
				   mat2=matx[i][j+1];
			   }
			   else if (j==attr.height-1)
			   {
				   mat1=matx[i][j-1];
				   mat2=matx[i][j];
			   }
			   else 
			   {
				   mat1=matx[i][j-1];
				   mat2=matx[i][j+1];
			   }
                attr.mat3[i][j]=(mat2-mat1)/2;
			}
		  }
		return attr.mat3;
	}
	
	
	
	// Function : diff_y -To compute differntiation along y axis.
	public double[][] diff_y(double maty[][], Attributes attr)
	{
        attr.mat3=new double[attr.width][attr.height];
		double mat1,mat2;
		for(int i = 0;i<attr.width;i++)
		  {
			for (int j=0;j<attr.height;j++)
			{				
			  if (i==0)
			   {
				   mat1=maty[i][j];
				   mat2=maty[i+1][j];
			   }
			   else if (i==attr.width-1)
			   {
				   mat1=maty[i-1][j];
				   mat2=maty[i][j];
			   }
			   else 
			   {
				   mat1=maty[i-1][j];
				   mat2=maty[i+1][j];
			   }
                attr.mat3[i][j]=(mat2-mat1)/2;
			}
		  }
	     // maty= subMatrix(mat2,mat1,width,height);
		  
		return attr.mat3;
	   }
	
	
	//Function : diff_xx -To compute second order differentiation along x axis.
	public double[][] diff_xx(double matxx[][], Attributes attr)
	{
        attr.mat3=new double[attr.width][attr.height];
		double mat1,mat2;
		for(int i = 0;i<attr.width;i++)
		{
			for (int j=0;j<attr.height;j++)
			 {				
				if (j==0)
				{
					mat1=matxx[i][j];
					mat2=matxx[i][j+1];
				   
				}
				else if (j==attr.height-1)
				{
					mat1=matxx[i][j-1];
					mat2=matxx[i][j];
				}	
				else 
				{
					mat1=matxx[i][j-1];
					mat2=matxx[i][j+1];
				}

                 attr.mat3[i][j]=(mat1+mat2-2*matxx[i][j]);
			 }
				
		  }
		 return attr.mat3;
	}		
	
	
	
	//Function : diff_yy - To compute second order differentiation along y axis.
	public double[][] diff_yy(double matyy[][], Attributes attr)
	{
        attr.mat3=new double[attr.width][attr.height];
		double mat1,mat2;
		for(int i = 0;i<attr.width;i++)
		  {
			for (int j=0;j<attr.height;j++)
			{				
			   if (i==0)
			   {
				   mat1=matyy[i][j];
				   mat2=matyy[i+1][j];
			   }
			   else if (i==attr.width-1)
			   {
				   mat1=matyy[i-1][j];
				   mat2=matyy[i][j];
			   }
			   else 
			   {
				   mat1=matyy[i-1][j];
				   mat2=matyy[i+1][j];
			   }
                attr.mat3[i][j]=(mat1+mat2-2*matyy[i][j]);
			}
		  }
		return attr.mat3;
		
	}
		 
	
	
	//Function: diff_xy  -To compute differentiation along xy direction
	public double[][] diff_xy(double matxy[][], Attributes attr)
	{

        attr.mat3=new double[attr.width][attr.height];
		double Dp;
		double Dm;
				
		for(int i = 0;i<attr.width-1;i++)
		  {
			for (int j=0;j<attr.height-1;j++)
			{
                attr.mat1[i][j]=matxy[i+1][j+1];
                attr.mat2[i+1][j+1]=matxy[i][j];
                attr.mat3[i+1][j]=matxy[i][j+1];
                attr.mat4[i][j+1]=matxy[i+1][j];
			}

		  }
		
		
		 for(int i = 0;i<attr.width;i++)
		  {
			for (int j=0;j<attr.height;j++)
			{	
				 if (j==attr.height-1 && i<attr.width-1)
                     attr.mat1[i][j]=attr.mat1[i][j-1];
				 
				 else if(i==attr.width-1)
                     attr.mat1[i][j]=attr.mat1[i-1][j];
		         
		         
		         if (i==0 && j>0)
                     attr.mat2[i][j]= attr.mat2[1][j];
		         else if(j==0)
                     attr.mat2[i][0]= attr.mat2[i][1];
		         		         
		         if (i==0 && j<attr.height-1)
                     attr.mat3[i][j]= attr.mat3[1][j];
		         else if (j==attr.height-1)
                     attr.mat3[i][j]=attr.mat3[i][j-1];
		         		         
		         if (j==0 && i<attr.width-1)
                     attr.mat4[i][j] = attr.mat4[i][1];
		         else if(i==attr.width-1)
                     attr.mat4[i][j]=attr.mat4[i-1][j];
			}
              attr.mat2[0][0]=attr.mat2[0][1];
		  }
        	 
		 for (int i=0;i<attr.width;i++)
		 {
			 for(int j=0;j<attr.height;j++)
			 {
		      Dp= attr.mat1[i][j]+attr.mat2[i][j] ;
		      Dm= attr.mat3[i][j]+attr.mat4[i][j] ;
                 attr.mata[i][j]= ((Dp-Dm)/4);
			 }
		 }
			 
		 return attr.mata;
		
	}

	
	public double truncate(double a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	
	private class Attributes{
        public double mat1[][],mat2[][],mat3[][],mat4[][],mata[][];
        public double img_x[][],img_y[][],img_xx[][],img_yy[][],img_xy[][];
        public double matr[][],matg[][],matb[][];
        public double img_org[][];
        public int width;
        public int height;
        public Attributes(MarvinImage image){
            width=image.getWidth();
            height=image.getHeight();

            mat1=new double[width][height];
            mat2=new double[width][height];
            mat4=new double[width][height];
            mata=new double[width][height];

            img_x=new double[width][height];
            img_y=new double[width][height];
            img_xx=new double[width][height];
            img_yy=new double[width][height];
            img_xy=new double[width][height];

            matr=new double[width][height];
            matg=new double[width][height];
            matb=new double[width][height];
        }
    }
}
