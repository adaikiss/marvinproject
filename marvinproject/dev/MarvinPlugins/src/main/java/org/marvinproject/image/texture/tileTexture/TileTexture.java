/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.texture.tileTexture;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import org.marvinproject.image.transform.flip.Flip;

public class TileTexture extends MarvinAbstractImagePlugin{
	public static final String ATTR_TILE = "tile";
	public static final String ATTR_LINES = "lines";
	public static final String ATTR_COLUMNS = "columns";
	private MarvinImagePlugin 		flip;
	
	public void load(){
//		setAttribute(ATTR_TILE, -1);
		setAttribute(ATTR_LINES, 2);
		setAttribute(ATTR_COLUMNS, 2);
		
		flip = MarvinPluginFactory.get(Flip.class);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
    
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
    	int lines = (Integer)getAttribute(ATTR_LINES, attrIn);
    	int columns = (Integer)getAttribute(ATTR_COLUMNS, attrIn);
    	MarvinImage tile = (MarvinImage)getAttribute(ATTR_TILE, attrIn);
    	int tileWidth = tile.getWidth();
    	int tileHeight = tile.getHeight();
    	
    	boolean[][] arrMask = mask.getMaskArray();
    	if(arrMask != null){
    		MarvinImage.copyColorArray(imgIn, imgOut);
    	}
    	else{
    		imgOut.resize(tile.getWidth()*columns, tile.getHeight()*lines);
    	}
    	
    	MarvinImage tileFlippedH = new MarvinImage(tileWidth, tileHeight);
    	MarvinImage tileFlippedV = new MarvinImage(tileWidth, tileHeight);
    	MarvinImage tileFlippedHV = new MarvinImage(tileWidth, tileHeight);
    	MarvinAttributes attr = new MarvinAttributes();
		attr.set(Flip.ATTR_FLIP, Flip.Type.Horizontal);
    	flip.process(tile, tileFlippedH, attr, null);
    	flip.process(tile, tileFlippedHV, attr, null);

        attr.set(Flip.ATTR_FLIP, Flip.Type.Vertical);
    	flip.process(tile, tileFlippedV, attr, null);
    	flip.process(tileFlippedHV, tileFlippedHV, attr, null);
    	    	
    	for(int y=0; y<lines; y++){
    		for(int x=0; x<columns; x++){
    			if(x % 2 == 0 && y % 2 == 0){
    				copyImage(tile, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else if(y % 2 == 0){
    				copyImage(tileFlippedH, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else if(x % 2 == 0){
    				copyImage(tileFlippedV, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else{
    				copyImage(tileFlippedHV, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    		}
    		
    		
    	}
    }
    
    private void copyImage(MarvinImage tile, MarvinImage imgOut, int x, int y, boolean mask[][]){
    	for(int j=0; j<tile.getHeight(); j++){
    		for(int i=0; i<tile.getWidth(); i++){
    			if(x+i < imgOut.getWidth() && y+j < imgOut.getHeight() && (mask == null || mask[x+i][y+j])){
    				imgOut.setIntColor(i+x, j+y, tile.getIntColor(i, j));
    			}
    		}
    	}
    }
}
