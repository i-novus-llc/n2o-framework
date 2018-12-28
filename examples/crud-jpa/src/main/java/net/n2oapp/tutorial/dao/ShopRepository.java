package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для операций с магазинами
 */
public interface ShopRepository extends PagingAndSortingRepository<Shop, Long> {

    /**
     * Возвращает список магазинов в соответствии с id товара, учитывая пагинацию
     *
     * @param pageable  объект для пагинации
     * @param productId id товара
     * @return список магазинов
     */
    @Query("select s from Shop s inner join s.products ps where product_id = :productId")
    List<Shop> findWithPagination(Pageable pageable, @Param("productId") long productId);

    /**
     * Возвращает список всех магазинов, учитывая пагинацию
     *
     * @return список магазинов
     */
    @Query("select s from Shop s ")
    List<Shop> findAllWithPagination(Pageable pageable);

    /**
     * Возвращает список магазинов в соответствии с id товара
     *
     * @param productId id товара
     * @return список магазинов
     */
    @Query("select s from Shop s inner join s.products ps where product_id = :productId")
    List<Shop> find(@Param("productId") long productId);

    /**
     * Возвращает список магазинов в соответствии с id товара
     *
     * @param productId id товара
     * @return список магазинов
     */
    @Query("select s from Shop s inner join s.products ps where product_id = :productId")
    String delete(@Param("productId") long productId);
}
