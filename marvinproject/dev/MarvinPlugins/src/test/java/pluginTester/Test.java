package pluginTester;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPluginFactory;
import marvin.util.MarvinAttributes;
import org.marvinproject.image.artistic.mosaic.Mosaic;
import org.marvinproject.image.artistic.television.Television;
import org.marvinproject.image.blur.gaussianBlur.GaussianBlur;
import org.marvinproject.image.blur.pixelize.Pixelize;
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
import org.marvinproject.image.combine.mergePhotos.MergePhotos;
import org.marvinproject.image.convolution.Convolution;
import org.marvinproject.image.edge.edgeDetector.EdgeDetector;
import org.marvinproject.image.edge.prewitt.Prewitt;
import org.marvinproject.image.edge.roberts.Roberts;
import org.marvinproject.image.edge.sobel.Sobel;
import org.marvinproject.image.equalization.histogramEqualization.HistogramEqualization;
import org.marvinproject.image.fill.boundaryFill.BoundaryFill;
import org.marvinproject.image.halftone.circles.Circles;
import org.marvinproject.image.halftone.dithering.Dithering;
import org.marvinproject.image.halftone.errorDiffusion.ErrorDiffusion;
import org.marvinproject.image.halftone.rylanders.Rylanders;
import org.marvinproject.image.morphological.boundary.Boundary;
import org.marvinproject.image.morphological.closing.Closing;
import org.marvinproject.image.morphological.dilation.Dilation;
import org.marvinproject.image.morphological.erosion.Erosion;
import org.marvinproject.image.morphological.opening.Opening;
import org.marvinproject.image.quantization.grayScaleQuantization.GrayScaleQuantization;
import org.marvinproject.image.restoration.noiseReduction.NoiseReduction;
import org.marvinproject.image.segmentation.crop.Crop;
import org.marvinproject.image.segmentation.floodfillSegmentation.FloodfillSegmentation;
import org.marvinproject.image.segmentation.imageSlicer.ImageSlicer;
import org.marvinproject.image.statistical.maximum.Maximum;
import org.marvinproject.image.statistical.median.Median;
import org.marvinproject.image.statistical.minimum.Minimum;
import org.marvinproject.image.statistical.mode.Mode;
import org.marvinproject.image.subtract.Subtract;
import org.marvinproject.image.texture.tileTexture.TileTexture;
import org.marvinproject.image.transform.flip.Flip;
import org.marvinproject.image.transform.rotate.Rotate;
import org.marvinproject.image.transform.scale.Scale;
import org.marvinproject.image.transform.skew.Skew;

import javax.swing.text.Segment;
import java.awt.*;
import java.io.File;

