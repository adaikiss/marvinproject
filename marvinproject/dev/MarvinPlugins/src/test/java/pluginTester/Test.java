package pluginTester;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.color.blackAndWhite.BlackAndWhite;
import org.marvinproject.image.color.brightnessAndContrast.BrightnessAndContrast;
import org.marvinproject.image.color.colorChannel.ColorChannel;
import org.marvinproject.image.color.emboss.Emboss;
import org.marvinproject.image.color.grayScale.GrayScale;
import org.marvinproject.image.color.invert.Invert;
import org.marvinproject.image.color.sepia.Sepia;
import org.marvinproject.image.color.skinColorDetection.ColorSpaceConverter;
import org.marvinproject.image.color.skinColorDetection.SkinColorDetection;
import org.marvinproject.image.color.thresholding.Thresholding;
import org.marvinproject.image.combine.combineByMask.CombineByMask;
import org.marvinproject.image.combine.combineByTransparency.CombineByTransparency;

import java.awt.*;
import java.io.File;

/**
 * @author hlw
 * @date 2016/9/25.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        PluginConfig[] configs = new PluginConfig[]{
                new PluginConfig("emboss", Emboss.class),
                new PluginConfig("gray_scale", GrayScale.class),
                new PluginConfig("color_channel", ColorChannel.class).setAttribute(ColorChannel.ATTR_RED, 10),
                new PluginConfig("brightness_and_contrast", BrightnessAndContrast.class).setAttribute(BrightnessAndContrast.ATTR_BRIGHTNESS, 100).setAttribute(BrightnessAndContrast.ATTR_CONTRAST, 100),
                new PluginConfig("black_and_white", BlackAndWhite.class).setAttribute(BlackAndWhite.ATTR_LEVEL, 20),
                new PluginConfig("invert", Invert.class),
                new PluginConfig("sepia", Sepia.class),
                new PluginConfig("skin_color_detection", SkinColorDetection.class),
                new PluginConfig("color_space_converter", ColorSpaceConverter.class),
                new PluginConfig("thresholding", Thresholding.class),
                new PluginConfig("combine_by_mask", CombineByMask.class).setAttribute(CombineByMask.ATTR_COMBINATION_IMAGE, MarvinImageIO.loadImage("E:\\test\\raw3.jpg"))
                        .setAttribute(CombineByMask.ATTR_COLOR_MASK, new Color(227, 182, 153)),
                new PluginConfig("combine_by_transparency", CombineByTransparency.class).setAttribute(CombineByTransparency.ATTR_COMBINATION_IMAGE, MarvinImageIO.loadImage("E:\\test\\raw3.jpg"))
                        .setAttribute(CombineByTransparency.ATTR_TRANSPARENCY, 20).setAttribute(CombineByTransparency.ATTR_XI, 200).setAttribute(CombineByTransparency.ATTR_YI, 200),
        };
        MarvinImage image = MarvinImageIO.loadImage("E:\\test\\raw2.jpg");
        MarvinImage output;
        for (PluginConfig config : configs) {
            output = MarvinImageIO.loadImage("E:\\test\\raw2.jpg");
            PluginFactory.get(config.type).process(image, output, config.attributes, null, new MarvinImageMask(image.getWidth(), image.getHeight(), image.getWidth()/2, 0, image.getWidth()/2, image.getHeight()), false);
            output.update();
            String outputPath = "E:\\test\\" + config.name + ".jpg";
            FileUtils.deleteQuietly(new File(outputPath));
            MarvinImageIO.saveImage(output, outputPath);
        }
    }

    static class PluginConfig {
        final String name;
        final Class<? extends MyMarvinImagePlugin> type;
        final MarvinAttributes attributes;
//        final MarvinImageMask mask;

        PluginConfig(String name, Class<? extends MyMarvinImagePlugin> type) {
            this.name = name;
            this.type = type;
            this.attributes = new MarvinAttributes();
//            this.mask = mask;
        }

        public PluginConfig setAttribute(String name, Object value) {
            this.attributes.set(name, value);
            return this;
        }
    }
}
