package net.n2oapp.tutorial.service;

import net.n2oapp.tutorial.dao.AddressRepository;
import net.n2oapp.tutorial.dao.CategoryRepository;
import net.n2oapp.tutorial.dao.ProductRepository;
import net.n2oapp.tutorial.dao.ShopRepository;
import net.n2oapp.tutorial.model.Address;
import net.n2oapp.tutorial.model.Category;
import net.n2oapp.tutorial.model.Product;
import net.n2oapp.tutorial.model.Shop;
import net.n2oapp.tutorial.service.ShopService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void up() {

    }

    @Test
    public void crudShop() {
        Shop shop = createShop();

        //create
        Long id = shopRepository.save(shop).getId();
        shop = shopRepository.findOne(id);
        assertThat(shop.address.location, is("Address1"));

        //update
        shop.address.location = "Address2";
        shopRepository.save(shop);
        shop = shopRepository.findOne(id);
        assertThat(shop.address.location, is("Address2"));

        //delete
        Long addressId = shop.address.id;
        shop.address = null;
        shopRepository.save(shop);
        shop = shopRepository.findOne(id);
        assertThat(shop.address, nullValue());
        assertThat(addressRepository.findOne(addressId), nullValue());
        shopRepository.delete(id);
        assertThat(shopRepository.findOne(id), nullValue());
    }

    @Test
    public void crudProduct() {
        Shop shop1 = createShop();
        Shop shop2 = createShop();
        Category category = createCategory();

        //create
        Product product = new Product();
        product.name = "Product";
        product.category = new Category(category.getId());
        product.shops = new ArrayList<>();
        product.shops.add(new Shop(shop1.getId()));
        product.shops.add(new Shop(shop2.getId()));
        Long id = productRepository.save(product).getId();
        product = productRepository.findOne(id);
        assertThat(product.name, is("Product"));
        assertThat(product.shops.size(), is(2));

        //update
        product.name = "Product2";
        productRepository.save(product);
        product = productRepository.findOne(id);
        assertThat(product.name, is("Product2"));


        //delete product shop
        product.shops = new ArrayList<>();
        product.shops.add(new Shop(shop1.getId()));
        productRepository.save(product);
        product = productRepository.findOne(id);
        assertThat(product.shops.size(), is(1));
        assertThat(product.shops.get(0).getId(), is(shop1.getId()));
        assertThat(shopRepository.findOne(shop2.getId()), notNullValue());

        //delete all product shops
        product.shops = null;
        productRepository.save(product);
        product = productRepository.findOne(id);
        assertThat(product.shops.size(), is(0));
        assertThat(shopRepository.findOne(shop1.getId()), notNullValue());

        //delete product
        productRepository.delete(id);
        assertThat(productRepository.findOne(id), nullValue());
    }

    private Shop createShop() {
        Shop shop = new Shop();
        shop.name = "Shop";
        shop.address = new Address();
        shop.address.location = "Address1";
        return shopRepository.save(shop);
    }

    private Category createCategory() {
        Category category = new Category();
        category.name = "Category";
        categoryRepository.save(category);
        return category;
    }
}
