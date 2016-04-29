package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Material;
import RayTracing.Ray;

public interface Surface {
    Intersection GetIntersection(Ray ray);
    Material GetMaterial();
}
