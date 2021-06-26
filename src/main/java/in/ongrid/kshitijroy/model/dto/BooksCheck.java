package in.ongrid.kshitijroy.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class BooksCheck implements Serializable {
    private Long book_title_id;
    private int  available;
    private Long book_id;
    private boolean booked;

    public Long getBook_title_id() {
        return book_title_id;
    }

    public void setBook_title_id(Long book_title_d) {
        this.book_title_id = book_title_d;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public BooksCheck() {
    }

    public BooksCheck(Long book_title_id, int available, Long book_id, boolean booked) {
        this.book_title_id = book_title_id;
        this.available = available;
        this.book_id = book_id;
        this.booked = booked;
    }
}
