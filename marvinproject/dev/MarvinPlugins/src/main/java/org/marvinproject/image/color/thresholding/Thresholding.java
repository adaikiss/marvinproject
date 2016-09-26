/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.thresholding;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import org.marvinproject.image.color.grayScale.GrayScale;

/**
 * Thresholding
 * @author Gabriel Ambrosio Archanjo
 */
public class Thresholding extends MarvinAbstractImagePlugin{
    public static final String ATTR_THRESHOLD = "threshold";
    public static final String ATTR_NEIGHBORHOOD = "neighborhood";
    public static final String ATTR_RANGE = "range";
	private MarvinAttributesPanel	attributesPanel;
//	private MarvinAttributes		attributes;
//	private int 					threshold,
//									neighborhood,
//									range;
	
	private MarvinImagePlugin pluginGray;
	
	public void load(){
		
		// Attributes
//		attributes = getAttributes();
		setAttribute(ATTR_THRESHOLD, 125);
		setAttribute(ATTR_NEIGHBORHOOD, -1);
		setAttribute(ATTR_RANGE, -1);
		
		pluginGray = MarvinPluginFactory.get(GrayScale.class);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblThreshold", "Threshold");
			attributesPanel.addTextField("txtThreshold", ATTR_THRESHOLD, getAttributes());
			attributesPanel.addLabel("lblNeighborhood", "Neighborhood");
			attributesPanel.addTextField("txtNeighborhood", ATTR_NEIGHBORHOOD, getAttributes());
			attributesPanel.addLabel("lblRange", "Range");
			attributesPanel.addTextField("txtRange", ATTR_RANGE, getAttributes());
		}
		return attributesPanel;
	}
	
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
		int 					threshold,
				neighborhood,
				range;
		threshold = (Integer)getAttribute(ATTR_THRESHOLD, attributesIn);
		neighborhood = (Integer)getAttribute(ATTR_NEIGHBORHOOD, attributesIn);
		range = (Integer)getAttribute(ATTR_RANGE, attributesIn);
		
		pluginGray.process(imageIn, imageOut, attributesIn, attributesOut, mask, previewMode);
		
		boolean[][] bmask = mask.getMaskArray();
		
		if(neighborhood == -1 && range == -1){
			hardThreshold(imageIn, imageOut, bmask, threshold);
		}
		else{
			contrastThreshold(imageIn, imageOut, neighborhood);
		}
				
	}
	
	private void hardThreshold(MarvinImage imageIn, MarvinImage imageOut, boolean[][] mask, int threshold){
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(mask != null && !mask[x][y]){
					continue;
				}
				
				if(imageIn.getIntComponent0(x,y) < threshold){
					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x,y), 0,0,0);
				}
				else{
					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x,y), 255,255,255);
				}				
			}
		}	
	}
	
	private void contrastThreshold
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
        int neighborhood
	){
		int range = 1;
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(checkNeighbors(x,y, neighborhood, neighborhood, imageIn, range)){
					imageOut.setIntColor(x,y,0,0,0);
				}
				else{
					imageOut.setIntColor(x,y,255,255,255);
				}
			}
		}
	}
	
	private boolean checkNeighbors(int x, int y, int neighborhoodX, int neighborhoodY, MarvinImage img, int range){
		
		int color;
		int z=0;
		
		color = img.getIntComponent0(x, y);
		
		for(int i=0-neighborhoodX; i<=neighborhoodX; i++){
			for(int j=0-neighborhoodY; j<=neighborhoodY; j++){
				if(i == 0 && j == 0){
					continue;
				}
				
				if(color < getSafeColor(x+i,y+j, img)-range && getSafeColor(x+i,y+j, img) != -1){
					z++;
				}
			}
		}
		
		if(z > (neighborhoodX*neighborhoodY)*0.5){
			return true;
		}
		
		return false;
	}
	
	private int getSafeColor(int x, int y, MarvinImage img){
		
		if(x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()){
			return img.getIntComponent0(x, y);
		}
		return -1;
	}
}
