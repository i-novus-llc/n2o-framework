package net.n2oapp.tutorial.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.tutorial.dao.CategoryRepository;
import net.n2oapp.tutorial.dao.ProductRepository;
import net.n2oapp.tutorial.model.Product;
import net.n2oapp.tutorial.util.ProductCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для поиска по товарам, и CRUD операций
 */
@Service
public class ProductService implements CollectionPageService<ProductCriteria, Product> {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CollectionPage<Product> getCollectionPage(ProductCriteria criteria) {
        if (criteria.getId() != null) {
            Product product = getProductById(criteria.getId());
            List<Product> oneProduct = new ArrayList<>();
            oneProduct.add(product);
            return new CollectionPage(1, oneProduct, criteria);
        }
        String sortingOrder = "ASC";
        String sortingColumn = "name";
        if (criteria.getSorting() != null) {
            sortingOrder = criteria.getSorting().getDirection().getExpression();
            sortingColumn = criteria.getSorting().getField();
        }
        PageRequest pageRequest = new PageRequest(criteria.getPage() - 1, criteria.getSize(), Sort.Direction.fromString(sortingOrder), sortingColumn);
        long[] categoryId =
                (criteria.getCategoryId() == null)
                        ? categoryRepository.findAllCategoryId()
                        : criteria.getCategoryId().stream().mapToLong(l -> l).toArray();
        Long minPrice = criteria.getMinPrice() != null ? criteria.getMinPrice() : 0;
        Long maxPrice = criteria.getMaxPrice() != null ? criteria.getMaxPrice() : Long.MAX_VALUE;
        List<Product> products = this.productRepository.findWithPagination(pageRequest, categoryId, minPrice, maxPrice);
        int fullCount = this.productRepository.findCount(categoryId, minPrice, maxPrice);
        return new CollectionPage(fullCount, products, criteria);
    }

    /**
     * Возвращает CollectionPage из одного товара в соответствии с id
     *
     * @param productId id товара
     * @return CollectionPage
     */
    Product getProductById(Long productId) {
        Product product = this.productRepository.findOne(productId);
        return product;
    }

    /**
     * Создание товара
     *
     * @param inputProduct объект товара
     * @return созданный товар
     */
    Product create(Product inputProduct) {
        Product result = productRepository.save(inputProduct);
        return result;
    }

    /**
     * Изменение товара
     *
     * @param inputProduct объект товара
     * @return измененный товар
     */
    Product update(Product inputProduct) {
        Product result = productRepository.save(inputProduct);
        return result;
    }

    /**
     * Удаление товара
     *
     * @param productId объект товара
     * @return null
     */
    String delete(Long productId) {
        productRepository.delete(productId);
        return null;
    }
}
