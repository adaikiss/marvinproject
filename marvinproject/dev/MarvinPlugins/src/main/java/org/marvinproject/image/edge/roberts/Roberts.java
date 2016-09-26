/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.edge.roberts;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import org.marvinproject.image.convolution.Convolution;

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class Roberts extends MarvinAbstractImagePlugin{

	private MarvinImagePlugin 	convolution;
	
	public void load(){
		convolution = MarvinPluginFactory.get(Convolution.class);
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
		double[][] matrixRobertsX = new double[][]{
				{1,		0},
				{0,		-1}
		};

		double[][] matrixRobertsY = new double[][]{
				{0,		1},
				{-1,	0}
		};
		MarvinAttributes attr = new MarvinAttributes();

		attr.set(Convolution.ATTR_MATRIX, matrixRobertsX);
		convolution.process(imageIn, imageOut, attr, null, mask, previewMode);

		attr.set(Convolution.ATTR_MATRIX, matrixRobertsY);
		convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
    }
}
