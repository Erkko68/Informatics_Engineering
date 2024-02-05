import javax.swing.text.rtf.RTFEditorKit;
import java.util.FormattableFlags;

public class Cell {

    private static final char C_FORBIDDEN = '#';
    private static final char C_FILLED = 'o';
    private static final char C_EMPTY = '·';

    public static final Cell FORBIDDEN = new Cell(C_FORBIDDEN); // step 3
    public static final Cell FILLED = new Cell(C_FILLED); // step 3
    public static final Cell EMPTY = new Cell(C_EMPTY); // step 3

    private final char status;

    /**
     * Constructor del estat de cada casella
     * @param status Caràcter que representa l'estat de la casella
     */
    private Cell(char status) {
        this.status = status;
    }

    /**
     * Comprova si el caràcter d'entrada correspon amb algun dels estats
     * @param status Caràcter d'entrada
     * @return Retorna si es alguna dels tres tipus d'estats o null en cas contrari
     */
    public static Cell fromChar(char status) {
        if(status == C_FORBIDDEN){
            return FORBIDDEN;
        }else if(status == C_FILLED){
            return FILLED;
        }else if(status == C_EMPTY){
            return EMPTY;
        }
        return null;
    }

    /**
     * Comprova si esta prohibida
     * @return true si esta prohibida false si no
     */
    public boolean isForbidden() {
        return this.status == C_FORBIDDEN;
    }
    /**
     * Comprova si esta ocupada
     * @return true si esta ocupada false si no
     */
    public boolean isFilled() {
        return this.status == C_FILLED;
    }
    /**
     * Comprova si esta buida
     * @return true si esta buida false si no
     */
    public boolean isEmpty() {
        return this.status == C_EMPTY;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
