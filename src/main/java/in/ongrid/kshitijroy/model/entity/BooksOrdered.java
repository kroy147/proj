package in.ongrid.kshitijroy.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BooksOrdered extends ResourceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="orders_id")
    private Orders orders;

    @ManyToOne
    private User userBook;

    @Column
    private Long bookId;

    @Column
    public Date returnDate;

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public User getUserBook() {
        return userBook;
    }

    public void setUserBook(User userBook) {
        this.userBook = userBook;
    }


}
