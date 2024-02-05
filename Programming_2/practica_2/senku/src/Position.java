public class Position {
    /**
     * Variable d'instància x
     */
    private final int x;
    /**
     * Variable d'instància y
     */
    private final int y;

    /**
     * Crea una instància representant una posició
     * amb les coordenades x,y.
     * @param x Guarda la component x de la posició
     * @param y Guarda la component y de la posició
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Simplement retorna la component x de la posició referenciada
     * @return Retorna x
     */
    public int getX() {
        return this.x;
    }
    /**
     * Simplement retorna la component y de la posició referenciada
     * @return Retorna y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Retorna un valor booleá si la posició refernciada
     * i la enviada per paràmetre comparteixen algún eix, es a dir que el seu valor x o y es el mateix
     * @param other Posició d'entrada
     * @return  retorna true o false
     */

    public boolean colinear(Position other) {
        return other.x == this.x || other.y == this.y;
    }

    /**
     * Fa la diferència entre les dues components de les posicions en valor absolut i les suma donant així la distància entre dos punts.
     * @param other Posició d'entrada
     * @return Retorna un int que representa la distància entre les dues posicions
     */
    public int distance(Position other) {
        return Math.abs(this.x-other.x)+Math.abs(this.y-other.y);
    }

    /**
     * Retorna una nova instància de position que representa el punt mig entre dos posicions
     * @param other Posició amb la que es vol comparar
     * @return retorna una nova instància de Position
     */
    public Position middle(Position other) {
        return new Position((getX()+other.getX())/2,(getY()+other.getY())/2);
    }

    // Needed for testing

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
