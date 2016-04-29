package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Material;
import RayTracing.Ray;

public abstract class Surface {
    public abstract Intersection GetIntersection(Ray ray);
    public Material Material;
}
