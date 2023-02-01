package team.ranunculus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.services.ProductService;
import team.ranunculus.services.ShopService;
import team.ranunculus.utils.ImageUtils;

import java.util.List;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {

    private final ProductService productService;
    private final ShopService shopService;

    @Autowired
    public ShopController(ProductService productService, ShopService shopService) {
        this.productService = productService;
        this.shopService = shopService;
    }

    @RequestMapping(value = {"/category","/search"}, method = RequestMethod.GET)
    public ModelAndView getCategory(@RequestParam(value = "keyword", required = false)String keyword,
                                    ModelAndView modelAndView) {
        if(keyword == null) {
        List<ProductEntity> productList=this.productService.getProductList();
        List<CapacityEntity> capacityList=this.productService.loadCapacityOptions();
        List<CategoryEntity> categoryList=this.productService.loadCategoryOptions();
        modelAndView.addObject("list", productList);
        modelAndView.addObject("imgUtil", new ImageUtils());
        modelAndView.addObject("capacity",capacityList);
        modelAndView.addObject("category",categoryList);
        } else {

        }
        modelAndView.setViewName("shop/category");
        return modelAndView;
    }

//    @RequestMapping(value= "/search", )

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public ModelAndView getBasket(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/basket");
        return modelAndView;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView getDetail(ModelAndView modelAndView) {
        modelAndView.setViewName("shop/detailpage");
        return modelAndView;
    }
}
