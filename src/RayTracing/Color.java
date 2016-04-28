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

    public byte doubleToByte(double d) {
        if (d > 1)
            return Byte.MAX_VALUE;
        if (d < 0)
            return Byte.MIN_VALUE;
        return (byte) (d * 255 - 128);
    }
}
