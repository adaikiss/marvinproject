/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.transform.skew;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Perform a vertical OR horizontal skew of an image.
 * @author Barry McCullagh
 * @version 01/30/2009
 */
public class Skew extends MarvinAbstractImagePlugin
{
	public static final String ATTR_SKEW = "skew";
    public static final String ATTR_SKEW_ANGLE = "skewAngle";
//	public static final String ATTR_SELECTED = "selected";
    public enum Type{
        Horizontal,
        Vertical
    }

	public void load(){
		setAttribute(ATTR_SKEW, Type.Horizontal);
//		setAttribute(ATTR_SELECTED, 0);
	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	/**
	 * Handles the 'Process' button being pressed. 
	 * Determines the angle to skew and if the user wants
	 * a horizontal or vertical skew.
	 * 
	 * @param a_imageIn - the image to be skewed
	 * @param a_previewMode - if a preview is being performed
	 * @return - void
	 */
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesIn,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//find the desired direction
		Type l_operation = (Type)getAttribute(ATTR_SKEW, a_attributesIn);
		//find the desired angle and convert from degrees to rads
		int s_angle = (Integer)getAttribute(ATTR_SKEW_ANGLE, a_attributesIn);
		double s_angle_rad = Math.toRadians(s_angle);
		
		if(l_operation == Type.Horizontal)
		{
			skewHorizontal(a_imageIn, a_imageOut, s_angle_rad);
		}
		else
		{
			skewVertical(a_imageIn, a_imageOut, s_angle_rad);
		}

	}
	/**
	 * Perform a horizontal skew of the input image (a_image). The maximum skew in each direction is 89 degrees.
	 * 
	 * @param a_imageIn The input image to be skewed.
	 * @param a_skewAngleRad The amount of skew to be performed.
	 * @return void
	 * @see MarvinImage
	 */
	private void skewHorizontal(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_skewAngleRad)
	{
		
		int r,g,b;
		//get image dimensions
		int l_aHeight = a_imageIn.getHeight();
		int l_aWidth = a_imageIn.getWidth();
		//a local copy of the image, used to store a copy of the original image.
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		
		//Calculate the width of the new image. 
		//l_extraWidth is not made absolute as it tells us if the skew is to be performed
		//to the left or right.
		int l_extraWidth = (int)(Math.ceil(l_aHeight * Math.tan(a_skewAngleRad)));
		int l_newWidth = l_aWidth + Math.abs(l_extraWidth); 
		
		//The modifications must be performed on a_image so erase the contents
		//of a_image so the skewed image can be put there.
		for(int xx = 0; xx < l_aWidth; xx++)
		{
			for(int yy = 0; yy < l_aHeight; yy++)
			{
				a_imageOut.setIntColor(xx, yy, a_imageIn.getAlphaComponent(xx,yy), 000000);
				
			}
				
		}
		a_imageOut.resize(l_newWidth, l_aHeight);
		
		//The top of the image is being moved to the right.
		//The amount each row moves depends on its height, calculated as l_newXcoordinate.
		if(l_extraWidth > 0)
		{
			for (int x = 0; x < l_aWidth; x++) {
				for (int y = 0; y < l_aHeight; y++){
					int l_newXcoordinate = x + (int)(Math.abs((y - l_aHeight) * Math.tan(a_skewAngleRad)));
					r = a_imageIn.getIntComponent0(x, y);
					g = a_imageIn.getIntComponent1(x, y);
					b = a_imageIn.getIntComponent2(x, y);
				
					a_imageOut.setIntColor(l_newXcoordinate, y, a_imageIn.getAlphaComponent(l_newXcoordinate, y), r, g, b);	
				}
			}
		}
		//The top of the image is being moved to the left.
		//The amount each row moves depends on its height, calculated as l_newXcoordinate
		else
		{
			for (int x = 0; x < l_aWidth; x++) {
				for (int y = l_aHeight-1; y >= 0; y--){
					int new_xcoordinate = x + (int)(Math.abs((y) * Math.tan(a_skewAngleRad)));

					r = a_imageIn.getIntComponent0(x, y);
					g = a_imageIn.getIntComponent1(x, y);
					b = a_imageIn.getIntComponent2(x, y);

					a_imageOut.setIntColor(new_xcoordinate, y, a_imageIn.getAlphaComponent(new_xcoordinate, y), r, g, b);	
				}
			}
		}
	}
	
	/**
	 * Perform a vertical skew of the input image
	 * @param a_imageIn
	 * @param a_skewAngleRad
	 * @return void
	 * @see MarvinImage
	 */
	private void skewVertical(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_skewAngleRad)
	{
		//The modifications must be applied to the image passed to the function (a_image).
		//Make a local copy of the image to store the data and modify the passed image.

		//MarvinImage l_image = (MarvinImage)(a_image.clone());
		int r, g, b;
		int l_newYcoordinate;
		//Get the image dimensions
		int l_aWidth = a_imageIn.getWidth();
		int l_aHeight = a_imageIn.getHeight();
		
		//Calculate the dimensions of the modified image then resize a_image so that 
		//can store the skewed image. The contents are also empties. 
		int l_extraHeight = (int)Math.ceil(l_aWidth * Math.tan(a_skewAngleRad));		
		int l_newHeight = l_aHeight + Math.abs(l_extraHeight);		
		for(int xx = 0; xx < l_aWidth; xx++)
		{
			for(int yy = 0; yy < l_aHeight; yy++)
			{
				a_imageOut.setIntColor(xx, yy, a_imageIn.getAlphaComponent(xx, yy), 000000);
			}
		}
		a_imageOut.resize(l_aWidth, l_newHeight);
		
		
		//Calculate the new coordinate of each pixel. If the image is being skewed
		//up or down (relative to the left hand column of pixels) a different calculation
		//must be performed
		for (int x = 0; x < l_aWidth; x++) {
			for (int y = 0; y < l_aHeight; y++){
				
				//for a skew 'down'
				if (l_extraHeight > 0)
				{
					l_newYcoordinate = y + (int)((x) * Math.tan(a_skewAngleRad));
				}
				//for a skew 'up'
				else
				{
					l_newYcoordinate = (l_newHeight) -(l_aHeight) + y - (int)Math.abs(((x) * Math.tan(a_skewAngleRad)));
				}
			
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);
				
				a_imageOut.setIntColor(x, l_newYcoordinate, a_imageIn.getAlphaComponent(x, l_newYcoordinate), r, g, b);	
			}
		}
		
	
	}
}