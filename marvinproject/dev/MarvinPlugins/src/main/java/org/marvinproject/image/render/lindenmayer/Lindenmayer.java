/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.lindenmayer;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
* @author Gabriel Ambr�sio Archhanjo
*/
public class Lindenmayer extends MarvinAbstractImagePlugin{
	public static final String ATTR_ROTATION_ANGLE = "rotationAngle";
	public static final String ATTR_INITIAL_ANGLE = "initialAngle";
	public static final String ATTR_ITERATIONS = "iterations";
	public static final String ATTR_RULES = "rules";
	public static final String ATTR_INITIAL_TEXT = "initialText";

	private String RULES =		"start->G\n" + 	
								"G->F[-G][+G]FG\n"+
								"F->FF\n";
	
	
	@Override
	public void load() {
		setAttribute(ATTR_ROTATION_ANGLE, 25.7);
		setAttribute(ATTR_INITIAL_ANGLE, 90.0);
		setAttribute(ATTR_ITERATIONS, 9);
		setAttribute(ATTR_RULES, RULES);
		setAttribute(ATTR_INITIAL_TEXT, "G");
	}

	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes attrIn,
		MarvinAttributes attrOut,
		MarvinImageMask a_mask,
		boolean mode
	) {
		Grammar 		grammar;
		TurtleGraphics	turtle;
		String			startText;
        grammar = new Grammar();
        turtle = new TurtleGraphics();
		String rules[] = null;
        startText = (String)getAttribute(ATTR_INITIAL_TEXT, attrIn);
		String strRules = ((String)(getAttribute(ATTR_RULES, attrIn)));
		if(strRules.contains("\n")){
			rules = ((String)(getAttribute(ATTR_RULES, attrIn))).split("\n");
		}
		else if(strRules.contains("&")){
			rules = ((String)(getAttribute(ATTR_RULES, attrIn))).split("&");
		}
		
		double initialAngle = (Double)getAttribute(ATTR_INITIAL_ANGLE, attrIn);
		double rotationAngle = (Double)getAttribute(ATTR_ROTATION_ANGLE, attrIn);
		int iterations = (Integer)getAttribute(ATTR_ITERATIONS, attrIn);
		
		
		for(int i=0; i<rules.length; i++){
//			addRule(rules[i]);
            String r[] = rules[i].split("->");

            if(r[0].equals("start")){
                startText = r[1];
            }
            else{
                grammar.addRule(r[0], r[1]);
            }
		}
		
		turtle.setStartPosition(0, 0, initialAngle);
		turtle.setRotationAngle(rotationAngle);
		
		imageOut.clear(0xFFFFFFFF);
		turtle.render(startText, grammar,iterations, imageOut);
		
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
}
