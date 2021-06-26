package in.ongrid.kshitijroy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BookTitle extends ResourceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String bookName;

    @Column
    private String authorName;

    @Column
    private String bookCover;

    @Column
    private int available;

    @JsonIgnore
    @ManyToOne
    private Category categoryOb;


    @JsonIgnore
    @OneToMany(mappedBy = "bookTitle",cascade = CascadeType.ALL)
    List<Book> book = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "book_title_cart",joinColumns = @JoinColumn(name = "book_title_id"),inverseJoinColumns = @JoinColumn(name = "cart_id"))
    List<Cart> carts=new ArrayList<>();


    @JsonIgnore
    @OneToOne
    private BooksOrdered bookOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Category getCategoryOb() {
        return categoryOb;
    }

    public void setCategoryOb(Category categoryOb) {
        this.categoryOb = categoryOb;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
