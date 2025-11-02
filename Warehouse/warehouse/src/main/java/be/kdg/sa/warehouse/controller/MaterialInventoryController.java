package be.kdg.sa.warehouse.controller;

import be.kdg.sa.warehouse.service.MaterialInventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

@Controller
public class MaterialInventoryController {
    private final MaterialInventoryService materialInventoryService;

    public MaterialInventoryController(MaterialInventoryService materialInventoryService) {
        this.materialInventoryService = materialInventoryService;
    }

    @GetMapping("/inventory")
    public ModelAndView getMaterialsInInventory() {
        ModelAndView modelAndView = new ModelAndView("materialinventory");
        try {
            modelAndView.addObject("materialItemsInventory", materialInventoryService.getMaterialsInInventory());
        } catch (NotFoundException e) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("errorMessage", "Not found.");
        } catch (Exception e) {
            modelAndView.setViewName("error/500");
            modelAndView.addObject("errorMessage", "Unable to retrieve materials information.");
        }
        return modelAndView;
    }

    @GetMapping("/inventory/oldestFirst")
    public ModelAndView getMaterialsInInventoryOldestFirst() {
        ModelAndView modelAndView = new ModelAndView("materialinventory");
        try {
            modelAndView.addObject("materialItemsInventory", materialInventoryService.getOldestMaterialInventories());
        } catch (NotFoundException e) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("errorMessage", "Not found.");
        } catch (Exception e) {
            modelAndView.setViewName("error/500");
            modelAndView.addObject("errorMessage", "Unable to retrieve materials information.");
        }
        return modelAndView;
    }

    @GetMapping("/inventory/{warehouseNumber}")
    public ModelAndView getMaterialInventoryFromWarehouse(@PathVariable int warehouseNumber) {
        ModelAndView modelAndView = new ModelAndView("materialinventoryfromwarehouse");
        try {
            modelAndView.addObject("materialItemsInventory", materialInventoryService.getMaterialInventoriesForWarehouse(warehouseNumber));
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
