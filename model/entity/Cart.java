package in.ongrid.kshitijroy.model.entity;

import javax.persistence.*;
import java.util.*;
@Entity
public class Cart extends ResourceInfo {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private boolean purchased;

    @ManyToOne
    private User userCart;


    @ManyToMany(mappedBy = "carts")
    List<BookTitle> book= new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public User getUserCart() {
        return userCart;
    }

    public void setUserCart(User userCart) {
        this.userCart = userCart;
    }

    public List<BookTitle> getBook() {
        return book;
    }

    public void setBook(List<BookTitle> book) {
        this.book = book;
    }
}
