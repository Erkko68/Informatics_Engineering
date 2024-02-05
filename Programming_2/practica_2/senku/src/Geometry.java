import acm.graphics.GDimension;
import acm.graphics.GPoint;

public class Geometry {

    private final int windowWidth;
    private final int windowHeight;
    private final int numCols;
    private final int numRows;
    private final double boardPadding;
    private final double cellPadding;

    /**
     * Inicialitza les variables d'instància per a la classe Geomtry, la cual representa els tamanys que ha de tenir el tauler.
     * @param windowWidth Amplada del tauler
     * @param windowHeight Altura del tauler
     * @param numCols Número de columnes
     * @param numRows Número de files
     * @param boardPadding Marge del tauler respecte el tamany total
     * @param cellPadding Marge entre caselles
     */
    public Geometry(int windowWidth, int windowHeight, int numCols, int numRows, double boardPadding, double cellPadding) {
        this.windowWidth=windowWidth;
        this.windowHeight=windowHeight;
        this.numCols=numCols;
        this.numRows=numRows;
        this.boardPadding=boardPadding;
        this.cellPadding=cellPadding;
    }

    /**
     * Retorna el número de files
     * @return Retorna un int
     */
    public int getRows() {
        return this.numRows;
    }

    /**
     * Retorna el número de columnes
     * @return Retorna un int
     */
    public int getColumns() {
        return this.numCols;
    }

    /**
     * Aquesta funció determina la mida del tauler aplicant els marges de separació amb la mida total del programa.
     * @return Retorna una nova instància del tipus GDimension.
     */
    public GDimension boardDimension() {
        return new GDimension(
                this.windowWidth-(this.windowWidth*this.boardPadding*2),
                this.windowHeight-(this.windowHeight*this.boardPadding*2));
    }

    /**
     * Retorna el punt del vèrtex superior esquerra del tauler.
     * Simplement, És una multiplicació de l'amplada i llargada total del taules amb el parametre boardPadding
     * @return Retorna en punt amb la forma GPoint.
     */
    public GPoint boardTopLeft() {
        return new GPoint(
                this.windowWidth*this.boardPadding,
                this.windowHeight*this.boardPadding);
    }

    /**
     * Calcula les dimensions de cada celda (el cercle interior no)
     * @return Retorna les mides de la celda amb GDimension
     */
    public GDimension cellDimension() {
        return new GDimension(
                boardDimension().getWidth()/this.getColumns(),
                boardDimension().getHeight()/this.getRows()
        );
    }

    /**
     * Similar a la funció boardTopLeft() aquesta retorna el punt superior esquerra de la celda que s'envia pels paràmetres x i y,
     * @param x Número de columna on es troba la celda
     * @param y Número de fila on e stroba la celda
     * @return retorna un nou punt GPoint
     */
    public GPoint cellTopLeft(int x, int y) {
        return new GPoint(
                (cellDimension().getWidth()*x)+boardTopLeft().getX(),
                (cellDimension().getHeight()*y)+boardTopLeft().getY());
    }

    /**
     * Calcula les dimensions adecuades de cada fixa en forma de cercle dins de cada celda.
     * També te en compte els marges entre celdes amb la variable cellPadding
     * @return Retorna les dimensions en format GDimension
     */
    public GDimension tokenDimension() {
        return new GDimension(
                cellDimension().getWidth()-(cellDimension().getWidth()*this.cellPadding*2),
                cellDimension().getHeight()-(cellDimension().getHeight()*this.cellPadding*2)
        );
    }

    /**
     * Al igual que cellTopLeft retorna la posició superior esquerra de la fixa
     * @param x Columna del token
     * @param y Fila del token
     * @return Retorna el punt superior esquerre del token en format GPoint
     */
    public GPoint tokenTopLeft(int x, int y) {
        return new GPoint(
                cellTopLeft(x,y).getX()+(cellDimension().getWidth()-tokenDimension().getWidth())/2,
                cellTopLeft(x,y).getY()+(cellDimension().getHeight()-tokenDimension().getHeight())/2
        );
    }

    /**
     * Aquesta funció està formada per dos bucles que troben la posició x i la y.
     * ES realitza una comparació de les mides de les caselles i detecta si la coordenada d'entrada es troba dins dels marges.
     * Si aquest es el cas, agafarà el índex del bucle com la posició de la casella que es vol seleccionar.
     * En el cas que les coordenades no tinguin una posició retornarà la posició -1,-1.
     * @param x Representa la coordenada x de la pantalla en format double
     * @param y Representa la coordenada y de la pantalla en format double
     * @return retorna la casella a la que correspon les coordenades x i y.
     */
    public Position xyToCell(double x, double y) {

        int col = -1;
        int row = -1;

        double x_marging = boardTopLeft().getX();
        double y_marging = boardTopLeft().getY();

        for(int i=0;i<getColumns();i++){
            for(int j=0;j<getRows();j++) {
                if (x_marging + i * cellDimension().getWidth() <= x && x <=
                        x_marging + (i + 1) * cellDimension().getWidth()) {
                    col = i;
                }
                if (y_marging + j * cellDimension().getHeight() <= y && y <=
                        y_marging + (j + 1) * cellDimension().getHeight()) {
                    row = j;
                }
            }
        }


        return new Position(col,row);
    }

    /**
     * Aquesta funció ens permet obtenir la coordenada central de cada casella, obté la mida de cada eix i la divideix entre dos.
     * @param x Posició x del punt superior esquerre
     * @param y Posició x del punt superior esquerre
     * @return Retorna un nou punt amb la coordenada central
     */
    public GPoint centerAt(int x, int y) {
        return new GPoint(
                (tokenTopLeft(x,y).getX()+(tokenDimension().getWidth())/2),
                (tokenTopLeft(x,y).getY()+(tokenDimension().getHeight())/2)
        );
    }
}
