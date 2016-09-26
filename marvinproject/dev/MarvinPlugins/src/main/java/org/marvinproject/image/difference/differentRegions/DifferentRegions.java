/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.difference.differentRegions;

import java.util.Vector;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Find the different regions between two images.
 * @author Gabriel Ambrosio Archanjo
 */
public class DifferentRegions extends MarvinAbstractImagePlugin{
	public static final String ATTR_COMPARISON_IMAGE = "comparisonImage";
	public static final String ATTR_COLOR_RANGE = "colorRange";

	public void load(){
		setAttribute(ATTR_COLOR_RANGE, 30);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	public void process(MarvinImage a_imageIn, MarvinImage a_imageOut, MarvinAttributes a_attributesIn, MarvinAttributes a_attributesOut, MarvinImageMask a_mask, boolean a_previewMode){
		int l_redA,
		l_redB,
		l_greenA,
		l_greenB,
		l_blueA,
		l_blueB;
        MarvinImage 		comparisonImage;

        int[][] 			arrPixelsPerSubRegion;
        boolean[][] 		arrRegionMask;

        int 				width;
        int 				height;
        int					colorRange;

        int 				subRegionSide=10;
        comparisonImage = (MarvinImage)getAttribute(ATTR_COMPARISON_IMAGE, a_attributesIn);
		colorRange = (Integer)getAttribute(ATTR_COLOR_RANGE, a_attributesIn);
		width = a_imageIn.getWidth();
		height = a_imageIn.getHeight();
		
        arrPixelsPerSubRegion = new int[width/subRegionSide][height/subRegionSide];
        arrRegionMask = new boolean[width/subRegionSide][height/subRegionSide];

		clearRegions(width, height, subRegionSide, arrPixelsPerSubRegion, arrRegionMask);
		
		for(int y=0; y<a_imageIn.getHeight(); y++){
			for(int x=0; x<a_imageIn.getWidth(); x++){
				
				l_redA = a_imageIn.getIntComponent0(x, y);
				l_greenA = a_imageIn.getIntComponent1(x, y);
				l_blueA = a_imageIn.getIntComponent2(x, y);
				
				l_redB = comparisonImage.getIntComponent0(x, y);
				l_greenB = comparisonImage.getIntComponent1(x, y);
				l_blueB = comparisonImage.getIntComponent2(x, y);
				
				if
				(
					Math.abs(l_redA-l_redB)> colorRange ||
					Math.abs(l_greenA-l_greenB)> colorRange ||
					Math.abs(l_blueA-l_blueB)> colorRange
				)
				{
					arrPixelsPerSubRegion[x/subRegionSide][y/subRegionSide]++;
				}
			}
		}		
		
		Vector<int[]> l_vecRegions = new Vector<int[]>();
		int[] l_rect;
	
		while(true){
			l_rect = new int[4];
			l_rect[0] = -1;
			
			JoinRegions(l_rect, width, height, subRegionSide, arrPixelsPerSubRegion, arrRegionMask);
			if(l_rect[0] != -1){
				l_vecRegions.add(l_rect);
			}
			else{
				break;
			}
		}			
		a_attributesOut.set("regions", l_vecRegions);		
	}
	
	private boolean JoinRegions(int[] a_rect, int width, int height, int subRegionSide, int[][] arrPixelsPerSubRegion, boolean[][] arrRegionMask){
		for(int x=0; x<width/subRegionSide; x++){
			for(int y=0; y<height/subRegionSide; y++){
				if(arrPixelsPerSubRegion[x][y] > (subRegionSide*subRegionSide/2) && !arrRegionMask[x][y]){
					arrRegionMask[x][y] = true;
					a_rect[0] = x*subRegionSide;
					a_rect[1] = y*subRegionSide;
					a_rect[2] = x*subRegionSide;
					a_rect[3] = y*subRegionSide;
					
					testNeighbors(a_rect, x,y, width, height, subRegionSide, arrPixelsPerSubRegion, arrRegionMask);
					return true;
				}
			}
		}
		return false;
	}
	
	private void testNeighbors(int[] a_rect, int a_x, int a_y, int width, int height, int subRegionSide, int[][] arrPixelsPerSubRegion, boolean[][] arrRegionMask){
		for(int x=a_x-5; x<a_x+5; x++){
			for(int y=a_y-5; y<a_y+5; y++){
				if
				(
					(x > 0 && x<width/subRegionSide) &&
					(y > 0 && y<height/subRegionSide)
				)
				{
					if(arrPixelsPerSubRegion[x][y] > (subRegionSide*subRegionSide/2) && !arrRegionMask[x][y]){						
						if(x*subRegionSide < a_rect[0]){
							a_rect[0] = x*subRegionSide;
						}
						if(x*subRegionSide > a_rect[2]){
							a_rect[2] = x*subRegionSide;
						}
						
						if(y*subRegionSide < a_rect[1]){
							a_rect[1] = y*subRegionSide;
						}
						if(y*subRegionSide > a_rect[3]){
							a_rect[3] = y*subRegionSide;
						}
						
						arrRegionMask[x][y] = true;
						testNeighbors(a_rect, x,y, width, height, subRegionSide, arrPixelsPerSubRegion, arrRegionMask);
					}
				}
			}				
		}
	}
	
	private void clearRegions(int width, int height, int subRegionSide, int[][] arrPixelsPerSubRegion, boolean[][] arrRegionMask){
		for(int x=0; x<width/subRegionSide; x++){
			for(int y=0; y<height/subRegionSide; y++){
				arrPixelsPerSubRegion[x][y]=0;
				arrRegionMask[x][y]=false;
			}
		}
	}
}
