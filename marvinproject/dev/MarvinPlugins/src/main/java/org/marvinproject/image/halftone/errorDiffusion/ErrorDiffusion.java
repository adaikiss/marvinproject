/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.errorDiffusion;

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
 * Halftone Error Diffusion implementation.
 * @author Gabriel Ambr�sio Archanjo
 * @version 1.0 02/28/2008

 * @author danilo
 *
 */
public class ErrorDiffusion extends MarvinAbstractImagePlugin
{
	public static final String ATTR_THRESHOLD = "threshold";

	public void load(){
		setAttribute(ATTR_THRESHOLD, 128);
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
		int color;
		double dif;
        int threshold = (Integer)getAttribute(ATTR_THRESHOLD, a_attributesIn);

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginFactory.get(GrayScale.class);
		l_filter.process(a_imageIn, a_imageOut, a_attributesIn, a_attributesOut, a_mask, a_previewMode);
		

		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for (int y = 0; y < a_imageOut.getHeight(); y++) {
			for (int x = 0; x < a_imageOut.getWidth(); x++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				color = a_imageOut.getIntComponent0(x, y);
				if(color > threshold){
					a_imageOut.setIntColor(x,y,a_imageIn.getAlphaComponent(x,y), 255,255,255);
					dif = -(255-color);
				}
				else{
					a_imageOut.setIntColor(x,y,a_imageIn.getAlphaComponent(x,y), 0,0,0);
					dif = color;
				}

				// Pixel Right
				if(x+1 < a_imageOut.getWidth()){
					color = a_imageOut.getIntComponent0(x+1,y);
					color+=(int)(0.4375*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x+1,y,a_imageIn.getAlphaComponent(x+1,y), color,color,color);

					// Pixel Right Down
					if(y+1 < a_imageOut.getHeight()){
						color = a_imageOut.getIntComponent0(x+1,y+1);
						color+=(int)(0.0625*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x+1,y+1,a_imageIn.getAlphaComponent(x+1,y+1), color,color,color);
					}
				}

				// Pixel Down
				if(y+1 < a_imageOut.getHeight()){
					color = a_imageOut.getIntComponent0(x,y+1);
					color+=(int)(0.3125*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x,y+1,a_imageIn.getAlphaComponent(x,y+1), color,color,color);

					// Pixel Down Left
					if(x-1 >= 0){
						color = a_imageOut.getIntComponent0(x-1,y+1);
						color+=(int)(0.1875*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x-1,y+1,a_imageIn.getAlphaComponent(x-1,y+1), color,color,color);
					}
				}
			}
		}
	}

	private int getValidGray(int a_value){
		if(a_value < 0) return 0;
		if(a_value > 255) return 255;
		return a_value;
	}
}