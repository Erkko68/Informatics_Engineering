// It knows the game rules

public class Game {

    private final Board board;

    /**
     * Constructor de la classe Board
     * @param board Obté el tauler de joc sobre el qual s'executarà el joc.
     */
    public Game(Board board) {
        this.board = board;
    }

    /**
     * Aquesta funció comprova si donada una casella aquesta en alguna de les 4 direccions pot matar,
     * utilitza el Array de direccions ALL dins un bucle per comprovar que from estigui,
     * ocupada la direcció següent també i la següent buida.
     * @param from Coordenades de la casella
     * @return Retorna si existeix algun moviment vàlid en les quatre direccions
     */
    public boolean isValidFrom(Position from) {
        for (int i = 0; i < Direction.ALL.length; i++) {
            Direction kill = Direction.ALL[i];
            Direction to = Direction.ALL_2[i];
            if (this.board.isFilled(from) && this.board.isFilled(kill.apply(from)) && this.board.isEmpty(to.apply(from))) {
                return true;
            }
        }
        return false;
    }

    /**
     * En assumir que validFrom ja és una posició vàlida solament cal comprovar que amb la posició to
     * la distància entre ells sigui 2 i siguin coolinears.
     * @param validFrom Posició de partida
     * @param to Posició Final
     * @return Retorna si són vàlides en format booleà.
     */
    public boolean isValidTo(Position validFrom, Position to) {
        return validFrom.distance(to) == 2 && this.board.isEmpty(to) && validFrom.colinear(to);
    }

    /**
     * Aquesta funció utilitza funcions auxiliars de la classe Board,
     * per a emplenar la posició final validTo i buidar la posició inicial validFrom en realitzar un moviment vàlid.
     * @param validFrom Posició de partida
     * @param validTo Posició final
     * @return Retorna la posició intermitja entre validFrom i validTo.
     */
    public Position move(Position validFrom, Position validTo) {
        board.emptyPosition(validFrom);
        board.emptyPosition(validTo.middle(validFrom));
        board.fillPosition(validTo);
        return validFrom.middle(validTo);
    }

    /**
     * Comproba si existeix alguna casella que tingui moviments vàlids,
     * simplement recorre totes les posicions del tauler i utilitza la funció
     * isValidFrom per comprovar-ho.
     * @return Retorna verdader o falç en funció de si existeixen o no positions vàlides.
     */
    public boolean hasValidMovesFrom() {
        for(int i=0;i<this.board.getHeight();i++){
            for(int j=0;j<this.board.getWidth();j++){
                if(isValidFrom(new Position(i,j))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Molt similar a la funció anterior amb l'única diferència que cada vegada que troba una posició vàlida la conta.
     * @return Retorna el nombre de moviments vàlids que hi ha en el tauler.
     */
    public int countValidMovesFrom() {
        int validFrom = 0;
        for(int i=0;i<board.getHeight();i++){
            for(int j=0;j< board.getWidth();j++){
                if(isValidFrom(new Position(i,j))){
                    validFrom++;
                }
            }
        }
        return validFrom;
    }

    /**
     * Donada la posició d'entrada comprova si és vàlida amb algun dels moviments possibles,
     * per això utilitza el array ALL_2 de directions per crear una nova posició i realitza el bucle
     * per veure si en troba alguna de vàlida i la suma.
     * @param validFrom Posició inicial que comprovar.
     * @return Retorna el nombre de moviments vàlids.
     */
    public int countValidMovesTo(Position validFrom) {
        int validTo = 0;
        for(int i=0;i<Direction.ALL_2.length;i++){
            if(isValidTo(validFrom,Direction.ALL_2[i].apply(validFrom))){
                validTo++;
            }
        }
        return validTo;
    }

}

