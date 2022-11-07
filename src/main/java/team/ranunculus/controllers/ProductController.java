package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.AdminService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    private final AdminService adminService;

    public ProductController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "addProd", method = RequestMethod.GET)
    public ModelAndView getAddProd(ModelAndView modelAndView) {
        modelAndView.setViewName("product/addProd");
        return modelAndView;
    }

    @RequestMapping(value = "addProd", method = RequestMethod.POST)
    @ResponseBody
    public String postAddProd(MultipartHttpServletRequest productData) throws IOException {
        System.out.println(productData.getParameter("prodName"));
        MultipartFile image = productData.getFile("prodImage");
        MultipartFile detailImage=productData.getFile("prodDetailImage");

        ProductEntity product = new ProductEntity();
        product.setName(productData.getParameter ("prodName"))
                .setCapacity(Integer.parseInt(productData.getParameter("prodCapacity")))
                .setCategory(Integer.parseInt(productData.getParameter("prodCategory")))
                .setCostPrice(Integer.parseInt(productData.getParameter("costPrice")))
                .setNetPrice(Integer.parseInt(productData.getParameter("netPrice")))
                .setImage(image.getBytes())
                .setMime(image.getContentType())
                .setProdDetailImage(detailImage.getBytes())
                .setProdDetailImageMime(detailImage.getContentType())
                .setStock(Integer.parseInt(productData.getParameter("stock")))
                .setLaunchingDate(new Date())
                .setStockUpdate(new Date());

        IResult result = this.adminService.insertProduct(product);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "managementProd", method = RequestMethod.GET)
    public ModelAndView getManagementProd(ModelAndView modelAndView) {
        modelAndView.setViewName("product/managementProd");
        return modelAndView;
    }

    @RequestMapping(value = "appendOption", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAppendOption(CapacityEntity capacity) {
        System.out.println(capacity.getText());
        IResult result = this.adminService.addCapacity(capacity);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    //용량 옵션을 데이터베이스에서 받아서 HTML 로 보내주기 위해 사용했음
    @ModelAttribute("capacity")
    public List<CapacityEntity> capacityEntities() {
        List<CapacityEntity> capacity = adminService.loadCapacityOptions();
        return capacity;
    }

    //카테고리 옵션을 데이터베이스에서 받아서 HTML 로 보내주기 위해 사용했음
    @ModelAttribute("category")
    public List<CategoryEntity> categoryEntities() {
        List<CategoryEntity> category = adminService.loadCategoryOptions();
        return category;
    }
}
