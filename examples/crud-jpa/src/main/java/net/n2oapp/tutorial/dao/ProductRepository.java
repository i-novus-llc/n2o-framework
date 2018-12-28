package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для операций с товарами
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    /**
     * Возвращает список товаров в соответствии с категорией, пагинацией и отфильтрованный по цене
     *
     * @param pageable       объект для пагинации
     * @param categoryIdList список категорий
     * @param minPrice       минимальная цена
     * @param maxPrice       максимальная цена
     * @return список товаров
     */
    @Query("select s from Product s where s.category.id in :categoryIdList and s.price between :minPrice and :maxPrice")
    List<Product> findWithPagination(Pageable pageable, @Param("categoryIdList") long[] categoryIdList, @Param("minPrice") long minPrice, @Param("maxPrice") long maxPrice);

    /**
     * Возвращает число товаров в соответствии с категорией и отфильтрованный по цене
     *
     * @param categoryIdList список категорий
     * @param minPrice       минимальная цена
     * @param maxPrice       максимальная цена
     * @return число товаров
     */
    @Query("select count(s) from Product s where s.category.id in :categoryIdList and s.price between :minPrice and :maxPrice")
    int findCount(@Param("categoryIdList") long[] categoryIdList, @Param("minPrice") long minPrice, @Param("maxPrice") long maxPrice);
}
