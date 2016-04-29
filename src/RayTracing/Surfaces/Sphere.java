package RayTracing.Surfaces;

import RayTracing.Intersection;
import RayTracing.Ray;
import RayTracing.Vector;

public class Sphere extends Surface {
	public Vector CenterPosition;
	public double Radius;

	@Override
	public Intersection GetIntersection(Ray ray) {
		double b = ray.V.MultiplyByScalar(2).DotProduct(
				ray.P0.minus(CenterPosition));
		double c = ray.P0.minus(CenterPosition).absoluteSquared() - Radius
				* Radius;

		double a = 1;
		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) {
			return null;
		}
		double d1 = (-b - Math.sqrt(discriminant)) / (2 * a);
		double d2 = (-b + Math.sqrt(discriminant)) / (2 * a);
		double d;
		if (d1 > 0 && d2 > 0)
			d = Math.min(d1, d2);
		else if (d1 > 0 && d2 < 0)
			d = d1;
		else if (d2 > 0 && d1 < 0)
			d = d2;
		else
			d = 0;
		Vector intersectionPoint = ray.P0.plus(ray.V.normalized()
				.MultiplyByScalar(d));
		Vector intersectionNormal = intersectionPoint.minus(CenterPosition)
				.normalized();
	//	System.out.println("sphere of radius " + Radius + " and center "
			//	+ CenterPosition.toString() + " intersected - discriminant is "
			//	+ discriminant + ",\n, intersection vector is "+intersectionPoint);
		return new Intersection(d, this, ray, intersectionPoint,
				intersectionNormal);
	}
}
