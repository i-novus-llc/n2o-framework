package net.n2oapp.tutorial.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.tutorial.dao.AddressRepository;
import net.n2oapp.tutorial.dao.ShopRepository;
import net.n2oapp.tutorial.model.Shop;
import net.n2oapp.tutorial.util.ShopCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для поиска по магазинам
 */
@Service
public class ShopService implements CollectionPageService<ShopCriteria, Shop> {

    @Autowired
    private final ShopRepository shopRepository;
    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    ShopService(ShopRepository shopRepository, AddressRepository addressRepository) {
        this.shopRepository = shopRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public CollectionPage<Shop> getCollectionPage(ShopCriteria criteria) {
        String sortingOrder = "ASC";
        String sortingColumn = "name";
        if (criteria.getSorting() != null) {
            sortingOrder = criteria.getSorting().getDirection().getExpression();
            sortingColumn = criteria.getSorting().getField();
        }
        PageRequest pageRequest = new PageRequest(criteria.getPage() - 1, criteria.getSize(), Sort.Direction.fromString(sortingOrder), sortingColumn);
        Long productId = criteria.getProductId();
        List<Shop> shops = productId != null ? this.shopRepository.findWithPagination(pageRequest, productId)
                : this.shopRepository.findAllWithPagination(pageRequest);
        int size = productId != null ? this.shopRepository.find(productId).size()
                : (int) this.shopRepository.count();
        return new CollectionPage(size, shops, criteria);
    }

    /**
     * Возвращает CollectionPage из одного магазина по id
     *
     * @param shopId id товара
     * @return CollectionPage
     */
    Shop getShopById(Long shopId) {
        Shop shop = shopRepository.findOne(shopId);
        return shop;
    }

    /**
     * Создание магазина
     *
     * @param inputShop объект товара
     * @return созданный товар
     */
    Shop create(Shop inputShop) {
        Shop result = shopRepository.save(inputShop);
        return result;
    }

    /**
     * Изменение магазина
     *
     * @param inputShop объект магазина
     * @return измененный товар
     */
    Shop update(Shop inputShop) {
        Shop result = shopRepository.save(inputShop);
        return result;
    }

    String delete(Long shopId) {
        shopRepository.delete(shopId);
        return null;
    }
}
