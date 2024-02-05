
package domain;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jmgimeno
 */
public class BookTest {

    @Test
    public void aBookIsCreatedOk() {
        Book book = new Book(2L, "Apunts de Programació 2");

        assertEquals(2L, book.getId());
        assertEquals("Apunts de Programació 2", book.getTitle());
        assertEquals(-1L, book.getIdMember());
    }

    @Test
    public void canLendBook() {
        Book book = new Book(2L, "Apunts de Programació 2");
        book.setIdMember(5L);

        assertEquals(5L, book.getIdMember());
    }

    @Test
    public void roundtrip() {
        Book book = new Book(2L, "Apunts de Programació 2");
        book.setIdMember(5L);

        byte[] record = book.toBytes();
        Book recoveredBook = Book.fromBytes(record);

        assertEquals(book.getId(), recoveredBook.getId());
        assertEquals(book.getTitle().substring(0, Book.TITLE_LIMIT), recoveredBook.getTitle());
        assertEquals(book.getIdMember(), recoveredBook.getIdMember());
    }
}
