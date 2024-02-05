
package files;

import domain.Book;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author jmgimeno
 */
public class BooksFile {
    
    private final RandomAccessFile raf;

    /**
     * Constructor of the class
     * @param fname File name as string
     * @throws IOException if an I/O error occurs.
     */
    public BooksFile(String fname) throws IOException {
        this.raf = new RandomAccessFile(fname,"rw");
    }

    /**
     * Set the length of the file to 0
     * @throws IOException if an I/O error occurs.
     */
    public void clear() throws IOException {
        this.raf.setLength(0);
    }

    /**
     * Reads the book using its id to search inside the file, then tales the bytes and returns de reconstructed book
     * @param id id of the book
     * @return Return the recovered book
     * @throws IOException if an I/O error occurs.
     */
    public Book read(long id) throws IOException {
        this.raf.seek(id*Book.SIZE);
        byte[] record = new byte[Book.SIZE];
        this.raf.read(record);
        return Book.fromBytes(record);
    }

    /**
     * Converts the book to bytes, and it writes it inside the file.
     * @param book book to write
     * @throws IOException if an I/O error occurs.
     */
    public void write(Book book) throws IOException {
        this.raf.seek(book.getId()*Book.SIZE);
        byte[] record = book.toBytes();
        this.raf.write(record);
    }

    /**
     * Counts the number of books contained in the file
     * It takes the total length of the file in bytes, and it divides by the length of each book
     * @return Returns the number of books.
     * @throws IOException if an I/O error occurs.
     */
    public long length() throws IOException {
        return this.raf.length()/Book.SIZE;
    }

    /**
     * Closes the file
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        this.raf.close();
    }
}