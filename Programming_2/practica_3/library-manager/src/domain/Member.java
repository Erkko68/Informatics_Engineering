
package domain;

import utils.PackUtils;

public class Member {
    
    private static final int MAX_BOOKS = 3;
    
    public static final int NAME_LIMIT = 20;
    public static final int ADDRESS_LIMIT = 30;
    /**
     * Total size (in bytes) of the member information
     */
    public static final int SIZE = 8 + NAME_LIMIT*2 + ADDRESS_LIMIT*2 + 8*3;
    
    private final long id;
    private final String name;
    private final String address;
    private final long[] idBooks;

    /**
     * Initualizes a new member with no books associed
     * @param id Member Id
     * @param name Member Name
     * @param address Member address
     */
    public Member(long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;

        this.idBooks = new long[MAX_BOOKS];
        for(int i=0;i<MAX_BOOKS;i++){
            this.idBooks[i] = -1L;
        }
    }

    /**
     * Initializes a new member with predefined idBooks
     * @param id Member Id
     * @param name Member Name
     * @param address Member address
     * @param idBooks Member books id's
     */
    public Member(long id, String name, String address, long[] idBooks) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idBooks = idBooks;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    /**
     * Count the number of books associated with the member,
     * it uses a loop looking for which positions aren't empty
     * @return Returns the number of books used
     */
    public int countBooks() {
        int count = 0;
        for (long idBook : this.idBooks) {
            if (idBook != -1L) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * If the member doesn't have less than the MAX_BOOKS it can borrow
     * @return Return ture if it can borrow or false if not.
     */
    public boolean canBorrow() {
        return countBooks()<MAX_BOOKS;
    }

    /**
     * Starts writing the book id at the last position if it can borrow more books
     * (It assumes that the books are ordered in the array from left to right so there aren't any empty spaces between books)
     * @param idBook Book id
     */
    public void addBook(long idBook) {
        if(canBorrow()) {
            this.idBooks[countBooks()] = idBook;
        }
    }

    /**
     * Removes the selected book.
     * Checks its position to empty it.
     * @param idBook Book to be removed.
     */
    public void removeBook(long idBook) {
        for (int i = 0; i < MAX_BOOKS; i++) {
            if(this.idBooks[i] == idBook){
                this.idBooks[i] = -1L;
            }
        }
    }

    /**
     * Search in the idBooks array if any of the containing books matches the one that the is being looking for.
     * @param idBook Book id being searched
     * @return Returns true if found, false if not.
     */
    public boolean containsBook(long idBook) {
        for(int i=0;i<MAX_BOOKS;i++){
            if(this.idBooks[i]==idBook){
                return true;
            }
        }
        return false;
    }
    
    
    public static Member fromBytes(byte[] record) {
        int offset = 0;
        long id = PackUtils.unpackLong(record,offset);
        offset += 8;
        String name = PackUtils.unpackLimitedString(NAME_LIMIT,record,offset);
        offset += NAME_LIMIT*2;
        String address = PackUtils.unpackLimitedString(ADDRESS_LIMIT,record,offset);
        offset += ADDRESS_LIMIT*2;
        long[] books = new long[MAX_BOOKS];

        for(int i=0;i<MAX_BOOKS;i++){
            books[i] = PackUtils.unpackLong(record,offset);
            offset += 8;
        }
        return new Member(id,name,address,books);
    }

    public byte[] toBytes() {
        byte[] member = new byte[SIZE];
        int offset = 0;
        PackUtils.packLong(this.id,member,offset);
        offset += 8;
        PackUtils.packLimitedString(this.name,NAME_LIMIT,member,offset);
        offset += NAME_LIMIT*2;
        PackUtils.packLimitedString(this.address,ADDRESS_LIMIT,member,offset);
        offset += ADDRESS_LIMIT*2;

        for(int i=0;i<MAX_BOOKS;i++){
            PackUtils.packLong(this.idBooks[i],member,offset);
            offset += 8;
        }
        return member;
    }

}
