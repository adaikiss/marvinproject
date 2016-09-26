/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.difference.differenceGray;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Absolute difference between two images considering the gray scale.
 * @author Danilo Rosetto Mu�oz
 * @version 1.0 03/28/2008
 */

public class DifferenceGray extends MarvinAbstractImagePlugin{
	public static final String ATTR_COMPARISON_IMAGE = "comparisonImage";
	public void load(){}

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
		MarvinImage comparisonImage = (MarvinImage) getAttribute(ATTR_COMPARISON_IMAGE, a_attributesIn);

		//Gets the minimum width and height
		int minX = Math.min(a_imageIn.getWidth(), comparisonImage.getWidth());
		int minY = Math.min(a_imageIn.getHeight(), comparisonImage.getHeight());
		
		for (int x = 0; x < minX; x++) {
			for (int y = 0; y < minY; y++) {
				//Calculate the difference
				
				//Gets the gray scale value
				int gray = (int)((a_imageIn.getIntComponent0(x, y)*0.3) + (a_imageIn.getIntComponent1(x, y)*0.11) + (a_imageIn.getIntComponent2(x, y)*0.59));
				int gray1 = (int)((comparisonImage.getIntComponent0(x, y)*0.3) + (comparisonImage.getIntComponent1(x, y)*0.11) + (comparisonImage.getIntComponent2(x, y)*0.59));
				
				//Makes the absolute difference
				int diff = Math.abs(gray - gray1);
	            int v = (diff / 2);
				
	            //Sets the value to the new image
				a_imageOut.setIntColor(x, y, v, v, v);
			}
		}
	}
}
