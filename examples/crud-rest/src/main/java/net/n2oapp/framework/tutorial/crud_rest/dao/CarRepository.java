package net.n2oapp.framework.tutorial.crud_rest.dao;

import net.n2oapp.framework.tutorial.crud_rest.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "cars")
public interface CarRepository extends PagingAndSortingRepository<Car, Long> {
    @Query("SELECT s FROM Car s WHERE (:minPrice is null or s.price >= :minPrice) and (:maxPrice is null or s.price <= :maxPrice)")
    Page<Car> findAll(Pageable pageable,
                      @Param("minPrice") Long minPrice,
                      @Param("maxPrice") Long maxPrice);
}