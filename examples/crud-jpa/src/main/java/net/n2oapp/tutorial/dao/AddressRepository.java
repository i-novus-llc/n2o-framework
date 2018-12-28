package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
