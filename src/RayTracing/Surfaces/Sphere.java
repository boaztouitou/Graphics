package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Ray;
import RayTracing.Vector;

public class Sphere implements Surface {
    public Vector CenterPosition;
    public double Radius;
    public RayTracing.Material Material;


    @Override
    public Intersection GetIntersection(Ray ray) {
        double b = ray.V.MultiplyByScalar(2).DotProduct(ray.P0.minus(CenterPosition));
        double c = ray.P0.minus(CenterPosition).absoluteSquared() - Radius * Radius;
        double discriminant = b * b - 4 * c;
        if (discriminant < 0) {
            return null;
        }
        double d1 = (-b - Math.sqrt(discriminant))/2;
        double d2 = (-b + Math.sqrt(discriminant))/2;
        double d;
        if (d1 > 0 && d2 > 0)
            d = Math.min(d1, d2);
        else if (d1 > 0 && d2 < 0)
            d = d1;
        else if (d2 > 0 && d1 < 0)
            d = d2;
        else
            d = 0;
        Vector intersectionPoint = ray.P0.plus(ray.V.normalized().MultiplyByScalar(d));
        Vector intersectionNormal = intersectionPoint.minus(CenterPosition).normalized();
        return new Intersection(d, this, ray, intersectionPoint, intersectionNormal);
    }
}
