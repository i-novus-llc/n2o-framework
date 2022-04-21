package net.n2oapp.framework.sandbox.cases.cars;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "cars")
public interface CarRepository extends PagingAndSortingRepository<Car, Long> {
    @Query("SELECT s FROM Car s WHERE (:name is null or name LIKE '%' || :name || '%') and " +
            "(:minPrice is null or s.price >= :minPrice) and (:maxPrice is null or s.price <= :maxPrice)")
    Page<Car> findAll(Pageable pageable,
                      @Param("name") String name,
                      @Param("minPrice") Long minPrice,
                      @Param("maxPrice") Long maxPrice);
}
