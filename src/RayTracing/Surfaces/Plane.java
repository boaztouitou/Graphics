package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Material;
import RayTracing.Ray;
import RayTracing.Vector;

public class Plane extends Surface {
    public Vector Normal;
    public double Offset;

    @Override
    public Intersection GetIntersection(Ray ray) {
    	
        if (Math.abs(ray.V.DotProduct(Normal)) < 0.0001)//parallel
            return null;

        Vector pointOnPlaneNormal = Normal.MultiplyByScalar(Offset);
        double d = pointOnPlaneNormal.minus(ray.P0).DotProduct(Normal) / ray.V.DotProduct(Normal);
        if (d < 0)
            return null;
        Vector intersectionPoint = ray.P0.plus(ray.V.normalized().MultiplyByScalar(d));
        return new Intersection(d, this, ray, intersectionPoint, Normal);
    }
}
