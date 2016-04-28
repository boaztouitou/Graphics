package RayTracing;

import RayTracing.Surfaces.Cylinder;
import RayTracing.Surfaces.Plane;
import RayTracing.Surfaces.Sphere;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

    public int imageWidth;
    public int imageHeight;
    public Scene scene;


    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {

        try {

            RayTracer tracer = new RayTracer();

            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;

            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3) {
                tracer.imageWidth = parseInt(args[2]);
                tracer.imageHeight = parseInt(args[3]);
            }

            tracer.scene = new Scene();

            // Parse scene file:
            tracer.parseScene(sceneFileName);

            // Render scene:
            tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
        } catch (RayTracerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName) {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }

    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }


    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public void parseScene(String sceneFileName) throws IOException, RayTracerException {
        FileReader fr = new FileReader(sceneFileName);

        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);


        while ((line = r.readLine()) != null) {
            line = line.trim();
            ++lineNum;

            if (line.isEmpty() || (line.charAt(0) == '#')) {  // This line in the scene file is a comment
                continue;
            } else {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

                if (code.equals("cam")) {
                    Camera camera = new Camera();
                    camera.Position = new Vector(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    camera.LookAtPosition = new Vector(parseDouble(params[3]), parseDouble(params[4]),
                            parseDouble(params[5]));
                    camera.UpVector = new Vector(parseDouble(params[6]), parseDouble(params[7]),
                            parseDouble(params[8]));
                    camera.ScreenDistance = parseDouble(params[9]);
                    camera.ScreenWidth = parseDouble(params[10]);
                    camera.ScreenHeight = ((double) imageHeight) / imageWidth * camera.ScreenWidth;
                    scene.camera = camera;
                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                } else if (code.equals("set")) {
                    SceneSettings sceneSettings = new SceneSettings();
                    sceneSettings.BackgroundColor = new Color(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    sceneSettings.RootNumberOfShadowRays = parseInt(params[3]);
                    sceneSettings.MaximumRecursion = parseInt(params[4]);
                    scene.sceneSettings = sceneSettings;
                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                } else if (code.equals("mtl")) {
                    Material material = new Material();
                    material.DiffuseColor = new Color(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    material.SpecularColor = new Color(parseDouble(params[3]), parseDouble(params[4]),
                            parseDouble(params[5]));
                    material.ReflectColor = new Color(parseDouble(params[6]), parseDouble(params[7]),
                            parseDouble(params[8]));
                    material.PhongCoeff = parseInt(params[9]);
                    material.Transparency = parseDouble(params[10]);
                    scene.materials.add(material);
                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                } else if (code.equals("sph")) {
                    Sphere sphere = new Sphere();
                    sphere.CenterPosition = new Vector(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    sphere.Radius = parseDouble(params[3]);
                    sphere.Material = scene.materials.get(parseInt(params[4]) - 1);
                    scene.surfaces.add(sphere);
                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                } else if (code.equals("pln")) {
                    Plane plane = new Plane();
                    plane.Normal = new Vector(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    plane.Offset = parseDouble(params[3]);
                    plane.Material = scene.materials.get(parseInt(params[4]) - 1);
                    scene.surfaces.add(plane);
                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                } else if (code.equals("cyl")) {
                    Cylinder cylinder = new Cylinder();
                    cylinder.CenterPosition = new Vector(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    cylinder.Length = parseDouble(params[3]);
                    cylinder.Radius = parseDouble(params[4]);
                    cylinder.Rotation = new Vector(parseDouble(params[5]), parseDouble(params[6]),
                            parseDouble(params[7]));
                    cylinder.Material = scene.materials.get(parseInt(params[8]) - 1);
                    scene.surfaces.add(cylinder);
                    System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
                } else if (code.equals("lgt")) {
                    Light light = new Light();
                    light.Position = new Vector(parseDouble(params[0]), parseDouble(params[1]),
                            parseDouble(params[2]));
                    light.Color = new Color(parseDouble(params[3]), parseDouble(params[4]),
                            parseDouble(params[5]));
                    light.SpecularIntensity = parseDouble(params[6]);
                    light.ShadowIntensity = parseDouble(params[7]);
                    light.WidthRadius = parseDouble(params[8]);
                    scene.lights.add(light);
                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                } else {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }

        // It is recommended that you check here that the scene is valid,
        // for example camera settings and all necessary materials were defined.

        System.out.println("Finished parsing scene file " + sceneFileName);

    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName) {
        long startTime = System.currentTimeMillis();

        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];


        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                Ray ray = scene.ConstructRayThroughPixel(i, j);
                Intersection hit = scene.FindIntersection(ray);
                Color color = GetColor(hit);
                rgbData[(j * imageWidth + i) * 3] = color.getRed();
                rgbData[(j * imageWidth + i) * 3 + 1] = color.getGreen();
                rgbData[(j * imageWidth + i) * 3 + 2] = color.getBlue();
            }
        }

        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
        //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
        //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255


        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);

    }

    private Color GetColor(Intersection hit){
        //TODO
        return null;
    }

    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {
            super(msg);
        }
    }


}