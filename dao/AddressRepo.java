package in.ongrid.kshitijroy.dao;

import in.ongrid.kshitijroy.model.entity.Address;
import in.ongrid.kshitijroy.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {

}
