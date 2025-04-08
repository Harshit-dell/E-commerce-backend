package com.example.E_Commers.Controller;

import com.example.E_Commers.model.Product;
import com.example.E_Commers.service.ProductService;
import org.apache.coyote.Response;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet(){
        return "heello";
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product> > getAllProducts(){
         return new ResponseEntity<>(service.getAllProdcuts(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product=service.getProductById(id);
        if(product!=null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        return new ResponseEntity<>(product,HttpStatus.NOT_FOUND);
    }




    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try {
            System.out.println(product);
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //this is going to service side by using getProductById and thus return it
    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product=service.getProductById(productId);
        byte[] imageFile=product.getImageDate();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);

    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile) throws IOException {

       Product product1= service.updateProduct(id,product,imageFile);
        if(product1!=null){
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("FAILED TO UPDATE",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
            Product product= service.getProductById(id);
            if(product!=null){
                service.deleteProduct(id);
                return new ResponseEntity<>("Worked",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Failed",HttpStatus.NOT_FOUND);
            }

    }

    @GetMapping("/product/search?{keyword}")
    public ResponseEntity<List<Product>> searchProduct(String keyword){
        List<Product> product = service.searchProduct(keyword);
            return new ResponseEntity<>(product,HttpStatus.OK);
    }



}
