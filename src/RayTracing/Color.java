package RayTracing;

public class Color {

    public double r;
    public double g;
    public double b;

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public byte getRed() {
        return doubleToByte(r);
    }

    public byte getBlue() {
        return doubleToByte(b);
    }

    public byte getGreen() {
        return doubleToByte(g);
    }

    public Color plus(Color c) {
        return new Color(this.r + c.r, this.g + c.g, this.b + c.b).legalize();
    }

    public Color multiplyByScalar(double d) {
        return new Color(this.r * d, this.g * d, this.b * d).legalize();
    }

    public Color legalize() {
        return new Color(legalColorDouble(this.r), legalColorDouble(this.g), legalColorDouble(this.b));
    }

    public byte doubleToByte(double d) {
        if (d > 1)
            return Byte.MAX_VALUE;
        if (d < 0)
            return Byte.MIN_VALUE;
        return (byte) (d * 255);
    }

    private double legalColorDouble(double d) {
        return d < 0 ? 0 : (d > 1 ? 1 : d);
    }
    public Color multiply(Color other){
    	return new Color(r*other.r, g*other.g, b*other.b).legalize();
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
