package org.marvinproject.image.segmentation.floodfillSegmentation;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.fill.boundaryFill.BoundaryFill;


public class FloodfillSegmentation extends MarvinAbstractImagePlugin{
    public static final String OUTPUT_SEGMENTS = "segments";
	private MarvinImagePlugin floodfill;
	
	public void load(){
		floodfill   = MarvinPluginFactory.get(BoundaryFill.class);
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
		if(attributesOut != null){
			attributesOut.set(OUTPUT_SEGMENTS, floodfillSegmentation(imageIn));
		}
	}
	
	private MarvinSegment[] floodfillSegmentation(MarvinImage image){
		MarvinImage fillBuffer = image.clone();
		fillBuffer.clear(0xFF000000);
		
		int currentColor=1;
		for(int y=0; y<image.getHeight(); y++){
			for(int x=0; x<image.getWidth(); x++){
				
				int color = fillBuffer.getIntColor(x, y);
				
				if((color & 0x00FFFFFF) == 0){
					MarvinAttributes attr = new MarvinAttributes();
					attr.set(BoundaryFill.ATTR_X, x);
					attr.set(BoundaryFill.ATTR_Y, y);
					attr.set(BoundaryFill.ATTR_COLOR, 0xFF000000 | (currentColor++));
					floodfill.process(image, fillBuffer, attr, null);
				}
			}
		}
		
		MarvinSegment[] segments = new MarvinSegment[currentColor-1];
		MarvinSegment seg;
		for(int y=0; y<fillBuffer.getHeight(); y++){
			for(int x=0; x<fillBuffer.getWidth(); x++){
				int color = (fillBuffer.getIntColor(x, y) & 0x00FFFFFF);
				
				if(color != 0x00FFFFFF && color > 0){
					
					seg = segments[color-1];
					
					if(seg == null){
						seg = new MarvinSegment();
						segments[color-1] = seg;
					}
					
					// x and width
					if(seg.x1 == -1 || x < seg.x1)	{		seg.x1 = x;		}
					if(seg.x2 == -1 || x > seg.x2)	{		seg.x2 = x;		}
					seg.width = seg.x2-seg.x1;
					
					// y and height;
					if(seg.y1 == -1 || y < seg.y1)	{		seg.y1 = y;		}
					if(seg.y2 == -1 || y > seg.y2)	{		seg.y2 = y;		}
					seg.height = seg.y2-seg.y1;
					
					seg.mass++;
				}
			}
		}
		
		return segments;
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
