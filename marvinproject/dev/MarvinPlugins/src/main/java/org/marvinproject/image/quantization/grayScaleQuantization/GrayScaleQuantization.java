/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.quantization.grayScaleQuantization;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.color.grayScale.GrayScale;

public class GrayScaleQuantization extends MarvinAbstractImagePlugin{
	public static final String ATTR_SHADES = "shades";
	private MarvinImagePlugin 		gray;
	
	@Override
	public void load() {
		gray = MarvinPluginFactory.get(GrayScale.class);
		
		setAttribute(ATTR_SHADES, 10);
	}
	
	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}

	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrIn,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview)
	{
		
		int colors = (Integer)getAttribute(ATTR_SHADES, attrIn);
		int range = 255/colors;
		gray.process(imageIn.clone(), imageIn);
		int c;
		int c2;
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				c = imageIn.getIntComponent0(x, y);
				c2 = (c/range)*range;
				
				imageOut.setIntColor(x, y, 255, c2,c2,c2);
			}
		}
	}
}
