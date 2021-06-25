package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.dto.BooksCheck;
import in.ongrid.kshitijroy.model.entity.BookTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookTitleRepo extends JpaRepository<BookTitle,Long> , BookTitleRepoCustom{
  List<BookTitle> findByCategoryObId(Long id);
  @Query(value = "select * from library.book_title as lb where lb.book_name LIKE %?1% ",nativeQuery = true)
  List<BookTitle> findByName(String bookName);
/*
  @Query(value= "select java.in.ongrid.kshitijroy.model.dto.BooksCheck.java" +
          "(bt.id as bt_id,bt.available,b.id as b_id,b.booked) from library.book_title as bt left join library.book " +
          "as b on bt.id=b.book_title_id where " +
           "bt.available>=1 and  b.booked=false and  bt.id=?1 ", nativeQuery = true)
   List<BooksCheck> findBooks(Long num);

 */
}