/**
 * @author hlw
 * @date 2016/9/25.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        PluginConfig[] configs = new PluginConfig[]{
                //artistic
                new PluginConfig("mosaic", Mosaic.class),
                new PluginConfig("television", Television.class),
                //blur
                new PluginConfig("gaussian_blur", GaussianBlur.class),
                new PluginConfig("pixelize", Pixelize.class),
                //color
                new PluginConfig("black_and_white", BlackAndWhite.class).setAttribute(BlackAndWhite.ATTR_LEVEL, 20),
                new PluginConfig("brightness_and_contrast", BrightnessAndContrast.class).setAttribute(BrightnessAndContrast.ATTR_BRIGHTNESS, 100).setAttribute(BrightnessAndContrast.ATTR_CONTRAST, 100),
                new PluginConfig("color_channel", ColorChannel.class).setAttribute(ColorChannel.ATTR_RED, 10),
                new PluginConfig("emboss", Emboss.class),
                new PluginConfig("gray_scale", GrayScale.class),
                new PluginConfig("invert", Invert.class),
                new PluginConfig("sepia", Sepia.class),
                new PluginConfig("skin_color_detection", SkinColorDetection.class),
                new PluginConfig("thresholding", Thresholding.class),
                //combine
                new PluginConfig("combine_by_mask", CombineByMask.class).setAttribute(CombineByMask.ATTR_COMBINATION_IMAGE, MarvinImageIO.loadImage("E:\\test\\raw3.jpg"))
                .setAttribute(CombineByMask.ATTR_COLOR_MASK, new Color(227, 182, 153)),
                new PluginConfig("combine_by_transparency", CombineByTransparency.class).setAttribute(CombineByTransparency.ATTR_COMBINATION_IMAGE, MarvinImageIO.loadImage("E:\\test\\raw3.jpg"))
                .setAttribute(CombineByTransparency.ATTR_TRANSPARENCY, 20).setAttribute(CombineByTransparency.ATTR_XI, 200).setAttribute(CombineByTransparency.ATTR_YI, 200),
                //convolution
//                new PluginConfig("convolution", Convolution.class),
                //corner
                //comparison
                //edge
                new PluginConfig("edge_detector", EdgeDetector.class),
                new PluginConfig("prewitt", Prewitt.class),
                new PluginConfig("roberts", Roberts.class),
                new PluginConfig("sobel", Sobel.class),
                //equalization
                new PluginConfig("histogram_equalization", HistogramEqualization.class),
                //fill
                new PluginConfig("boundary_fill", BoundaryFill.class),
                //halftone
                new PluginConfig("circles", Circles.class),
                new PluginConfig("dithering", Dithering.class),
                new PluginConfig("error_diffusion", ErrorDiffusion.class),
                new PluginConfig("rylanders", Rylanders.class),
                //histogram
                //morphological
                new PluginConfig("boundary", Boundary.class),
                new PluginConfig("closing", Closing.class),
                new PluginConfig("dilation", Dilation.class),
                new PluginConfig("erosion", Erosion.class),
                new PluginConfig("opening", Opening.class),
                //pattern
                //quantization
                new PluginConfig("gray_scale_quantization", GrayScaleQuantization.class),
                //render
                //restoration
                new PluginConfig("noise_reduction", NoiseReduction.class),
                //segmentation
                new PluginConfig("crop", Crop.class).setAttribute(Crop.ATTR_WIDTH, 100).setAttribute(Crop.ATTR_HEIGHT, 100).setAttribute(Crop.ATTR_X, 10).setAttribute(Crop.ATTR_Y, 10),
                new PluginConfig("image_slicer", ImageSlicer.class).setAttribute(ImageSlicer.ATTR_COLS, 5).setAttribute(ImageSlicer.ATTR_LINES, 5),
                //statistical
                new PluginConfig("maximum", Maximum.class),
                new PluginConfig("median", Median.class),
                new PluginConfig("minimum", Minimum.class),
                new PluginConfig("mode", Mode.class),
                //steganography
                //subtract
                new PluginConfig("subtract", Subtract.class).setAttribute(Subtract.ATTR_BACKGROUND_IMAGE, MarvinImageIO.loadImage("E:\\test\\raw4.png")),
                //textture
                new PluginConfig("tile_texture", TileTexture.class).setAttribute(TileTexture.ATTR_TILE, MarvinImageIO.loadImage("E:\\test\\raw4.png")),
                //transform
                new PluginConfig("flip", Flip.class),
                new PluginConfig("rotate", Rotate.class).setAttribute(Rotate.ATTR_ROTATE_ANGLE, 45),
                new PluginConfig("scale", Scale.class).setAttribute(Scale.ATTR_NEW_WIDTH, 100),
                new PluginConfig("skew", Skew.class).setAttribute(Skew.ATTR_SKEW_ANGLE, 45),
        };
        MarvinImage image = MarvinImageIO.loadImage("E:\\test\\raw2.jpg");
        MarvinImage output;
        for (PluginConfig config : configs) {
            output = MarvinImageIO.loadImage("E:\\test\\raw2.jpg");
            MarvinPluginFactory.get(config.type).process(image, output, config.attributes, null, new MarvinImageMask(image.getWidth(), image.getHeight(), image.getWidth()/2, 0, image.getWidth()/2, image.getHeight()), false);
            output.update();
            String outputPath = "E:\\test\\" + config.name + ".jpg";
            File file = new File(outputPath);
            if(file.exists() && file.isFile()) {
                file.delete();
            }
            MarvinImageIO.saveImage(output, outputPath);
        }

//        new PluginConfig("merge_photos", MergePhotos.class).setAttribute(MergePhotos.ATTR_THRESHOLD, MarvinImageIO.loadImage("E:\\test\\raw3.jpg"))
//                .setAttribute(CombineByTransparency.ATTR_TRANSPARENCY, 20).setAttribute(CombineByTransparency.ATTR_XI, 200).setAttribute(CombineByTransparency.ATTR_YI, 200);
    }

    static class PluginConfig {
        final String name;
        final Class<? extends MarvinImagePlugin> type;
        final MarvinAttributes attributes;
//        final MarvinImageMask mask;

        PluginConfig(String name, Class<? extends MarvinImagePlugin> type) {
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
