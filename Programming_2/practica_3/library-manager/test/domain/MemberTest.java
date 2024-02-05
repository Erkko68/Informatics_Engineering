
package domain;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jmgimeno
 */
public class MemberTest {


    @Test
    public void aMemberIsCreatedOk() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");

        assertEquals(2L, member.getId());
        assertEquals("Sacarino", member.getName());
        assertEquals("13 Rue del Percebe", member.getAddress());
        assertEquals(0, member.countBooks());
    }

    @Test
    public void canLendBook() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");
        member.addBook(3L);
        member.addBook(5L);

        assertEquals(2, member.countBooks());
        assertTrue(member.canBorrow());
        assertTrue(member.containsBook(3L));
        assertTrue(member.containsBook(5L));
        assertFalse(member.containsBook(9L));
    }

    @Test
    public void saturatedMember() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");
        member.addBook(3L);
        member.addBook(5L);
        member.addBook(8L);

        assertFalse(member.canBorrow());
    }

    @Test
    public void addToSaturatedDoesNothing() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");
        member.addBook(3L);
        member.addBook(5L);
        member.addBook(8L);
        member.addBook(1L);

        assertEquals(3, member.countBooks());
        assertTrue(member.containsBook(3L));
        assertTrue(member.containsBook(5L));
        assertFalse(member.containsBook(9L));
    }

    @Test
    public void removeExistingBook() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");
        member.addBook(3L);
        member.addBook(5L);
        member.removeBook(3L);

        assertEquals(1, member.countBooks());
        assertTrue(member.containsBook(5L));
        assertFalse(member.containsBook(3L));
    }

    @Test
    public void removeExistingBookDoesNothing() {
        Member member = new Member(2L, "Sacarino", "13 Rue del Percebe");
        member.addBook(3L);
        member.addBook(5L);
        member.removeBook(7L);

        assertEquals(2, member.countBooks());
        assertTrue(member.containsBook(3L));
        assertTrue(member.containsBook(5L));
    }

    @Test
    public void roundTripNotSaturatedMember() {
        Member member = new Member(2L, "Mortadelo y Filemón y Sacarino",
                "13 Rue del Percebe Bajando por la derecha todo al fondo");
        member.addBook(3L);
        member.addBook(5L);
        byte[] record = member.toBytes();
        Member recoveredMember = Member.fromBytes(record);

        assertEquals(2L, recoveredMember.getId());
        assertEquals("Mortadelo y Filemón y Sacarino".substring(0, Member.NAME_LIMIT), recoveredMember.getName());
        assertEquals("13 Rue del Percebe Bajando por la derecha todo al fondo".substring(0, Member.ADDRESS_LIMIT), recoveredMember.getAddress());
        assertEquals(2, recoveredMember.countBooks());
        assertTrue(recoveredMember.containsBook(3L));
        assertTrue(recoveredMember.containsBook(5L));
    }
}
