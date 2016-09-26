/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.segmentation.crop;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Crop extends MarvinAbstractImagePlugin{
	/**
	 * offset x
	 */
	public static final String ATTR_X = "x";
	/**
	 * offset y
	 */
	public static final String ATTR_Y = "y";
	/**
	 * crop width
	 */
	public static final String ATTR_WIDTH = "width";
	/**
	 * crop height
	 */
	public static final String ATTR_HEIGHT = "height";

	public void load(){
		setAttribute(ATTR_X, 0);
		setAttribute(ATTR_Y, 0);
		setAttribute(ATTR_WIDTH, 0);
		setAttribute(ATTR_HEIGHT, 0);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attrIn,
		MarvinAttributes attrOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
    {
		int x = (Integer)getAttribute(ATTR_X, attrIn);
		int y = (Integer)getAttribute(ATTR_Y, attrIn);
		int width = (Integer)getAttribute(ATTR_WIDTH, attrIn);
		int height = (Integer)getAttribute(ATTR_HEIGHT, attrIn);
		
		imageOut.setDimension(width, height);
		
		for(int i=x; i<x+width; i++){
			for(int j=y; j<y+height; j++){
				imageOut.setIntColor(i-x, j-y, imageIn.getIntColor(i, j));
			}
		}
    }
}
