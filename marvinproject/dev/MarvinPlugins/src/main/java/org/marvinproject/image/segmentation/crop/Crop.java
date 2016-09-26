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

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;
	
	public void load(){
		attributes = getAttributes();
		setAttribute("x", 0);
		setAttribute("y", 0);
		setAttribute("width", 0);
		setAttribute("height", 0);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblX", "x:");
			attributesPanel.addTextField("txtX", "x", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblY", "y:");
			attributesPanel.addTextField("txtY", "y", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblWidth", "width:");
			attributesPanel.addTextField("txtWidth", "width", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblHeight", "height:");
			attributesPanel.addTextField("txtHeight", "height", attributes);
		}
		return attributesPanel;
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
		int x = (Integer)getAttribute("x");
		int y = (Integer)getAttribute("y");
		int width = (Integer)getAttribute("width");
		int height = (Integer)getAttribute("height");
		
		imageOut.setDimension(width, height);
		
		for(int i=x; i<x+width; i++){
			for(int j=y; j<y+height; j++){
				imageOut.setIntColor(i-x, j-y, imageIn.getIntColor(i, j));
			}
		}
    }
}
