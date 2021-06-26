package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {

}
