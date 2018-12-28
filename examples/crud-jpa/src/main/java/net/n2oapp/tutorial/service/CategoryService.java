package net.n2oapp.tutorial.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.tutorial.dao.CategoryRepository;
import net.n2oapp.tutorial.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для поиска по категориям
 */
@Service
public class CategoryService implements CollectionPageService<Criteria, Category> {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CollectionPage<Category> getCollectionPage(Criteria criteria) {
        List<Category> categoryList = (List<Category>) this.categoryRepository.findAll();
        List<Category> fullCount = (List<Category>) this.categoryRepository.findAll();
        return new CollectionPage(fullCount.size(), categoryList, criteria);
    }
}
