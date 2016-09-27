/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.circles;


import java.awt.Color;
import java.awt.Graphics;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import org.marvinproject.image.color.grayScale.GrayScale;

/**
 * Halftone using circles.
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 02/28/2008
 */
public class Circles extends MarvinAbstractImagePlugin
{
    public static final String ATTR_CIRCLE_WIDTH = "circleWidth";
    public static final String ATTR_SHIFT = "shift";
    public static final String ATTR_CIRCLE_DISTANCE = "circleDistance";
	public void load(){
		setAttribute(ATTR_CIRCLE_WIDTH, 6);
		setAttribute(ATTR_SHIFT, 0);
		setAttribute(ATTR_CIRCLE_DISTANCE, 0);
	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
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
		int circleWidth;
		int shift;
		int circlesDistance;
		double l_intensity;

		circleWidth = (Integer)getAttribute(ATTR_CIRCLE_WIDTH, a_attributesIn);
		shift = (Integer)getAttribute(ATTR_SHIFT, a_attributesIn);
		circlesDistance = (Integer)getAttribute(ATTR_CIRCLE_DISTANCE, a_attributesIn);

		Graphics l_graphics = a_imageOut.getBufferedImage().getGraphics();

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginFactory.get(GrayScale.class);
		l_filter.process(a_imageIn, a_imageIn, a_attributesIn, a_attributesOut, a_mask, a_previewMode);
		

		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		int l_dif=0;
		for (int y = 0; y < a_imageIn.getHeight(); y+=circleWidth+circlesDistance) {
			for (int x = 0+l_dif; x < a_imageIn.getWidth(); x+=circleWidth+circlesDistance) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				l_intensity = getSquareIntensity(x,y,a_imageIn, circleWidth);
				l_intensity+=1.0/circleWidth;
				l_graphics.setColor(Color.white);
				l_graphics.fillRect(x,y,circleWidth+circlesDistance,circleWidth+circlesDistance);
				l_graphics.setColor(Color.black);
				l_graphics.fillArc((int)(x+(circleWidth-(l_intensity*circleWidth))/2), (int)(y+(circleWidth-(l_intensity*circleWidth))/2), (int)(l_intensity*(circleWidth)), (int)(l_intensity*(circleWidth)),1,360);
			}
			l_dif = (l_dif+shift)%circleWidth;
		}
		a_imageOut.updateColorArray();
	}

	private double getSquareIntensity(int a_x, int a_y, MarvinImage image, int circleWidth){
		double l_totalValue=0;
		for(int y=0; y<circleWidth; y++){
			for(int x=0; x<circleWidth; x++)
			{
				if(a_x+x > 0 && a_x+x < image.getWidth() &&  a_y+y> 0 && a_y+y < image.getHeight()){
					l_totalValue+= 255-(image.getIntComponent0(a_x+x,a_y+y));
				}
			}
		}
		return (l_totalValue/(circleWidth*circleWidth*255));
	}
}