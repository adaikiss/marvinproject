package org.marvinproject.image.color.colorChannel;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class ColorChannel extends MarvinAbstractImagePlugin{

	public static final String ATTR_RED = "red";
	public static final String ATTR_GREEN = "green";
	public static final String ATTR_BLUE = "blue";
	@Override
	public void load() {
		setAttribute(ATTR_RED, 0);
		setAttribute(ATTR_GREEN, 0);
		setAttribute(ATTR_BLUE, 0);
	}
	
	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrIn,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {
		
		int vr = (Integer)getAttribute(ATTR_RED);
		int vg = (Integer)getAttribute(ATTR_GREEN);
		int vb = (Integer)getAttribute(ATTR_BLUE);
		
		double mr = 1+Math.abs((vr/100.0)*2.5);
		double mg = 1+Math.abs((vg/100.0)*2.5);
		double mb = 1+Math.abs((vb/100.0)*2.5);
		
		mr = (vr > 0? mr : 1.0/mr);
		mg = (vg > 0? mg : 1.0/mg);
		mb = (vb > 0? mb : 1.0/mb);
		
		int red,green,blue;
		boolean[][] l_arrMask = mask.getMaskArray();
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				red = imageIn.getIntComponent0(x, y);
				green = imageIn.getIntComponent1(x, y);
				blue = imageIn.getIntComponent2(x, y);
				
				red 	= (int)Math.min(red * mr, 255);
				green 	= (int)Math.min(green * mg, 255);
				blue	= (int)Math.min(blue * mb, 255);
				
				imageOut.setIntColor(x, y, 255, red, green, blue);
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
	
}
