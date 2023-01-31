package team.ranunculus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView getCategory(ModelAndView modelAndView) {
        List<ProductEntity> productList=this.productService.getProductList();
        modelAndView.addObject("list", productList);
        modelAndView.addObject("imgUtil", new ImageUtils());
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
