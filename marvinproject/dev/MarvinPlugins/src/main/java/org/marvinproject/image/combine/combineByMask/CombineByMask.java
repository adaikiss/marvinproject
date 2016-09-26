/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.combine.combineByMask;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine two images using a mask color.
 * @author Gabriel Ambrosio Archanjo
 */
public class CombineByMask extends MarvinAbstractImagePlugin{
	public static final String ATTR_XI = "xi";
	public static final String ATTR_YI = "yi";
	public static final String ATTR_COLOR_MASK = "colorMask";
	public static final String ATTR_COMBINATION_IMAGE = "combinationImage";


	public void load(){
		setAttribute(ATTR_XI, 0);
		setAttribute(ATTR_YI, 0);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	public void process(MarvinImage imageIn, MarvinImage imageOut, MarvinAttributes attributesIn, MarvinAttributes attributesOut, MarvinImageMask mask, boolean previewMode){
		MarvinImage 		combinationImage;

		Color 				colorMask;

		int 		xi,
				yi;
		xi = (Integer)getAttribute(ATTR_XI, attributesIn);
		yi = (Integer)getAttribute(ATTR_YI, attributesIn);
		colorMask = (Color)getAttribute(ATTR_COLOR_MASK, attributesIn);
		combinationImage = (MarvinImage)getAttribute(ATTR_COMBINATION_IMAGE, attributesIn);
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();

        boolean[][] bmask = mask.getMaskArray();
		for(int y=0; y<imageIn.getHeight(); y++){
    		for(int x=0; x<imageIn.getWidth(); x++){
                if(bmask != null && !bmask[x][y]){
                    continue;
                }
    			l_xCI = x-xi;
    			l_yCI = y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    				if(imageIn.getIntColor(x, y) == colorMask.getRGB()){
    					imageOut.setIntColor(x, y, combinationImage.getIntColor(x, y));
    				}
    				else{
    					imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    				}
    			}
    			else{
    				imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    			}
    		}
		}
	}
}
