/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.transform.scale;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Simple and fast scale based on nearest neighbors
 * @author Gabriel Ambr�sio Archanjo
 */
public class Scale extends MarvinAbstractImagePlugin{
	public static final String ATTR_NEW_WIDTH = "newWidth";
	public static final String ATTR_NEW_HEIGHT = "newHeight";
	public void load(){
		setAttribute(ATTR_NEW_WIDTH, 0);
		setAttribute(ATTR_NEW_HEIGHT, 0);
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
		boolean previewMode
	){
		int 					width;
		int 					height;
		int 					newWidth;
		int 					newHeight;
		if(!previewMode){
			width = a_imageIn.getWidth();
			height = a_imageIn.getHeight();
			newWidth = (Integer)getAttribute(ATTR_NEW_WIDTH, a_attributesIn);
			newHeight = (Integer)getAttribute(ATTR_NEW_HEIGHT, a_attributesIn);
			
			a_imageOut.setDimension(newWidth, newHeight);
			
		    int x_ratio = (width<<16)/newWidth;
		    int y_ratio = (height<<16)/newHeight;
		    int x2, y2 ;
		    for (int i=0;i<newHeight;i++) {
		        for (int j=0;j<newWidth;j++) {
		            x2 = ((j*x_ratio)>>16) ;
		            y2 = ((i*y_ratio)>>16) ;
		            a_imageOut.setIntColor(j,i, a_imageIn.getAlphaComponent(x2,y2), a_imageIn.getIntColor(x2,y2));
		        }                
		    }	    
		}
	}
}