package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.dto.BooksCheck;
import in.ongrid.kshitijroy.model.entity.BookTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTitleRepo extends JpaRepository<BookTitle,Long> , BookTitleRepoCustom{
  List<BookTitle> findByCategoryObId(Long id);
  @Query(value = "select * from library.book_title as lb where lb.book_name LIKE %?1% ",nativeQuery = true)
  List<BookTitle> findByName(String bookName);
  @Query(value= " SELECT \n" +
          "    bt.id, bt.available, b.id, b.booked\n" +
          "FROM\n" +
          "    (SELECT \n" +
          "        *\n" +
          "    FROM\n" +
          "        book_title\n" +
          "    WHERE\n" +
          "        id IN (:bookTitleList)) AS bt\n" +
          "        LEFT JOIN\n" +
          "    book AS b ON bt.id = b.id\n" +
          "WHERE\n" +
          "    bt.available > 0 AND b.booked = FALSE;", nativeQuery = true)
   List<BookTitle> findBooks(List<Long> bookTitleList);
}
