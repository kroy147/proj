package in.ongrid.kshitijroy.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BooksCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookTitleId;
    private int  copiesLeft;
    private Long bookId;
    private boolean booked;

    public Long getBookTitleId() {
        return bookTitleId;
    }

    public void setBookTitleId(Long bookTitleId) {
        this.bookTitleId = bookTitleId;
    }

    public int getCopiesLeft() {
        return copiesLeft;
    }

    public void setCopiesLeft(int copiesLeft) {
        this.copiesLeft = copiesLeft;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public BooksCheck() {
    }

    public BooksCheck(Long id, Long bookTitleId, int copiesLeft, Long bookId, boolean booked) {
        this.id = id;
        this.bookTitleId = bookTitleId;
        this.copiesLeft = copiesLeft;
        this.bookId = bookId;
        this.booked = booked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
