public class Direction {

    public static final Direction UP = new Direction(0,-1);  // step 2
    public static final Direction RIGHT = new Direction(1,0); // step 2
    public static final Direction DOWN = new Direction(0,1); // step 2
    public static final Direction LEFT = new Direction(-1,0); // step 2

    public static final Direction UP_2 = new Direction(0,-2);  // step 2
    public static final Direction RIGHT_2 = new Direction(2,0); // step 2
    public static final Direction DOWN_2 = new Direction(0,2); // step 2
    public static final Direction LEFT_2 = new Direction(-2,0); // step 2
    /**
     * Array que conté totes les direccions possibles
     */
    public static final Direction[] ALL = new Direction[]{UP, RIGHT, DOWN, LEFT};
    public static final Direction[] ALL_2 = new Direction[]{UP_2, RIGHT_2, DOWN_2, LEFT_2};

    private final int dx;
    private final int dy;

    /**
     * Constructor privat de les direccions
     * @param dx distància x
     * @param dy distància x
     */
    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Crea una nova Position amb les la distància aplicada
     * @param from Posició inicial
     * @return Posició modificada amb la direcció.
     */
    public Position apply(Position from) {
        return new Position(this.dx+from.getX(),this.dy+ from.getY());
    }
}
