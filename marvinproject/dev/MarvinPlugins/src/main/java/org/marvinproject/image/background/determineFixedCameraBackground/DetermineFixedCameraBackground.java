package org.marvinproject.image.background.determineFixedCameraBackground;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class DetermineFixedCameraBackground extends MarvinAbstractImagePlugin{
	public void load(){
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
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
		int[][][][] weights = new int[imageIn.getWidth()][imageIn.getHeight()][3][26];
//		boolean[][] l_arrMask = mask.getMaskArray();
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
//				if(l_arrMask != null && !l_arrMask[x][y]){
//					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), imageIn.getIntColor(x, y));
//					continue;
//				}
				int red 	= imageIn.getIntComponent0(x, y);
				int green 	= imageIn.getIntComponent1(x, y);
				int blue 	= imageIn.getIntComponent2(x, y);
				
				
				weights[x][y][0][red/10]++;
				weights[x][y][1][green/10]++;
				weights[x][y][2][blue/10]++;
				
				
				imageOut.setIntColor(x, y, 255, getProbableColor(weights[x][y][0]), getProbableColor(weights[x][y][1]), getProbableColor(weights[x][y][2]));
			}
		}	
	}
	
	private int getProbableColor(int[] arr){
		int max = -1;
		int maxIndex = 0;
		
		for(int i=0; i<arr.length; i++){
		if(max == -1 || arr[i] > max){
				max = arr[i];
				maxIndex = i;
			}
		}
		
		return maxIndex*10;
	}
}
