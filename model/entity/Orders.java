package in.ongrid.kshitijroy.model.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Orders extends ResourceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date issueDate;

    @ManyToOne
    private User userOrder;



    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    List<BooksOrdered> booksOrderedList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public User getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(User userOrder) {
        this.userOrder = userOrder;
    }

    public List<BooksOrdered> getBooksOrderedList() {
        return booksOrderedList;
    }

    public void setBooksOrderedList(List<BooksOrdered> booksOrderedList) {
        this.booksOrderedList = booksOrderedList;
    }
}
