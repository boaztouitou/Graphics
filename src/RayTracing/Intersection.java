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
            	if(intersection.Distance>0) result = intersection;
            	else{
            		//System.out.println("Error intersectiong with "+intersection.Surface.getClass().getSimpleName());
            	}
        }
        return result;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "Distance=" + Distance +
                ", Surface=" + Surface +
                ", Ray=" + Ray +
                ", IntersectionPoint=" + IntersectionPoint +
                ", IntersectionNormal=" + IntersectionNormal +
                '}';
    }
}
