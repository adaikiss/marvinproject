/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.subtract;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Subtract an image from another
 * @author Gabriel Ambrosio Archanjo
 */
public class Subtract extends MarvinAbstractImagePlugin{
    public static final String ATTR_COLOR_RANGE = "colorRange";
    public static final String ATTR_BACKGROUND_IMAGE = "backgroundImage";
	public void load(){
		setAttribute(ATTR_COLOR_RANGE, 30);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
    
    
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
		MarvinImage imageBackground;
		int colorRange;
    	imageBackground = (MarvinImage)getAttribute(ATTR_BACKGROUND_IMAGE, a_attributesIn);
    	colorRange = (Integer)getAttribute(ATTR_COLOR_RANGE, a_attributesIn);
    	
    	int l_red,
    		l_redBg,
    		l_green,
    		l_greenBg,
    		l_blue,
    		l_blueBg;
    	
    	for(int y=0; y<a_imageIn.getHeight(); y++){
    		for(int x=0; x<a_imageIn.getWidth(); x++){
    			
    			l_redBg = imageBackground.getIntComponent0(x, y);
    			l_greenBg = imageBackground.getIntComponent1(x, y);
    			l_blueBg = imageBackground.getIntComponent2(x, y);
    			
    			l_red = a_imageIn.getIntComponent0(x, y);
    			l_green = a_imageIn.getIntComponent1(x, y);
    			l_blue = a_imageIn.getIntComponent2(x, y);    			
    			
    			if
    			(
    				Math.abs(l_redBg-l_red)> colorRange ||
    				Math.abs(l_greenBg-l_green)> colorRange ||
    				Math.abs(l_blueBg-l_blue)> colorRange
    			)
    			{
    				a_imageOut.setIntColor(x, y, a_imageIn.getIntColor(x, y));
    			}
    			else{
    				a_imageOut.setIntColor(x, y, 0,0,255);
    			}	
    		}
    	}
	}
}
