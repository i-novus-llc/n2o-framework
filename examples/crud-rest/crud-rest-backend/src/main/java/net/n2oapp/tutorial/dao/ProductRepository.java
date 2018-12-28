package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("select s from Product s where s.category.id in :categoryIdList and s.price between :minPrice and :maxPrice")
    List<Product> findWithPagination(Pageable pageable, @Param("categoryIdList") long[] categoryIdList, @Param("minPrice") long minPrice, @Param("maxPrice") long maxPrice);

    @Query("select s from Product s where s.category.id in :categoryIdList and s.price between :minPrice and :maxPrice")
    List<Product> find(@Param("categoryIdList") long[] categoryIdList, @Param("minPrice") long minPrice, @Param("maxPrice") long maxPrice);
}
