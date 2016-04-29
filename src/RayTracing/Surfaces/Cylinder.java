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
        if (infiniteCylinderIntersection != null && !PointIsInCylinder(infiniteCylinderIntersection.IntersectionPoint,ray))
            infiniteCylinderIntersection = null;

        //intersect with each plane
        Intersection topIntersection = TopPlane().GetIntersection(ray);
        if (topIntersection != null && !PointIsInCylinder(topIntersection.IntersectionPoint,ray))
            topIntersection = null;
        Intersection bottomIntersection = BottomPlane().GetIntersection(ray);
        if (bottomIntersection != null && !PointIsInCylinder(bottomIntersection.IntersectionPoint ,ray))
            bottomIntersection = null;

        return Intersection.minimal(infiniteCylinderIntersection, bottomIntersection, topIntersection);
    }

    private Intersection GetInfiniteCylinderIntersection(Ray ray) {
        Vector p = ray.P0;
        Vector v = ray.V;
        Vector p_a = CenterPosition;
        Vector v_a = Rotation;
        double r = Radius;
        
        double A = v.minus(v_a.MultiplyByScalar(v.DotProduct(v_a))).absoluteSquared();
        double B = 2*(v.minus(v_a.MultiplyByScalar(v.DotProduct(v_a)))
        		.DotProduct(
        				(p.minus(p_a).minus(v_a.MultiplyByScalar(p.minus(p_a).DotProduct(v_a))) )
        				)
        				);
        double C = p.minus(p_a).minus(v_a.MultiplyByScalar(p.minus(p_a).DotProduct(v_a))).absoluteSquared() - r*r;
        double discr = Math.pow(B, 2)-4*A*C;
        Vector intersection;
        if(discr<0) return null;
        if(discr == 0) {
        	 intersection = ray.P0.plus(ray.V.MultiplyByScalar(-B/(2*A)));
        	if(intersection.minus(ray.P0).DotProduct(v_a)<0) return null;
        	
        }
        double root1 = (-B+Math.sqrt(discr))/2*A;
        double root2 = (-B+Math.sqrt(discr))/2*A;
        if(root1<0&&root2<0) return null;
        if (root1<0){
        	intersection = ray.P0.plus(ray.V.MultiplyByScalar(root2));
        	
        	
        }
        else if (root2<0){
        	intersection = ray.P0.plus(ray.V.MultiplyByScalar(root1));
        	
        	
        }
        else{
        	double root = root1<root2?root1:root2;
        	intersection = ray.P0.plus(ray.V.MultiplyByScalar(root));
        }
        
        
        
        double distance = Math.sqrt(ray.P0.minus(intersection).absoluteSquared());
    	
		Surface surface = this;
		Vector intersectionPoint = intersection;
		Vector intersectionNormal = intersection.minus(p_a.plus(v_a.MultiplyByScalar(intersection.minus(p_a).DotProduct(v_a))));
		Intersection res = new Intersection(distance, surface , ray, intersectionPoint , intersectionNormal);
		return res;
        
    }

    private Plane TopPlane() {
        Plane top = new Plane();
        top.Normal = Rotation;
        Vector point = Rotation.MultiplyByScalar(Length/2).plus(CenterPosition);
        top.Offset = Math.sqrt(point.ProjectOn(top.Normal).absoluteSquared());
        top.Material = Material;
        return top;
    }

    private Plane BottomPlane() {
        Plane bottom = new Plane();
        bottom.Normal = Rotation;
        Vector point = Vector.zero().minus(Rotation.MultiplyByScalar(Length/2)).plus(CenterPosition);
        bottom.Offset = Math.sqrt(point.ProjectOn(bottom.Normal).absoluteSquared());
        bottom.Material = Material;
        return bottom;
    }

    private boolean PointIsInCylinder(Vector point, Ray ray) {
        Intersection bottom = BottomPlane().GetIntersection(ray);
        Intersection top = TopPlane().GetIntersection(ray);
        Vector bottom_point = bottom.IntersectionPoint;
        Vector top_point = top.IntersectionPoint;
        Vector mid_up = top_point.minus(point);
        Vector mid_down = bottom_point.minus(point);
        if(mid_up.DotProduct(mid_down)<0) return true;
        return false;
    }
    
}
