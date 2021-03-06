package RayTracing;

public class Vector {
    private static final Vector _ZERO = new Vector(0, 0, 0);
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector CrossProduct(Vector a, Vector b) {
        double x = a.y * b.z - a.z * b.y;
        double y = a.z * b.x - a.x * b.z;
        double z = a.x * b.y - a.y * b.x;
        return new Vector(x, y, z);
    }

    public static double DotProduct(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector plus(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vector minus(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vector zero() {
        return _ZERO;
    }

    public static boolean PointOnLine(Vector point1, Vector point2, Vector pointBetween) {
        double lineLength = Math.sqrt(point1.minus(point2).absoluteSquared());
        double sumOfDistances = Math.sqrt(point1.minus(pointBetween).absoluteSquared()) +
                Math.sqrt(point2.minus(pointBetween).absoluteSquared());
        return Math.abs(lineLength - sumOfDistances) < 0.00001;
    }

    public static double DistanceFromPoint(Vector v, Vector point) {
        return Math.sqrt(point.minus(v).CrossProduct(point).absoluteSquared() / v.absoluteSquared());
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Vector minus(Vector v2) {
        return Vector.minus(this, v2);
    }

    public Vector plus(Vector v2) {
        return Vector.plus(this, v2);
    }

    public double DotProduct(Vector v2) {
        return Vector.DotProduct(this, v2);
    }

    public Vector CrossProduct(Vector v2) {
        return Vector.CrossProduct(this, v2);
    }

    public Vector MultiplyByScalar(double scal) {
        return new Vector(x * scal, y * scal, z * scal);
    }

    public Vector ProjectOn(Vector other) {
        double s = Vector.DotProduct(this, other) / Vector.DotProduct(other, other);
        return other.MultiplyByScalar(s);
    }

    public double absoluteSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double length() {
        return Math.sqrt(this.absoluteSquared());
    }

    public Vector normalized() {
        double length = Math.sqrt(this.absoluteSquared());
        return new Vector(this.x / length, this.y / length, this.z / length);
    }

    public Vector findPerpendicularXY() {
        return new Vector(this.getY(), -1 * this.getX(), 0).normalized();
    }

    public Vector findPerpendicularXZ() {
        return new Vector(-1 * this.getZ(), 0, this.getX()).normalized();
    }

    public boolean equals(Vector v) {
        if (v == null)
            return false;
        return Math.abs(this.x - v.x) < 0.000001 && Math.abs(this.y - v.y) < 0.000001
                && Math.abs(this.z - v.z) < 0.000001;
    }

    public Vector rotateAroundX(double alpha) {
        return new Vector(this.x,
                cos(alpha) * this.y - sin(alpha) * this.z,
                sin(alpha) * this.y + cos(alpha) * this.z);
    }

    public Vector rotateAroundY(double alpha) {
        return new Vector(this.x * cos(alpha) + this.z * sin(alpha),
                this.y,
                -this.x * sin(alpha) + this.z * cos(alpha));
    }

    public Vector rotateAroundZ(double alpha) {
        return new Vector(this.x * cos(alpha) - this.y * sin(alpha),
                this.x * sin(alpha) + this.y * cos(alpha),
                this.z);
    }

    public Vector mirror(Vector mirrorVector) {
       // return mirrorVector.MultiplyByScalar(2 * mirrorVector.DotProduct(this)).minus(this);
    	return this.minus(mirrorVector.MultiplyByScalar(2*this.DotProduct(mirrorVector)));
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    private double cos(double degree) {
        return Math.cos(Math.PI * degree / 180);
    }

    private double sin(double degree) {
        return Math.sin(Math.PI * degree / 180);
    }
}
