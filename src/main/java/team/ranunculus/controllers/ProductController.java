package team.ranunculus.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import team.ranunculus.entities.member.ContactAuthEntity;
import team.ranunculus.entities.member.UserEntity;
import team.ranunculus.entities.product.CapacityEntity;
import team.ranunculus.entities.product.CategoryEntity;
import team.ranunculus.entities.product.ProductEntity;
import team.ranunculus.enums.CommonResult;
import team.ranunculus.interfaces.IResult;
import team.ranunculus.services.ProductService;
import team.ranunculus.utils.ImageUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "addProd", method = RequestMethod.GET)
    public ModelAndView getAddProd(ModelAndView modelAndView,
                                   @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user) {
        if(user==null){
//            System.out.println("로그인 상태가 아닌 유저가 상품 추가를 시도하면");
            modelAndView.setViewName("redirect:/member/userLogin");
        }
        else if(productService.checkIsAdmin(user)) {
//            System.out.println("로그인 상태인 유저가 어드민이면");
            modelAndView.setViewName("product/addProd");
        }
        else{
            //로그인한 상태의 유저가 어드민이 아니면
            modelAndView.setViewName("redirect:/");
        }
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

        IResult result = this.productService.insertProduct(product);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        System.out.println(result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "managementProd", method = RequestMethod.GET)
    public ModelAndView getManagementProd(ModelAndView modelAndView) {
        List<ProductEntity> productList=this.productService.getProductList();
        List<CapacityEntity> capacityList=this.productService.loadCapacityOptions();
        modelAndView.addObject("capacityList",capacityList);
        modelAndView.addObject("list", productList);
        modelAndView.addObject("imgUtil", new ImageUtils());
        modelAndView.setViewName("product/managementProd");
        return modelAndView;
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public ModelAndView getDetail(@PathVariable(value = "id") int id,
                                ModelAndView modelAndView) {
        ProductEntity product = this.productService.readProductByIndex(id);
        List<CapacityEntity> capacityList=this.productService.loadCapacityOptions();
        List<CategoryEntity> categoryList=this.productService.loadCategoryOptions();
        System.out.println(product.getImage());
//        System.out.println(product.getProdDetailImage());
        modelAndView.addObject("capacityList",capacityList);
        modelAndView.addObject("categoryList",categoryList);
        modelAndView.addObject("imgUtil", new ImageUtils());
        modelAndView.addObject("product", product);
        modelAndView.setViewName("product/detail");
        return modelAndView;
    }

    @RequestMapping(value = "modifyProdDetail/{id}", method = RequestMethod.GET)
    public ModelAndView getModifyProdDetail(@PathVariable(value = "id") int id, ModelAndView modelAndView){

        ProductEntity product = this.productService.readProductByIndex(id);
        List<CapacityEntity> capacityList=this.productService.loadCapacityOptions();
        List<CategoryEntity> categoryList=this.productService.loadCategoryOptions();


        modelAndView.addObject("capacityList",capacityList);
        modelAndView.addObject("categoryList",categoryList);
        modelAndView.addObject("product", product);
        modelAndView.setViewName("product/modifyProdDetail");
        return modelAndView;
    }

    @RequestMapping(value = "modifyProdDetail/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postModifyProdDetail(@PathVariable(value = "id") int id,
                                   @RequestParam(value = "newName") String newName,
                                   @RequestParam(value = "newCostPrice", required = false, defaultValue = "-1") int newCostPrice,
                                   @RequestParam(value = "newNetPrice", required = false, defaultValue = "-1") int newNetPrice,
                                   @RequestParam(value = "newImage", required = false) byte[] newImage,
                                   @RequestParam(value = "newDetailImage", required = false) byte[] newDetailImage,
                                   @RequestParam(value = "newCapacity", required = false, defaultValue = "-1") int newCapacity,
                                   @RequestParam(value = "newCategory", required = false, defaultValue = "-1") int newCategory,
                                   @RequestParam(value = "newStock",required = false, defaultValue = "-1") int newStock)
            throws Exception {
        ProductEntity currentProd = this.productService.readProductByIndex(id);

        System.out.println(currentProd.getIndex());
        ProductEntity newProduct = ProductEntity.build();
        if (newName != null) {
            newProduct.setName(newName);
        }
        if (newImage != null) {
            newProduct.setImage(newImage);
        }
        if (newDetailImage != null){
            newProduct.setProdDetailImage(newDetailImage);
        }
        if (newCostPrice != -1){
            newProduct.setCostPrice(newCostPrice);
        }
        if (newNetPrice != -1){
            newProduct.setNetPrice(newNetPrice);
        }
        if (newCapacity !=-1){
            newProduct.setCapacity(newCapacity);
        }
        if (newCategory !=-1){
            newProduct.setCategory(newCategory);
        }
        if (newStock != -1){
            newProduct.setStock(newCategory);
        }

        JSONObject responseJson = new JSONObject();

        IResult result = this.productService.editProd(currentProd,newProduct);

        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    //옵션 추가하기 버튼을 누르면 작동함
    @RequestMapping(value = "appendOption", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAppendOption(CapacityEntity capacity) {
//        System.out.println(capacity.getText());
        IResult result = this.productService.addCapacity(capacity);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    //용량 옵션을 데이터베이스에서 받아서 HTML 로 보내주기 위해 사용했음
    @ModelAttribute("capacity")
    public List<CapacityEntity> capacityEntities() {
        List<CapacityEntity> capacity = productService.loadCapacityOptions();
        return capacity;
    }

    //카테고리 옵션을 데이터베이스에서 받아서 HTML 로 보내주기 위해 사용했음
    @ModelAttribute("category")
    public List<CategoryEntity> categoryEntities() {
        List<CategoryEntity> category = productService.loadCategoryOptions();
        return category;
    }
}