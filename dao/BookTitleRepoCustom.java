package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.dto.BooksCheck;

import java.util.List;

public interface BookTitleRepoCustom {
    List<BooksCheck> findBooks(Long num);
}
