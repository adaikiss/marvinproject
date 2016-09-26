/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.edge.sobel;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.convolution.Convolution;

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class Sobel extends MarvinAbstractImagePlugin{

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
		double[][] matrixSobelX = new double[][]{
				{1,		0,	-1},
				{2,		0,	-2},
				{1,		0,	-1}
		};

		double[][] matrixSobelY = new double[][]{
				{-1,	-2,		-1},
				{0,		0,		0},
				{1,		2,		1}
		};
		MarvinAttributes attr = new MarvinAttributes();

		attr.set(Convolution.ATTR_MATRIX, matrixSobelX);
		convolution.process(imageIn, imageOut, attr, null, mask, previewMode);

		attr.set(Convolution.ATTR_MATRIX, matrixSobelY);
		convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
    }
}
