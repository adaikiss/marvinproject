/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.edge.prewitt;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.math.MarvinMath;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.convolution.Convolution;

/**
 * @author Gabriel Ambrosio Archanjo
 */
public class Prewitt extends MarvinAbstractImagePlugin{
	public static final String ATTR_INTENSITY = "intensity";
	private MarvinImagePlugin 	convolution;
	
	public void load(){
		convolution = MarvinPluginFactory.get(Convolution.class);
		setAttribute(ATTR_INTENSITY, 1.0);
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
		double[][] matrixPrewittX = new double[][]{
				{1,		0,		-1},
				{1,		0,		-1},
				{1,		0,		-1}
		};

		double[][] matrixPrewittY = new double[][]{
				{1,		1,		1},
				{0,		0,		0},
				{-1,	-1,		-1}
		};
		double intensity = (Double)getAttribute(ATTR_INTENSITY, attrIn);

        MarvinAttributes attr = new MarvinAttributes();
        if(intensity == 1){
            attr.set(Convolution.ATTR_MATRIX, matrixPrewittX);
			convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
            attr.set(Convolution.ATTR_MATRIX, matrixPrewittY);
			convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
		} else{
            attr.set(Convolution.ATTR_MATRIX,  MarvinMath.scaleMatrix(matrixPrewittX, intensity));
			convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
            attr.set(Convolution.ATTR_MATRIX,  MarvinMath.scaleMatrix(matrixPrewittY, intensity));
			convolution.process(imageIn, imageOut, attr, null, mask, previewMode);
		}
    }
}
