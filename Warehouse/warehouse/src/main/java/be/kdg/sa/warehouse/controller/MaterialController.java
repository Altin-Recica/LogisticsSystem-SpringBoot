package be.kdg.sa.warehouse.controller;

import be.kdg.sa.warehouse.service.MaterialService;
import be.kdg.sa.warehouse.service.ConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

@Controller
public class MaterialController {
    private final MaterialService materialService;
    private final ConfigurationService configService;

    public MaterialController(MaterialService materialService, ConfigurationService configService) {
        this.materialService = materialService;
        this.configService = configService;
    }

    @GetMapping("/materials/{name}")
    public String getMaterial(@PathVariable String name, Model model) {
        try {
            model.addAttribute("material", materialService.getMaterial(name));
            model.addAttribute("storageCost", configService.getStorageCost(name));
            model.addAttribute("sellingPrice", configService.getSellingPrice(name));
            return "material";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @GetMapping("/materials")
    public ModelAndView getMaterials() {
        ModelAndView modelAndView = new ModelAndView("materials");
        try {
            modelAndView.addObject("materials", materialService.getMaterials());
            modelAndView.addObject("materialsStorageCosts", configService.getStorageCosts());
            modelAndView.addObject("materialsSellingPrices", configService.getSellingPrices());
        } catch (NotFoundException e) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("errorMessage", "Not found.");
        } catch (Exception e) {
            modelAndView.setViewName("error/500");
            modelAndView.addObject("errorMessage", "Unable to retrieve materials information.");
        }
        return modelAndView;
    }

}