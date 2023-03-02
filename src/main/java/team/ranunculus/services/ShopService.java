package team.ranunculus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.mappers.IProductMapper;
import java.util.List;

@Service
public class ShopService {
    private final IProductMapper productMapper;

    @Autowired
    public ShopService(IProductMapper productMapper) {
        this.productMapper = productMapper;
    }


}
