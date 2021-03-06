package web_shop.exercise.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import web_shop.exercise.Domain.Category;
import web_shop.exercise.Domain.Product;
import web_shop.exercise.Service.ICrudService;

import javax.validation.Valid;

@Controller
public class CategoryController
{
    private final ICrudService<Category, Long> iCategoryCrudService;
    private final ICrudService<Product, Long> iProductCrudService;

    public CategoryController(ICrudService<Category, Long> iCategoryCrudService, ICrudService<Product, Long> iProductCrudService)
    {
        this.iCategoryCrudService = iCategoryCrudService;
        this.iProductCrudService = iProductCrudService;
    }

    @GetMapping({"/categories"})
    public String CategoryPage(Model model)
    {
        model.addAttribute("category", iCategoryCrudService.findAll());

        return ("/categories/index");
    }

    @GetMapping("/categories/create")
    public String Create(Category category, Model model)
    {
        model.addAttribute("category", category);
        return "/categories/create";
    }

    @PostMapping("/categories/create")
    public String CreateCompany(@ModelAttribute @Valid Category category, BindingResult resultCategory, Model model)
    {
        if(!resultCategory.hasErrors())
        {
            iCategoryCrudService.save(category);
        }
        else
        {
            model.addAttribute("resultCategory", resultCategory);
            return "/categories/create";
        }
        return "redirect:/categories";
    }

    //Use @PathVariable to bound id from URL to method parameter
    @GetMapping({"/categories/details/{id}"})
    public String Detail(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("category", iCategoryCrudService.findById(id));
        model.addAttribute("product", iProductCrudService.findAll());
        return ("/categories/details");
    }

    @GetMapping("/categories/update/{id}")
    public String Update(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("category", iCategoryCrudService.findById(id));
        model.addAttribute("product", iProductCrudService.findAll());
        return "/categories/update";
    }

    @PostMapping("/categories/update")
    public String Update(@ModelAttribute @Valid Category category, BindingResult resultCategory, Model model)
    {
        if(!resultCategory.hasErrors())
        {
            iCategoryCrudService.save(category);
        }
        else
        {
            model.addAttribute("resultCategory", resultCategory);
            return "/categories/update";
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String delete(@PathVariable("id") long id)
    {
        iCategoryCrudService.deleteByID(id);
        return "redirect:/categories";
    }
}
