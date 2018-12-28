package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Репозиторий для операций с категориями
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    /**
     * Находит все id категорий
     *
     * @return long массив с id категорий
     */
    @Query("select id from Category")
    long[] findAllCategoryId();
}
