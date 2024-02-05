public class Board {

    private final int width;
    private final int height;
    private final Cell[][] cells;

    /**
     * Constructor de la classe
     * @param width Amplada del tauler
     * @param height Altura del tauler
     * @param board Tauler enviat en forma d'string
     */
    public Board(int width, int height, String board) {
        this.width = width;
        this.height = height;
        this.cells = boardFiller(board);
    }

    /**
     * El constructor utilitza una funció auxiliar per emmagatzemar el tauler en format d'string
     * en un format de matriu, simplement cada vegada que troba el caràcter \n canvia de fila, i cada caràcter individual correspon a una posició de la matriu.
     * @param board String de la taula inicial
     * @return Retorna una nova matriu emplenada amb les Cells del String.
     */
    private Cell[][] boardFiller(String board){
        Cell[][] tempBoard = new Cell[this.height][this.width];
        int k = 0;
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                Cell add = Cell.fromChar(board.charAt(k));
                if(add != null){
                    tempBoard[i][j]= add;
                }
                k++;
            }
            k++;
        }
        return tempBoard;
    }

    /**
     * @return Retorna l'amplada
     */
    public int getWidth() {
        return this.width;
    }
    /**
     * @return Retorna l'altura
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Primer comprova si la posició està dins del tauler i seguidament si ho està
     * crea una nova casella i mira si està prohibida.
     * @param pos Posició de la casella
     * @return Retorna true o false si esta prohibida o no.
     */
    public boolean isForbidden(Position pos) {
        if(outOfBounds(pos)){
            Cell newCell = cells[pos.getY()][pos.getX()];
            return newCell.isForbidden();
        }
        return true;
    }
    /**
     * Primer comprova si la posició està dins del tauler i seguidament si ho està
     * crea una nova casella i mira si està emplenada.
     * @param pos Posició de la casella
     * @return Retorna true o false si esta emplenada o no.
     */
    public boolean isFilled(Position pos) {
        if(outOfBounds(pos)){
            Cell newCell = cells[pos.getY()][pos.getX()];
            return newCell.isFilled();
        }
        return false;
    }
    /**
     * Primer comprova si la posició està dins del tauler i seguidament si ho està
     * crea una nova casella i mira si està buida.
     * @param pos Posició de la casella
     * @return Retorna true o false si esta buida o no.
     */
    public boolean isEmpty(Position pos) {
        if(outOfBounds(pos)){
            Cell newCell = cells[pos.getY()][pos.getX()];
            return newCell.isEmpty();
        }
        return false;
    }
    /**
     * Primer comprova si la posició està dins del tauler i seguidament si ho està
     * crea una nova casella i l'emplena amb les funcions de la classe Cell.
     * @param pos Posició de la casella
     */
    public void fillPosition(Position pos) {
        if(outOfBounds(pos)){
            cells[pos.getY()][pos.getX()] = Cell.FILLED;
        }
    }
    /**
     * Primer comprova si la posició està dins del tauler i seguidament si ho està
     * crea una nova casella i la buida amb les funcions de la classe Cell.
     * @param pos Posició de la casella
     */
    public void emptyPosition(Position pos) {
        if(outOfBounds(pos)){
            cells[pos.getY()][pos.getX()]= Cell.EMPTY;
        }
    }

    /**
     * FUnció auxiliar que comprova donada una posició, si aquesta està dins dels marges del tauler.
     * @param pos Posició a analitzar
     * @return Retorna true o falç si està dins dels marges o no.
     */
    private boolean outOfBounds(Position pos){
        return (pos.getX()<this.width && pos.getX()>=0 && pos.getY()<this.height && pos.getY()>=0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(cells[y][x].toString());
            }
            if (y != height - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
