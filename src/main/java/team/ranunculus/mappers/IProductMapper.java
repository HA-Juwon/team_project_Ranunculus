package team.ranunculus.mappers;

import org.apache.ibatis.annotations.Mapper;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;

import java.util.List;

@Mapper
public interface IProductMapper {
    int insertProduct(ProductEntity productEntity);
    int insertCapacityOption(CapacityEntity capacity);
    ProductEntity selectProductByIndex(int index);
    List<ProductEntity> selectAllProduct();
    List<CapacityEntity> selectCapacity();
    List<CategoryEntity> selectCategory();
}