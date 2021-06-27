package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.dto.BooksCheck;
import in.ongrid.kshitijroy.model.entity.BookTitle;

import java.util.List;

public interface BookTitleRepoCustom {
    List<BookTitle> findBooks(List<Long> num);
}
