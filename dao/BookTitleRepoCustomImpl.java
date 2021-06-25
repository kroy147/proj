package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.dto.BooksCheck;
import org.springframework.stereotype.Repository;
import in.ongrid.kshitijroy.model.entity.BookTitle;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class BookTitleRepoCustomImpl implements BookTitleRepoCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<BooksCheck> findBooks(Long num){
        Query query= entityManager.createNativeQuery("select bt.id as bt_id,bt.available,b.id as b_id,b.booked " +
                "from library.book_title as bt left join library.book " +
                "as b on bt.id=b.book_title_id where " +
                "bt.available>=1 and  b.booked=false and  bt.id=?1 ",BooksCheck.class);
        query.setParameter(1,num);
        return query.getResultList();
    }
}
