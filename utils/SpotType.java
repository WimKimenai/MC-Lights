package wkimenai.mc-lights.utils;

public enum SpotType
{
    OFF("NDhjZjIyZDZlYjExYzQ2M2UyZDI1ZDhhODFlOTY3NWY5MTg2ODgzMmIwNDM4ODc4YzczOWZkZGRjNDJhIn19fQ==", 0, 0, 0), 
    GREEN("MzRkNDZjM2ZmODhlMGRiYzUxZjY2MjE5M2UxOWViNGEwMWNkMWY0MGFkZDNhMWJiMTU2NjcwOTlhOGJiYjIifX19", 0, 255, 0), 
    RED("ZmZkYzRjODg5NWUzZjZiM2FjNmE5YjFjZDU1ZGUzYTI5YmJjOGM3ODVlN2ZiZGJkOTMyMmQ4YzIyMzEifX19", 255, 0, 0), 
    YELLOW("Yzk1MGRlMzg0M2UwNmIzM2ZmYzMzNGMxNTRiZjliMzk4ODgxNWM3ZTU0NjM2MjM3N2ZlN2EwNzQzNmFiIn19fQ==", 255, 255, 0), 
    BLUE("YzJjYWE1NGJkMGJkY2ZkNzdjYjA2YzI1OGM3YzViODNiNTI2NzE4ZDA0ZDg0ZTRkMTg3YjZkOTcwMjYyYyJ9fX0=", 255, 255, 255);
    
    private String texture;
    private int r;
    private int g;
    private int b;
    
    private SpotType(final String str, final int r, final int g, final int b) {
        this.texture = CC.headkey + str;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public String getTexture() {
        return this.texture;
    }
    
    public int getB() {
        return this.b;
    }
    
    public int getG() {
        return this.g;
    }
    
    public int getR() {
        return this.r;
    }
    
    public static SpotType getRandom() {
        return values()[(int)(Math.random() * values().length)];
    }
}
