package RayTracing.Surfaces;


import RayTracing.Intersection;
import RayTracing.Material;
import RayTracing.Ray;
import RayTracing.Vector;

public class Cylinder extends Surface {
    public Vector CenterPosition;
    public double Length;
    public double Radius;
    public Vector Rotation;

    public Cylinder(Vector centerPosition, double length, double radius, Vector rotationDegree, Material material) {
        CenterPosition = centerPosition;
        Length = length;
        Radius = radius;
        Rotation = new Vector(0, 0, 1).rotateAroundX(rotationDegree.getX())
                .rotateAroundY(rotationDegree.getY())
                .rotateAroundZ(rotationDegree.getZ())
                .normalized();
        Material = material;
    }

    @Override
    public Intersection GetIntersection(Ray ray) {  
        Intersection topIntersection = TopPlane().GetIntersection(ray);
        if (topIntersection != null && !PointIsInInfiniteCylinder(topIntersection.IntersectionPoint))
            topIntersection = null;
        
        //intersect with each plane
        Intersection bottomIntersection = BottomPlane().GetIntersection(ray);
        if (bottomIntersection != null && !PointIsInInfiniteCylinder(bottomIntersection.IntersectionPoint))
            bottomIntersection = null;

        Intersection infiniteCylinderIntersection = GetInfiniteCylinderIntersection(ray);
        if (infiniteCylinderIntersection != null && !PointIsInCylinderPlanes(infiniteCylinderIntersection.IntersectionPoint))
            infiniteCylinderIntersection = null;
     //   if (infiniteCylinderIntersection!=null) System.out.println("wall");
       // return infiniteCylinderIntersection;
      //  if(infiniteCylinderIntersection==Intersection.minimal(infiniteCylinderIntersection, bottomIntersection, topIntersection)) System.out.println("found wall");
        return Intersection.minimal(infiniteCylinderIntersection, bottomIntersection, topIntersection);
    }

    private Intersection GetInfiniteCylinderIntersection(Ray ray) {
        Vector p = ray.P0;
        Vector v = ray.V;
        Vector p_a = CenterPosition;
        Vector v_a = Rotation;
        double r = Radius;

        double A = (v.minus(v_a.MultiplyByScalar(v.DotProduct(v_a)))).absoluteSquared();
        double B = 2 * (v.minus(v_a.MultiplyByScalar(v.DotProduct(v_a)))
                .DotProduct(
                        ((p.minus(p_a)).minus(v_a.MultiplyByScalar((p.minus(p_a)).DotProduct(v_a))))
                )
        );
        double C = ((p.minus(p_a)).minus(v_a.MultiplyByScalar((p.minus(p_a)).DotProduct(v_a)))).absoluteSquared() - r * r;
        double discr = B*B - 4 * A * C;
        
        Vector intersection;
        if (discr < 0) return null;
        if (discr == 0) {
            return null;
        }
        
        double root1 = (-B + Math.sqrt(discr)) / (2 * A);
        double root2 = (-B - Math.sqrt(discr)) / (2 * A);
        if(root1<root2)System.out.println("ERR");
    
        if (root1 < 0 && root2 < 0) return null;
        if (root1 < 0) {
            intersection = ray.P0.plus(ray.V.MultiplyByScalar(root2));
        } else if (root2 < 0) {
            intersection = ray.P0.plus(ray.V.MultiplyByScalar(root1));
        } else {
            double root = root1 < root2 ? root1 : root2;
            intersection = ray.P0.plus(ray.V.MultiplyByScalar(root));
        }

        double distance = Math.sqrt(ray.P0.minus(intersection).absoluteSquared());

        Surface surface = this;
        Vector intersectionPoint = intersection;
     //   Vector center = Rotation.MultiplyByScalar(intersection.minus(CenterPosition).DotProduct(Rotation)).plus(CenterPosition);
        double up = intersection.minus(CenterPosition).DotProduct(Rotation);
        Vector upv = Rotation.MultiplyByScalar(up);
        Vector center = CenterPosition.plus(upv);
        Vector intersectionNormal = intersection.minus(center).normalized();
        Intersection res = new Intersection(distance, surface, ray, intersectionPoint, intersectionNormal);
        return res;

    }

    private Plane TopPlane() {
        Plane top = new Plane();
        top.Normal = Rotation;
        Vector point = Rotation.MultiplyByScalar(Length / 2).plus(CenterPosition);
        double offsetDirection = (Vector.PointOnLine(Vector.zero(), Rotation, point))
                ? 1 : -1;
        top.Offset = offsetDirection * Math.sqrt(point.ProjectOn(top.Normal).absoluteSquared());
        top.Material = Material;
        return top;
    }

    private Plane BottomPlane() {
        Plane bottom = new Plane();
        bottom.Normal = Rotation;
        Vector point = Vector.zero().minus(Rotation.MultiplyByScalar(Length / 2)).plus(CenterPosition);
        double offsetDirection = (Vector.PointOnLine(Vector.zero(), Rotation, point))
                ? 1 : -1;
        bottom.Offset = offsetDirection * Math.sqrt(point.ProjectOn(bottom.Normal).absoluteSquared());
        bottom.Material = Material;
        return bottom;
    }

    private boolean PointIsInCylinderPlanes(Vector point) {
    	
    	
        Ray rayUp = new Ray(point, Rotation);
        Ray rayDown = new Ray(point, Rotation.MultiplyByScalar(-1));
        Intersection bottom = BottomPlane().GetIntersection(rayDown);
        Intersection top = TopPlane().GetIntersection(rayUp);
    //    System.out.println("bottom is "+(bottom==null?"NULL":"NOT NULL"));
       // System.out.println("top is "+(top==null?"NULL":"NOT NULL"));
        if (bottom == null || top == null) {
        //	 System.out.println("point NOT in cylinder planes");
            return false;
        }
    //    System.out.println("point in cylinder planes");
        return true;
       // return Vector.PointOnLine(top.IntersectionPoint, bottom.IntersectionPoint, point);
    }

    private boolean PointIsInInfiniteCylinder(Vector point) {
    	Vector upv = Rotation.MultiplyByScalar(point.minus(CenterPosition).DotProduct(Rotation));
    	Vector circle_center = CenterPosition.plus(upv);
    	Vector radius = point.minus(circle_center);
    	if(radius.absoluteSquared()>Radius*Radius)
    		return false;
    	return true;
    	
       // return Vector.DistanceFromPoint(Rotation, point.minus(CenterPosition)) < Radius;
    }

}
