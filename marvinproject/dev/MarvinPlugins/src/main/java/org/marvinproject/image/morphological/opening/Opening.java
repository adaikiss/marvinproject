/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.morphological.opening;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.morphological.dilation.Dilation;
import org.marvinproject.image.morphological.erosion.Erosion;

public class Opening extends MarvinAbstractImagePlugin{
	public static final String ATTR_MATRIX = "matrix";

	private MarvinImagePlugin 	pluginDilation,
								pluginErosion;
								
	
	@Override
	public void load() {
		pluginDilation = MarvinPluginFactory.get(Dilation.class);
		pluginErosion = MarvinPluginFactory.get(Erosion.class);
		
	}

	@Override
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrIn,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean previewMode
	)
	{
		boolean[][] matrix = (boolean[][])getAttribute(ATTR_MATRIX, attrIn);
		
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY && matrix != null){
			MarvinAttributes attr = new MarvinAttributes();
			attr.set(ATTR_MATRIX, getAttribute("matrix"));
			pluginErosion.process(imgIn, imgOut, attr, attrOut, mask, previewMode);
			
			MarvinImage.copyColorArray(imgOut, imgIn);
			
			pluginDilation.process(imgIn, imgOut, attr, attrOut, mask, previewMode);
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

}
