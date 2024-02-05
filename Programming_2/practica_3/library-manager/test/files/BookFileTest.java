
package files;

import domain.Book;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jmgimeno
 */
public class BookFileTest {

    private static final String BOOKS = "test_books.dat";

    private final BooksFile bookFile;
    private final Book book1;
    private final Book book2;

    public BookFileTest() throws IOException {
        bookFile = new BooksFile(BOOKS);
        bookFile.clear();
        book1 = new Book(1L, "Título 1");
        book1.setIdMember(2L);
        book2 = new Book(2L, "Título 2");
    }

    @Test
    public void roundtrip() throws IOException {
        bookFile.write(book1);
        bookFile.write(book2);
        Book recovered1 = bookFile.read(1L);
        Book recovered2 = bookFile.read(2L);

        assertEqualsBook(book1, recovered1);
        assertEqualsBook(book2, recovered2);
    }

    private static void assertEqualsBook(Book book1, Book book2) {
        assertEquals(book1.getId(), book2.getId());
        assertEquals(book1.getTitle(), book2.getTitle());
        assertEquals(book1.getIdMember(), book2.getIdMember());
    }
}
