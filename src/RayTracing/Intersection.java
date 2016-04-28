package RayTracing;


import RayTracing.Surfaces.Surface;

public class Intersection {
    public double Distance;
    public Surface Surface;
    public Ray Ray;
    public Vector IntersectionPoint;
    public Vector IntersectionNormal;

    public Intersection(double distance, Surface surface, Ray ray,
                        Vector intersectionPoint, Vector intersectionNormal) {
        Distance = distance;
        Surface = surface;
        Ray = ray;
        IntersectionPoint = intersectionPoint;
        IntersectionNormal = intersectionNormal;
    }

    public static Intersection minimal(Intersection... intersections) {
        Intersection result = null;
        for (Intersection intersection : intersections) {
            if (intersection != null && (result == null || result.Distance > intersection.Distance))
                result = intersection;
        }
        return result;
    }
}
