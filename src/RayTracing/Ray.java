package RayTracing;

public class Ray {
    public Vector P0;
    public Vector V;

    public Ray(Vector p0, Vector v) {
        P0 = p0;
        V = v.normalized();
    }

    @Override
    public String toString() {
        return "Ray{" +
                "P0=" + P0 +
                ", V=" + V +
                '}';
    }
}
