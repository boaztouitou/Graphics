package RayTracing;

import RayTracing.Surfaces.Surface;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    public java.util.List<Material> materials = new ArrayList<>();
    //  public Camera camera;
    //  public SceneSettings sceneSettings;
    public List<Surface> surfaces = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();
    public Color BackgroundColor;
    public int RootNumberOfShadowRays;
    public int MaximumRecursion;
    Vector CamPosition = null;
    Vector LookDirection = null;
    Vector UpVector = null;
    Vector LeftVector = null;
    double ScreenDistance;
    double ScreenWidth;
    double ScreenHeight;
    int DimWidth;
    int DimHeight;
    Vector ScreenTopLeft;
    Color[][] image;
    Vector[][] PixelLocations;

    public Scene(Vector camPos, Vector lookatPoint, Vector upVector,
                 double screen_width, double screen_distance, int dim_width, int dim_height) {
        SetCameraPosition(camPos, lookatPoint, upVector);
        SetDimensions(dim_width, dim_height);
        SetScreen(screen_distance, screen_width);
    }

    public void SetCameraPosition(Vector camPos, Vector lookAtPoint, Vector upVector) {
        CamPosition = camPos;
        LookDirection = Vector.minus(lookAtPoint, camPos);
        LookDirection =  LookDirection.normalized();
      //  LookDirection = lookAtPoint;
        UpVector = upVector.minus(upVector.ProjectOn(LookDirection));
        UpVector = UpVector.normalized();
        LeftVector = Vector.CrossProduct(UpVector, LookDirection);
        LeftVector = LeftVector.normalized();
    }

    public void SetDimensions(int width, int height) {
        DimWidth = width;
        DimHeight = height;
        image = new Color[height][width];
        PixelLocations = new Vector[height][width];
    }

    public void SetScreen(double distance, double width) {
        if (CamPosition == null) throw new InvalidParameterException("Cam position not set when setting screen");
        ScreenDistance = distance;
        ScreenWidth = width;
        ScreenHeight = width * (((double) DimHeight) / DimWidth);
        Vector screenCentre = CamPosition.plus(LookDirection.MultiplyByScalar(ScreenDistance));
        Vector leftBoundary = screenCentre.plus(LeftVector.MultiplyByScalar(ScreenWidth / 2));
        Vector topLeft = leftBoundary.plus(UpVector.MultiplyByScalar(ScreenHeight/2));
        //Fill Pixel Locations
        double pixelWidth = width / DimWidth;
        double pixelHeight = width / DimHeight;


        for (int i = 0; i < PixelLocations.length; i++) {
            for (int j = 0; j < PixelLocations[i].length; j++) {
                PixelLocations[i][j] = topLeft.minus(LeftVector.MultiplyByScalar(pixelWidth * i))
                        .minus(UpVector.MultiplyByScalar(pixelHeight * j));
            }
        }

    }

    public Ray ConstructRayThroughPixel(int i, int j) {
        if (CamPosition == null) throw new IllegalArgumentException("Cam Position not set");
      //  System.out.println("creating ray " + i + "," + j);
        return new Ray(CamPosition, PixelLocations[i][j].minus(CamPosition));
    }

    public Intersection FindIntersection(Ray ray) {
        Intersection hit = null;
        for (Surface surface : surfaces) {
            Intersection newHit = surface.GetIntersection(ray);
            hit = Intersection.minimal(hit, newHit);
        }
        return hit;
    }

}
