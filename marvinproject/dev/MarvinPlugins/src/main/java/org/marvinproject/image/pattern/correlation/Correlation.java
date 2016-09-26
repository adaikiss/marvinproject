/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.pattern.correlation;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Correlation extends MarvinAbstractImagePlugin {
    public static final String ATTR_IMAGE_TESTE = "image_teste";
	public void load() {
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
        MarvinImage img_padrao,img_teste;
        double somaX,somaY,somaXY = 0,n,somaX2,somaY2,acima,abaixo,r,somaX2R = 0,somaY2R = 0;
        double cr;

		img_padrao = a_imageIn;
        img_teste = (MarvinImage)getAttribute(ATTR_IMAGE_TESTE, a_attributesIn);
		// the image have to same size
		if(img_teste.getWidth() == a_imageIn.getWidth() && img_teste.getHeight() == a_imageIn.getHeight()){

			n = img_padrao.getWidth() * img_padrao.getHeight();

			
			// soma X
			somaX = 0;

			for (int x = 1; x < img_padrao.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_padrao.getHeight()-1; y++) 
				{
					somaX = somaX + img_padrao.getIntComponent0(x, y);
				}
			}

			// soma Y
			somaY = 0;

			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaY = somaY + img_teste.getIntComponent0(x, y);
				}
			}
			

			// soma XR
			somaX = 0;

			for (int x = 1; x < img_padrao.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_padrao.getHeight()-1; y++) 
				{
					somaX2R = somaX2R + Math.pow (img_padrao.getIntComponent0(x, y),2);
				}
			}
			
			// soma Yr
			somaY = 0;
			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaY2R = somaY2R +  Math.pow (img_teste.getIntComponent0(x, y),2);
				}		
			}
			// soma x . y
			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaXY = somaXY + (img_teste.getIntComponent0(x, y) * img_padrao.getIntComponent0(x, y) );
				}
			}
			somaX2 = Math.sqrt(somaX);
			somaY2 = Math.sqrt(somaY);


			acima = n*(somaXY) - (somaX * somaY);
			abaixo = (Math.sqrt( n * (somaX2R) - somaX2 )) * (Math.sqrt(  n * (somaY2R) - somaY2));
			r = acima / abaixo;

			cr = r;
            a_attributesOut.set("cr", cr);
		}

	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

}
