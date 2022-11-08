package team.ranunculus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.mappers.IProductMapper;

import java.util.List;

@Service
public class AdminService {
    private final IProductMapper adminMapper;

    public AdminService(IProductMapper adminMapper) {
        this.adminMapper = adminMapper;
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

        if(this.adminMapper.insertProduct(product)==0){
            System.out.println("추가된 값이 0개다!");
            return CommonResult.FAILURE;
        }

        return CommonResult.SUCCESS;
    }

    public IResult addCapacity(CapacityEntity capacity){
        if(capacity.getText()==null){
            return CommonResult.FAILURE;
        }
        if(this.adminMapper.insertCapacityOption(capacity)==0){
//            System.out.println("추가에 실패했다!");
            return CommonResult.FAILURE;
        }
//        System.out.println("성공했다!");
        return CommonResult.SUCCESS;
    }

    public List<CapacityEntity> loadCapacityOptions(){
        List<CapacityEntity>list=adminMapper.selectCapacity();
        return list;
    }

    public List<CategoryEntity> loadCategoryOptions(){
        List<CategoryEntity>list=adminMapper.selectCategory();
        return list;
    }
}
