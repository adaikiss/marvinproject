/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.transform.flip;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Flip image horintally and vertically.
 * @author Gabriel Ambrosio Archanjo
 * @version 02/28/2008
 */
public class Flip extends MarvinAbstractImagePlugin
{
	public static final String ATTR_FLIP = "flip";
	public final static String FLIP_HORIZONTAL = "horizontal";
	public final static String FLIP_VERTICAL = "vertical";

	public void load(){
		setAttribute(ATTR_FLIP, FLIP_HORIZONTAL);
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
		boolean a_previewMode
	)
	{
        boolean[][]				arrMask;
		String l_operation = (String)getAttribute(ATTR_FLIP, a_attributesIn);
		arrMask = a_mask.getMaskArray();
		
		if(l_operation.equals(FLIP_HORIZONTAL)){
			flipHorizontal(a_imageIn, a_imageOut, arrMask);
		}
		else{
			flipVertical(a_imageIn, a_imageOut, arrMask);
		}
	}

	private void flipHorizontal(MarvinImage a_imageIn, MarvinImage a_imageOut, boolean[][]				arrMask){
		int r,g,b;
		for (int y = 0; y < a_imageIn.getHeight(); y++) {
			for (int x = 0; x < (a_imageIn.getWidth()/2)+1; x++) {	
				if(arrMask != null && !arrMask[x][y]){
					continue;
				}
				//Get Y points and change the positions 
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);

				a_imageOut.setIntColor(x,y,
						a_imageIn.getAlphaComponent(x, y),
						a_imageIn.getIntComponent0((a_imageIn.getWidth()-1)-x, y),
						a_imageIn.getIntComponent1((a_imageIn.getWidth()-1)-x, y),
						a_imageIn.getIntComponent2((a_imageIn.getWidth()-1)-x, y)
				);

				int nx = (a_imageIn.getWidth() - 1) - x;
				a_imageOut.setIntColor(nx, y, a_imageIn.getAlphaComponent(nx, y), r, g, b);
			}
		}
	}

	private void flipVertical(MarvinImage a_imageIn, MarvinImage a_imageOut, boolean[][]				arrMask){
		int r,g,b;
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < (a_imageIn.getHeight()/2)+1; y++) {
				if(arrMask != null && arrMask[x][y]){
					continue;
				}
			
				//Get X points and change the positions 
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);

				a_imageOut.setIntColor(x,y,
						a_imageIn.getAlphaComponent(x,y),
						a_imageIn.getIntComponent0(x, (a_imageIn.getHeight()-1)-y),
						a_imageIn.getIntComponent1(x, (a_imageIn.getHeight()-1)-y),
						a_imageIn.getIntComponent2(x, (a_imageIn.getHeight()-1)-y)
				);

				int ny = (a_imageIn.getHeight() - 1) - y;
				a_imageOut.setIntColor(x, ny, a_imageIn.getAlphaComponent(x,ny), r, g, b);
			}
		}
	}
}