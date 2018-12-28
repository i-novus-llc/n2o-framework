package net.n2oapp.tutorial.dao;

import net.n2oapp.tutorial.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query("select id from Category")
    long[] findAllCategoryId();
}
