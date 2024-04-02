package org.example.controller;

import org.example.dao.ProductDao;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller

public class ProductController {

    @Autowired
    private ProductDao productDao;
    @RequestMapping("/")
    public String home(Model m)
    {
        List<Product> products = productDao.getProducts();
        m.addAttribute("products", products);
        return "index";
    }

    @RequestMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("title", "Add product");
        return "add_product_form";
    }

    // handle add product form
    @RequestMapping(value = "/handle-product" , method=RequestMethod.POST)
    public RedirectView handleProduct(@ModelAttribute Product product ,HttpServletRequest request){
        System.out.println(product);
        productDao.createProduct(product);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath()+"/");
        return redirectView;
    }
    // delete handler
    @RequestMapping("/delete/{productId}")
    public  RedirectView deleteProduct(@PathVariable("productId") int productId, HttpServletRequest request) {
        this.productDao.deleteProduct(productId);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(request.getContextPath() + "/");
        return redirectView;

    }
    @RequestMapping("/update/{productId}")
    public String updateForm(@PathVariable("productId") int pid, Model model){
    Product product = this.productDao.getProduct(pid);
    model.addAttribute("product", product);
    return "update_form";
    }

}
