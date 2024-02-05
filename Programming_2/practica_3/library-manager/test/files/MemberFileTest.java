
package files;

import domain.Member;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jmgimeno
 */
public class MemberFileTest {

    private static final String MEMBERS = "test_members.dat";

    private final MembersFile memberFile;
    private final Member member1;
    private final Member member2;

    public MemberFileTest() throws IOException {
        memberFile = new MembersFile(MEMBERS);
        memberFile.clear();
        member1 = new Member(1L, "Nombre 1", "Dirección 1");
        member1.addBook(4L);
        member1.addBook(7L);
        member1.addBook(12L);
        member2 = new Member(2L, "Nombre 2", "Dirección 2");
    }

    @Test
    public void roundtrip() throws IOException {
        memberFile.write(member1);
        memberFile.write(member2);
        Member recovered1 = memberFile.read(1L);
        Member recovered2 = memberFile.read(2L);

        assertEqualsMember(member1, recovered1);
        assertEqualsMember(member2, recovered2);

        assertTrue(recovered1.containsBook(4L));
        assertTrue(recovered1.containsBook(7L));
        assertTrue(recovered1.containsBook(12L));
    }

    private static void assertEqualsMember(Member member1, Member member2) {
        assertEquals(member1.getId(), member2.getId());
        assertEquals(member1.getName(), member2.getName());
        assertEquals(member1.getAddress(), member2.getAddress());
        assertEquals(member1.countBooks(), member2.countBooks());
    }

}
