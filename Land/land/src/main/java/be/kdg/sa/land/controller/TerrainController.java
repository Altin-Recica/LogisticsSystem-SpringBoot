package be.kdg.sa.land.controller;

import be.kdg.sa.land.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

@Controller
public class TerrainController {
    private final FIFOQueueService fifoQueueService;
    private final TerrainService terrainService;

    public TerrainController(FIFOQueueService fifoQueueService, TerrainService terrainService) {
        this.fifoQueueService = fifoQueueService;
        this.terrainService = terrainService;
    }

    @GetMapping("/terrain/trucks")
    public ModelAndView getTrucksInTerrain() {
        ModelAndView modelAndView = new ModelAndView("terrain");
        try {
            modelAndView.addObject("trucks", terrainService.getAllTrucksInTerrain());
            return modelAndView;
        } catch (NotFoundException e) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("errorMessage", "Not found.");
        } catch (Exception e) {
            modelAndView.setViewName("error/500");
            modelAndView.addObject("errorMessage", "Unable to retrieve materials information.");
        }
        return modelAndView;
    }

    @GetMapping("/trucks/overview")
    public String getTrucksOverview(Model model) {
        try {
            model.addAttribute("trucksInQueue", fifoQueueService.getAllTrucksInQueue());
            model.addAttribute("trucksInTerrain", terrainService.getAllTrucksInTerrain());
            return "truckOverview";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }
}
