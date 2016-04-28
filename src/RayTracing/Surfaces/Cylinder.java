package RayTracing.Surfaces;


import RayTracing.Intersection;
import RayTracing.Material;
import RayTracing.Ray;
import RayTracing.Vector;

public class Cylinder implements Surface {
    public Vector CenterPosition;
    public double Length;
    public double Radius;
    public Vector Rotation;
    public Material Material;

    @Override
    public Intersection GetIntersection(Ray ray) {
        Intersection infiniteCylinderIntersection = GetInfiniteCylinderIntersection(ray);
        if (infiniteCylinderIntersection != null && !PointIsInCylinder(infiniteCylinderIntersection.IntersectionPoint))
            infiniteCylinderIntersection = null;

        //intersect with each plane
        Intersection topIntersection = TopPlane().GetIntersection(ray);
        if (topIntersection != null && !PointIsInCylinder(topIntersection.IntersectionPoint))
            topIntersection = null;
        Intersection bottomIntersection = BottomPlane().GetIntersection(ray);
        if (bottomIntersection != null && !PointIsInCylinder(bottomIntersection.IntersectionPoint))
            bottomIntersection = null;

        return Intersection.minimal(infiniteCylinderIntersection, bottomIntersection, topIntersection);
    }

    private Intersection GetInfiniteCylinderIntersection(Ray ray) {
        //TODO
        return null;
    }

    private Plane TopPlane() {
        Plane top = new Plane();
        top.Normal = Rotation;
        top.Offset = Math.sqrt(Rotation.normalized().minus(CenterPosition)
                .MultiplyByScalar(Length / 2).absoluteSquared());
        top.Material = Material;
        return top;
    }

    private Plane BottomPlane() {
        Plane bottom = new Plane();
        bottom.Normal = Rotation.MultiplyByScalar(-1);
        bottom.Offset = Math.sqrt(Rotation.MultiplyByScalar(-1).normalized().minus(CenterPosition)
                .MultiplyByScalar(Length / 2).absoluteSquared());
        bottom.Material = Material;
        return bottom;
    }

    private boolean PointIsInCylinder(Vector point) {
        //TODO
        return false;
    }
}
