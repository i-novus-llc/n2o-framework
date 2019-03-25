package net.n2oapp.tutorial.rest;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.tutorial.dao.CategoryRepository;
import net.n2oapp.tutorial.dao.ProductRepository;
import net.n2oapp.tutorial.model.Category;
import net.n2oapp.tutorial.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/products")
class ProductRestController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductRestController(@Autowired ProductRepository productRepository,
                                 @Autowired CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/categories")
    public CollectionPage getCategories() {
        Collection<Category> categories = (Collection<Category>) categoryRepository.findAll();
        return new CollectionPage(categories.size(), categories, new Criteria());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/categories/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        Category result = categoryRepository.findById(categoryId).get();
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public CollectionPage<Product> getProducts(@RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                               @RequestParam(value = "sortingColumn", required = false, defaultValue = "name") String sortingColumn,
                                               @RequestParam(value = "sortingOrder", required = false, defaultValue = "asc") String sortingOrder,
                                               @RequestParam(value = "minPrice", required = false, defaultValue = "0") long minPrice,
                                               @RequestParam(value = "maxPrice", required = false, defaultValue = "" + Long.MAX_VALUE) long maxPrice,
                                               @RequestParam(value = "categoryId", required = false) long[] categoryId) {

        PageRequest pageRequest = new PageRequest(page - 1, size, Sort.Direction.fromString(sortingOrder), sortingColumn);
        categoryId = (categoryId == null) ? categoryRepository.findAllCategoryId() : categoryId;
        Collection<Product> products = this.productRepository.findWithPagination(pageRequest, categoryId, minPrice, maxPrice);
        Collection<Product> fullCount = this.productRepository.find(categoryId, minPrice, maxPrice);
        return new CollectionPage(fullCount.size(), products, new Criteria());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return this.productRepository.findById(productId).get();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Product create(@RequestBody Product form) {
        return productRepository.save(form);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Product update(@RequestBody Product form) {
        return productRepository.save(form);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{productId}")
    public void delete(@PathVariable Long productId) {
        productRepository.deleteById(productId);
    }
}
