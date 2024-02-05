
package files;

import domain.Book;
import domain.Member;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.MemoryManagerMXBean;

/**
 *
 * @author jmgimeno
 */
public class MembersFile {

    private final RandomAccessFile raf;

    /**
     * Constructor of the class
     * @param fname File name as string
     * @throws IOException if an I/O error occurs.
     */
    public MembersFile(String fname) throws IOException {
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
     * Reads the member using its id to search inside the file, then tales the bytes and returns de recovered member
     * @param id id of the member
     * @return Return the recovered member
     * @throws IOException if an I/O error occurs.
     */
    public Member read(long id) throws IOException {
        this.raf.seek(id*Member.SIZE);
        byte[] record = new byte[Member.SIZE];
        this.raf.read(record);
        return Member.fromBytes(record);
    }

    /**
     * Converts the member to bytes, and it writes it inside the file.
     * @param member member to write
     * @throws IOException if an I/O error occurs.
     */
    public void write(Member member) throws IOException {
        this.raf.seek(member.getId()*Member.SIZE);
        byte[] record = member.toBytes();
        this.raf.write(record);
    }

    /**
     * Counts the number of books contained in the file
     * It takes the total length of the file in bytes, and it divides by the length of each member
     * @return Returns the number of members.
     * @throws IOException if an I/O error occurs.
     */
    public long length() throws IOException {
        return this.raf.length()/ Member.SIZE;
    }

    /**
     * Closes the file
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        this.raf.close();
    }
}