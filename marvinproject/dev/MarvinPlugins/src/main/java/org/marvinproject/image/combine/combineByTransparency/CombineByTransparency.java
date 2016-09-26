/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.combine.combineByTransparency;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine by transparency plug-in.
 * 
 * @author Alan Thiago do Prado
 * @version 1.0 29/01/2009
 */
public class CombineByTransparency extends MarvinAbstractImagePlugin {
	public static final String ATTR_XI = "xi";
	public static final String ATTR_YI = "yi";
	public static final String ATTR_TRANSPARENCY = "transparency";
	public static final String ATTR_COMBINATION_IMAGE = "combinationImage";
	public void load() {
		setAttribute(ATTR_XI, 0);
		setAttribute(ATTR_YI, 0);
		setAttribute(ATTR_TRANSPARENCY, 10);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesIn,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		int 					xi,
				yi;

		int 					transparency;

		MarvinImage 			combinationImage;
		xi = (Integer)getAttribute(ATTR_XI, attributesIn);
		yi = (Integer)getAttribute(ATTR_YI, attributesIn);
		combinationImage = (MarvinImage)getAttribute(ATTR_COMBINATION_IMAGE, attributesIn);
		transparency = (Integer)getAttribute(ATTR_TRANSPARENCY, attributesIn);
		
		double l_factor = (100-transparency)/100.0;
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();
		
		double 	l_redA,
				l_redB,
				l_greenA,
				l_greenB,
				l_blueA,
				l_blueB;

        boolean[][] bmask = mask.getMaskArray();
		for(int y=0; y<imageIn.getHeight(); y++){
    		for(int x=0; x<imageIn.getWidth(); x++){
                if(bmask != null && !bmask[x][y]){
                    continue;
                }
    			l_xCI = x-xi;
    			l_yCI = y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    			
    				l_redA = imageIn.getIntComponent0(x, y);
    				l_greenA = imageIn.getIntComponent1(x, y);
    				l_blueA = imageIn.getIntComponent2(x, y);
    				
    				l_redB = combinationImage.getIntComponent0(l_xCI, l_yCI);
    				l_greenB = combinationImage.getIntComponent1(l_xCI, l_yCI);
    				l_blueB = combinationImage.getIntComponent2(l_xCI, l_yCI);
    				
    				imageOut.setIntColor
    				(
    					x,
    					y,
    					(int)((l_redA*(1.0-l_factor))+(l_redB*l_factor)),
    					(int)((l_greenA*(1.0-l_factor))+(l_greenB*l_factor)),
    					(int)((l_blueA*(1.0-l_factor))+(l_blueB*l_factor))
    				);
    			}
    			else{
    				imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    			}
    		}
		}
	}
}
