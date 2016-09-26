/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.artistic.mosaic;


import java.awt.Color;
import java.awt.Graphics;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * 马赛克
 * Mosaic implementation
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 03/01/2008
 */
public class Mosaic extends MarvinAbstractImagePlugin
{
	public final static String SHAPE_SQUARES = "squares";
	public final static String SHAPE_TRIANGLES = "triangles";

	public final static String ATTR_WIDTH = "width";
	public final static String ATTR_SHAPE = "shape";
	public final static String ATTR_BORDER = "border";

	private MarvinAttributesPanel	attributesPanel;
//	private MarvinAttributes 		attributes;
	private MarvinPerformanceMeter 	performanceMeter;

	public void load()
	{
		// Attributes
		setAttribute(ATTR_WIDTH, 6);
		setAttribute(ATTR_SHAPE, SHAPE_SQUARES);
		setAttribute(ATTR_BORDER, true);
		performanceMeter = new MarvinPerformanceMeter();
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblWidth", "Tile witdh:");
			attributesPanel.addTextField("txtwidth", ATTR_WIDTH, getAttributes());
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblWidth", "Format:");
			attributesPanel.addComboBox("combShape", ATTR_SHAPE, new Object[]{SHAPE_SQUARES, SHAPE_TRIANGLES}, getAttributes());
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblWidth", "Edge:");
			attributesPanel.addComboBox("combBorder", ATTR_BORDER, new Object[]{true, false}, getAttributes());
		}
		return attributesPanel;
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesIn,
		MarvinAttributes attributesOut,
		MarvinImageMask mask,
		boolean a_previewMode
	)
	{
		int width;
		String shape;
		boolean border;
		width = (Integer)getAttribute(ATTR_WIDTH, attributesIn);
		shape = (String)getAttribute(ATTR_SHAPE, attributesIn);
		border = (Boolean)getAttribute(ATTR_BORDER, attributesIn);

		Graphics l_graphics = imageOut.getBufferedImage().getGraphics();

		performanceMeter.enableProgressBar("Mosaic", imageIn.getHeight()*width);

		if(shape.equals(SHAPE_SQUARES)){
			squaresMosaic(width, border, l_graphics, imageIn, imageOut, mask);
		}
		else if(shape.equals(SHAPE_TRIANGLES)){
			trianglesMosaic(width, border, l_graphics, imageIn, imageOut, mask);
		}

		imageOut.updateColorArray();
		performanceMeter.finish();
	}

	private void squaresMosaic(int width, boolean border, Graphics graphics, MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		Color l_color;
		boolean[][] l_arrMask = mask.getMaskArray();
		for (int y = 0; y < imageIn.getHeight(); y+=width) {
			for (int x = 0; x < imageIn.getWidth(); x+=width) {
				if(l_arrMask != null && !l_arrMask[x][y]){
//					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), imageIn.getIntColor(x, y));
					continue;
				}
				l_color = getSquareColor(x,y,imageIn, width);
				graphics.setColor(l_color);
				graphics.fillRect((int)(x), (int)(y), (int)((width)), (int)((width)));

				if(border){
					graphics.setColor(Color.black);
					graphics.drawRect((int)(x), (int)(y), (int)((width)), (int)((width)));
				}
			}
			performanceMeter.stepsFinished(imageIn.getWidth());
		}
	}

	private void trianglesMosaic(int width, boolean border, Graphics graphics, MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		Color l_colorT1;
		Color l_colorT2;
		int t=-1;
		boolean l_aux=true;

		if
		(
			((imageIn.getWidth()/width)%2 == 0 && imageIn.getWidth()%width==0) ||
			((imageIn.getWidth()/width)%2 == 1 && imageIn.getWidth()%width!=0)
		)
		{
			l_aux=false;
		}
		boolean[][] l_arrMask = mask.getMaskArray();
		for (int y = 0; y < imageIn.getHeight(); y+=width) {
			for (int x = 0; x < imageIn.getWidth(); x+=width) {
				if(l_arrMask != null && !l_arrMask[x][y]){
//					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), imageIn.getIntColor(x, y));
					continue;
				}
				if(t ==-1)
				{
					l_colorT1 = getTriangleColor(x,y,0, imageIn, width);
					l_colorT2 = getTriangleColor(x,y,1, imageIn, width);

					graphics.setColor(l_colorT1);
					graphics.fillPolygon(new int[]{x,x+width,x}, new int[]{y,y,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x,x+width,x}, new int[]{y,y,y+width},3);
					}


					graphics.setColor(l_colorT2);
					graphics.fillPolygon(new int[]{x+width,x+width,x}, new int[]{y,y+width,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x+width,x+width,x}, new int[]{y,y+width,y+width},3);
					}
				}
				else{
					l_colorT1 = getTriangleColor(x,y,2, imageIn, width);
					l_colorT2 = getTriangleColor(x,y,3, imageIn, width);


					graphics.setColor(l_colorT1);
					graphics.fillPolygon(new int[]{x,x+width,x+width}, new int[]{y,y,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x,x+width,x+width}, new int[]{y,y,y+width},3);
					}	



					graphics.setColor(l_colorT2);
					graphics.fillPolygon(new int[]{x, x+width,x}, new int[]{y,y+width,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x, x+width,x}, new int[]{y,y+width,y+width},3);
					}
				}
				performanceMeter.stepsFinished(imageIn.getWidth());
				t*=-1;
			}
			if(l_aux){
				t*=-1;
			}
		}
	}

	private Color getSquareColor(int aX, int aY, MarvinImage image, int width){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		for(int y=0; y<width; y++){
			for(int x=0; x<width; x++)
			{
				if(aX+x > 0 && aX+x < image.getWidth() &&  aY+y> 0 && aY+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(aX+x,aY+y);
						l_green = image.getIntComponent1(aX+x,aY+y);
						l_blue = image.getIntComponent2(aX+x,aY+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(aX+x,aY+y))/2;
						l_green = (l_green+image.getIntComponent1(aX+x,aY+y))/2;
						l_blue = (l_blue+image.getIntComponent2(aX+x,aY+y))/2;
					}
				}
			}
		}
		return new Color(l_red,l_green,l_blue);
	}

	private Color getTriangleColor(int aX, int aY, int tringlePos, MarvinImage image, int width){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		int l_xInitial=0;
		int l_xOffSet=0;
		int l_xOffSetInc=0;
		int l_xInitalInc=0;

		switch(tringlePos){
			case 0:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=-1;
				l_xInitalInc = 0;
				break;
			case 1:
				l_xInitial = width-1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = -1;
				break;
			case 2:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = 1;
				break;
			case 3:
				l_xInitial = 1;
				l_xOffSet = 1;
				l_xOffSetInc=1;
				l_xInitalInc = 0;
				break;
			
		}
		
		int x = l_xInitial;
		int y = 0;

		for(int w=0; w< width-1; w++){ 
			while(x < l_xOffSet){
				if(aX+x > 0 && aX+x < image.getWidth() &&  aY+y> 0 && aY+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(aX+x,aY+y);
						l_green = image.getIntComponent1(aX+x,aY+y);
						l_blue = image.getIntComponent2(aX+x,aY+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(aX+x,aY+y))/2;
						l_green = (l_green+image.getIntComponent1(aX+x,aY+y))/2;
						l_blue = (l_blue+image.getIntComponent2(aX+x,aY+y))/2;
					}
				}
				x++;
			}
			l_xInitial+=l_xInitalInc;
			l_xOffSet+=l_xOffSetInc;
			x = l_xInitial;
			y++;
			
		}
		if(l_red == -1) l_red = 0;
		if(l_green == -1) l_green = 0;
		if(l_blue == -1) l_blue = 0;
		return new Color(l_red,l_green,l_blue);
	}
}