
package librarymanager;

import acm.program.CommandLineProgram;
import domain.Book;
import domain.Member;
import files.BooksFile;
import files.LogFile;
import files.MembersFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class LibraryManager extends CommandLineProgram {

    private static final String BOOKS = "llibres.dat";
    private static final String MEMBERS = "socis.dat";
    private static final String LOG = "manager.log";
    private static final String MOVEMENTS = "movements.csv";
    private static final String ADDBOOK = "ALTALIBRO";
    private static final String ADDMEMBER = "ALTASOCIO";
    private static final String LEND = "PRESTAMO";
    private static final String RETURN = "DEVOLUCION";

    private BufferedReader movements;

    private BooksFile booksFile;
    private MembersFile membersFile;
    private LogFile logFile;

    private int movementNumber = 0;

    // Entry points

    public static void main(String[] args) {
        new LibraryManager().start(args);
    }

    public void run() {
        try {
            openFiles();
            processMovements();
            closeFiles();
        } catch (IOException ex) {
            println("ERROR something about the files");
        }
    }

    // Opening and closing

    private void openFiles() throws IOException {
        movements = new BufferedReader(new FileReader(MOVEMENTS));
        logFile = new LogFile(LOG);
        booksFile = new BooksFile(BOOKS);
        membersFile = new MembersFile(MEMBERS);
    }

    private void closeFiles() throws IOException {
        movements.close();
        logFile.close();
        booksFile.close();
        membersFile.close();
    }

    // Movements processing

    /**
     * Starting function to process all the movements in the movements.csv file, it uses a loop to read each line and the sends it to the next function.
     * @throws IOException if an I/O error occurs.
     */
    private void processMovements() throws IOException {
        String movement = movements.readLine();
        while(movement != null){
            movementsSelector(movement,this.movementNumber);
            movement = movements.readLine();
            this.movementNumber += 1;
        }
        // Reset size of the files to remove previous data, not specified in requeriments, but useful when testing.
        booksFile.clear();
        membersFile.clear();
    }

    /**
     * Reads each string and dividies the movement separated by a comma. Then depending on the mode selected will execute each mode auxiliar function. Or return an error message of invalid mode.
     * @param movement String to process.
     * @param lineNum Number used to set each log line.
     * @throws IOException if an I/O error occurs.
     */
    private void movementsSelector(String movement, int lineNum) throws IOException{
        StringTokenizer line = new StringTokenizer(movement,",");
        String token = line.nextToken();

        switch (token) {
            case ADDBOOK -> {
                String tittle = line.nextToken();
                addBook(tittle, lineNum);
            }
            case ADDMEMBER -> {
                String name = line.nextToken();
                name = normalizeString(name,Member.NAME_LIMIT);
                String direction = line.nextToken();
                direction = normalizeString(direction,Member.ADDRESS_LIMIT);
                addMember(name, direction, lineNum);
            }
            case LEND -> {
                long bookId = Long.parseLong(line.nextToken());
                long memberId = Long.parseLong(line.nextToken());
                lendBook(bookId,memberId,lineNum);
            }
            case RETURN -> {
                long bookId = Long.parseLong(line.nextToken());
                returnBook(bookId,lineNum);
            }
            default -> logFile.error("Invalid movement at line: " + lineNum, lineNum);
        }
    }

    /**
     * Used to prevent the string to exceed the desired length, is only used to do the comparison
     * in the function repeatedMember, because the string converted to bytes is always cut.
     * @param string String to resize.
     * @param limit Length of the final string.
     * @return A new string with the correct size.
     */
    private String normalizeString(String string,int limit) {
        if(string.length()>limit){
            string = string.substring(0,limit);
        }
        return string;
    }

    /**
     * First checks if the book has been already added and then adds the book with the corresponding id using the length() function.
     * @param tittle tittle of the book
     * @param line line to write log
     * @throws IOException if an I/O error occurs.
     */
    private void addBook(String tittle, int line) throws IOException{
        long id = booksFile.length();
        Book book = new Book(id, tittle);
        booksFile.write(book);
        logFile.ok("Book with tittle: \"" + book.getTitle() + "\" added with id: " + book.getId() +".", line);
    }

    /**
     * Adds a new member with the corresponding id, name and address, first checking if it has been already added.
     * @param name Name of the member.
     * @param address Address of the member.
     * @param line line to write log.
     * @throws IOException if an I/O error occurs.
     */
    private void addMember(String name, String address, int line) throws IOException{
        if(!repeatedMember(name,address)) {
            long id = membersFile.length();
            Member member = new Member(id, name, address);
            membersFile.write(member);

            logFile.ok("Member with the name \"" + member.getName() + "\" and address \"" + member.getAddress() + "\" added successfully with Id: " + id, line);
        }else{
            logFile.error("Member already exists.", line);
        }
    }

    /**
     * Checks if the member has been already added.
     * @param name Name
     * @param address Address
     * @return returns ture if repeated, false if not.
     * @throws IOException if an I/O error occurs.
     */
    private boolean repeatedMember(String name, String address) throws IOException{
        long members = membersFile.length();
        for(long id = 0; id < members; id++){
            if(name.equals(membersFile.read(id).getName()) && address.equals(membersFile.read(id).getAddress())){
                return true;
            }
        }
        return false;
    }

    /**
     * It lends the book to a member doing all the necessary checks.
     * @param bookId ID of the book
     * @param memberId ID of the member
     * @param line line to write log
     * @throws IOException if an I/O error occurs.
     */
    private void lendBook(long bookId, long memberId, int line) throws IOException{
        if(bookExists(bookId)){                                 //Check if book exists
            Book book = booksFile.read(bookId);                 //Get book
            if(memberExists(memberId)){                         //Check if member exists
                Member member = membersFile.read(memberId);     // Get member
                if(!isLent(book)){                              //Check if book has been lent
                    if(member.canBorrow()){                     // Check if member can borrow more books and if so lend the book.
                        book.setIdMember(memberId);
                        member.addBook(bookId);
                        booksFile.write(book);
                        membersFile.write(member);
                        logFile.ok("Book with tittle: \"" + book.getTitle() + "\" lent successfully to member with id " + member.getId() , line);
                    }else{
                        logFile.error("Member with id: " + member.getId() + " can't borrow more books.", line);
                    }
                }else{
                    logFile.error("Book already lent to member with id: " + book.getIdMember(), line);
                }
            }else{
                logFile.error("The member doesn't exists.", line);
            }
        }else{
            logFile.error("The book doesn't exists.", line);
        }
    }

    /**
     * Returns the book borrowed by a member.
     * @param bookId ID of the book.
     * @param line line ofr the log.
     * @throws IOException if an I/O error occurs.
     */
    private void returnBook(long bookId, int line) throws IOException{
        if(bookExists(bookId)){                                 //Check if book exists
            Book book = booksFile.read(bookId);                 // Get book
            if(isLent(book)) {                                  // Check if the book has been lent
                long memberId = book.getIdMember();             // Get member id
                if(memberExists(memberId)) {                    //Check if the member exists
                    Member member = membersFile.read(memberId); // Get member
                    if (member.containsBook(bookId)) {          // Check if the member has the book and then remove it.
                        book = new Book(book.getId(), book.getTitle());
                        member.removeBook(bookId);
                        booksFile.write(book);
                        membersFile.write(member);
                        logFile.ok("Book with tittle: \"" + book.getTitle() + "\" and id: " + book.getId() + ", returned successfully from member with id: "+member.getId(), line);
                    } else {
                        logFile.error("The member doesn't have the book.", line);
                    }
                } else {
                    logFile.error("The member  doesn't exists.", line);
                }
            }else{
                logFile.error("The book is not lent.", line);
            }
        }else{
            logFile.error("The book doesn't exists.", line);
        }
    }

    /**
     * Checks if the book has been lent, knowing that a book is not lent when its
     * idMember is -1L we simply check if that's the case.
     * @param book book to check
     * @return True if lent false if not
     */
    private boolean isLent(Book book){
        return book.getIdMember()!=-1;
    }

    /**
     * To check if the book exists we simply check if its id is between the booksFile.length()
     * since all books are stored from 0 to n without any spaces in between.
     * @param bookId Book ID to check
     * @return returns true if book exists false if not.
     * @throws IOException if an I/O error occurs.
     */
    private boolean bookExists(long bookId) throws IOException{
        return bookId < booksFile.length() && bookId>=0;
    }

    /**
     * Checks if the memberId is inside the range from 0 to n in the length of the membersFile.
     * @param memberId member to check
     * @return returns true if the member exists false if not.
     * @throws IOException if an I/O error occurs.
     */
    private boolean memberExists(long memberId) throws IOException{
        return memberId < membersFile.length() && memberId>=0;
    }
}