package team.ranunculus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ranunculus.entities.board.QnaEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IMemberMapper;
import team.ranunculus.mappers.IProductMapper;

import java.util.List;

@Service
public class ProductService {
    private final IProductMapper productMapper;
    private final IMemberMapper memberMapper;

    public ProductService(IProductMapper productMapper, IMemberMapper memberMapper) {
        this.productMapper = productMapper;
        this.memberMapper = memberMapper;
    }

    public ProductEntity readProductByIndex(int id)
    {
        return this.productMapper.selectProductByIndex(id);
    }

    @Transactional
    public IResult insertProduct(ProductEntity product){
        //필수 입력 사항이 없으면
        if (product.getName() == null ||
                product.getCostPrice() == 0 ||
                product.getNetPrice()==0 ||
//                product.getImage() == null ||
                product.getCapacity() == 0 ||
                product.getCategory() == 0 ||
                product.getStock()==0) {
            System.out.println("필수 값이 없다!");
            return CommonResult.FAILURE;
        }

        if(this.productMapper.insertProduct(product)==0){
            System.out.println("추가된 값이 0개다!");
            return CommonResult.FAILURE;
        }

        return CommonResult.SUCCESS;
    }

    public boolean checkIsAdmin(UserEntity user){
        if(memberMapper.selectUserByEmail(user).isAdmin()){
            return true;
        }
        else
            return false;
    }

    public IResult addCapacity(CapacityEntity capacity){
        if(capacity.getText()==null){
            return CommonResult.FAILURE;
        }
        if(this.productMapper.insertCapacityOption(capacity)==0){
//            System.out.println("추가에 실패했다!");
            return CommonResult.FAILURE;
        }
//        System.out.println("성공했다!");
        return CommonResult.SUCCESS;
    }

    public List<ProductEntity> getProductList(){
        return this.productMapper.selectAllProduct();
    }

    public List<CapacityEntity> loadCapacityOptions(){
        List<CapacityEntity>list= productMapper.selectCapacity();
        return list;
    }
    public List<CategoryEntity> loadCategoryOptions(){
        List<CategoryEntity>list= productMapper.selectCategory();
        return list;
    }
}
