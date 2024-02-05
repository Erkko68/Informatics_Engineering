
package domain;

import utils.PackUtils;

public class Book {
    
    public static final int TITLE_LIMIT = 20;
    /**
     * SIZE final int represents the max length of
     * each member data, the value 8 corresponds to the length in bytes
     * of the longs added with the TITLE_LIMIT constant.
     * Is represented as:
     * id+Book_title+idMember
     */
    public static final int SIZE = 8+TITLE_LIMIT*2+8;
    
    private final long id;
    private final String title;
    private long idMember;

    /**
     * Constructor with empty idMember
     * @param id Id
     * @param title Book title
     */
    public Book(long id, String title) {
        this.id = id;
        this.title = title;
        this.idMember = -1L;
    }

    /**
     * Constructor
     * @param id ID
     * @param title Book title
     * @param idSoci Member id
     */
    public Book(long id, String title, long idSoci) {
        this.id = id;
        this.title = title;
        this.idMember = idSoci;
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public long getIdMember() {
        return this.idMember;
    }

    public void setIdMember(long idMember) {
        this.idMember = idMember;
    }

    /**
     * Decode book represented as binari into a new nook.
     * @param record Array of bytes where the book is codified
     * @return Returns a new book with the informtion stored in the bytes
     */
    public static Book fromBytes(byte[] record) {
        int offset = 0;
        long id = PackUtils.unpackLong(record,offset);
        offset += 8;
        String tittle = PackUtils.unpackLimitedString(TITLE_LIMIT,record,offset);
        offset += TITLE_LIMIT*2;
        long idMember = PackUtils.unpackLong(record,offset);
        return new Book(id,tittle,idMember);
    }

    /**
     * Stores the book as a new array of bytes
     * @return returns an array of bytes with the book information
     */
    public byte[] toBytes() {
        byte[] book = new byte[SIZE];
        int offset = 0;
        PackUtils.packLong(this.id,book,offset);
        offset += 8;
        PackUtils.packLimitedString(this.title,TITLE_LIMIT,book,offset);
        offset += TITLE_LIMIT*2;
        PackUtils.packLong(this.idMember,book,offset);
        return book;
    }

}
