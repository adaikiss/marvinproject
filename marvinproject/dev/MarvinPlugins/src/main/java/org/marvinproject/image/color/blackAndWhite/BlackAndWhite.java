package org.marvinproject.image.color.blackAndWhite;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import org.marvinproject.image.color.grayScale.GrayScale;

public class BlackAndWhite extends MarvinAbstractImagePlugin{
    public static final String ATTR_LEVEL = "level";
	private final static double MAX_RLEVEL = 0.03;
	private MarvinImagePlugin grayScale;
	
	@Override
	public void load() {
		grayScale = MarvinPluginFactory.get(GrayScale.class);
		setAttribute(ATTR_LEVEL, 10);
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
		grayScale.process(imageIn, imageOut);
		int level = (Integer)getAttribute(ATTR_LEVEL);
		double rlevel = (level/100.0)*MAX_RLEVEL;
		
		int gray;
		boolean[][] l_arrMask = mask.getMaskArray();
		for(int y=0; y<imageOut.getHeight(); y++){
			for(int x=0; x<imageOut.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
//					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), imageIn.getIntColor(x, y));
					continue;
				}
				gray = imageOut.getIntComponent0(x, y);
				
				
				if(gray <= 127){
					gray = (int)Math.max((gray * (1 - ((127-gray)*rlevel))),0);
				}
				else{
					gray = (int)Math.min(gray* (1+((gray-127)*rlevel)), 255);
				}
				
				imageOut.setIntColor(x, y, gray, gray, gray);
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
