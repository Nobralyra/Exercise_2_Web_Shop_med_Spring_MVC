package web_shop.exercise.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import web_shop.exercise.Domain.Category;
import web_shop.exercise.Domain.Company;
import web_shop.exercise.Domain.CompanyDescription;
import web_shop.exercise.Domain.Product;
import web_shop.exercise.Service.ICrudService;

import javax.validation.Valid;

@Controller
public class ProductController
{
    private final ICrudService<Product, Long> iProductCrudService;
    private final ICrudService<Company, Long> iCompanyCrudService;
    private final ICrudService<CompanyDescription, Long> iCompanyDescriptionCrudService;
    private final ICrudService<Category, Long> iCategoryCrudService;
    
    
    public ProductController(ICrudService<Product, Long> iProductCrudService, ICrudService<Company, Long> iCompanyCrudService, ICrudService<CompanyDescription, Long> iCompanyDescriptionCrudService, ICrudService<Category, Long> iCategoryCrudService)
    {
        this.iProductCrudService = iProductCrudService;
        this.iCompanyCrudService = iCompanyCrudService;
        this.iCompanyDescriptionCrudService = iCompanyDescriptionCrudService;
        this.iCategoryCrudService = iCategoryCrudService;
    }

    @GetMapping({"", "/"})
    public String indexPage(Model model)
    {
        //add all products to model from ICrudService
        model.addAttribute("product", iProductCrudService.findAll());

        if(iCompanyCrudService.findAll().size() == 0)
        {
            return ("/companies/index");
        }
        return ("/products/index");
    }

    @GetMapping("/products/create")
    public String create(Product product, Model model)
    {
        model.addAttribute("category", iCategoryCrudService.findAll());
        model.addAttribute("company", iCompanyCrudService.findAll());
        model.addAttribute("product", product);

        return "/products/create";
    }

    @PostMapping("/products/create")
    public String createProduct(@Valid @ModelAttribute ("product") Product product, BindingResult resultProduct,
                                @Valid @ModelAttribute ("companyDescription") CompanyDescription companyDescription,
                                BindingResult resultCompanyDescription,
                                Model model)
    {
        if(!resultProduct.hasErrors())
        {
            iProductCrudService.save(product);
        }
        else
        {
            model.addAttribute("resultProduct", resultProduct);
            model.addAttribute("resultCompanyDescription", resultCompanyDescription);
            model.addAttribute("product", product);
            model.addAttribute("companyDescription", companyDescription);
            model.addAttribute("category", iCategoryCrudService.findAll());
            model.addAttribute("company", iCompanyCrudService.findAll());

            return "/products/create";
        }
        return "redirect:/";
    }

    //Use @PathVariable to bound id from URL to method parameter
    @GetMapping({"/products/details/{id}"})
    public String detail(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("product", iProductCrudService.findById(id));
        model.addAttribute("category", iCategoryCrudService.findAll());

        return ("/products/details");
    }
    
    @GetMapping("/products/update/{id}")
    public String update(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("product", iProductCrudService.findById(id));
        model.addAttribute("company", iCompanyCrudService.findById(id));
        model.addAttribute("category", iCategoryCrudService.findAll());

        return "/products/update";
    }

    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute @Valid Product product, BindingResult resultProduct, Model model)
    {
        if(!resultProduct.hasErrors())
        {
            iProductCrudService.save(product);
        }
        else
        {
            model.addAttribute("resultProduct", resultProduct);
            model.addAttribute("category", iCategoryCrudService.findAll());

            return "/products/update";
        }
        return "redirect:/";
    }

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable("id") long id)
    {
        iProductCrudService.deleteByID(id);

        return "redirect:/";
    }
}
