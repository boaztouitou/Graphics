package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Ray;
import RayTracing.Vector;

public class Plane extends Surface {
    public Vector Normal;
    public double Offset;

    public Plane() {
    }

    public Plane(Vector point, Vector normal) {
        Vector planeNormalIntersection = point.ProjectOn(normal);
        double offsetDirection = (Vector.PointOnLine(Vector.zero(), normal.MultiplyByScalar(999), point))
                ? 1 : -1;
        Normal = normal.normalized();
        Offset = planeNormalIntersection.length() * offsetDirection;
    }

    @Override
    public Intersection GetIntersection(Ray ray) {

        if (Math.abs(ray.V.DotProduct(Normal)) < 0.0001)//parallel
            return null;

        Vector pointOnPlane = Normal.MultiplyByScalar(Offset);
        double d = pointOnPlane.minus(ray.P0).DotProduct(Normal) / ray.V.DotProduct(Normal);
        if (d < 0.00001)
            return null;
        Vector intersectionPoint = ray.P0.plus(ray.V.normalized().MultiplyByScalar(d));
        if(ray.V.DotProduct(Normal)>0)
        	return new Intersection(d, this, ray, intersectionPoint, Normal.MultiplyByScalar(-1));
        return new Intersection(d, this, ray, intersectionPoint, Normal);
    }
}
