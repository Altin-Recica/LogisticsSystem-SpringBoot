package be.kdg.sa.land.controller;

import be.kdg.sa.land.service.FIFOQueueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

@Controller
public class FIFOQueueController {
    private final FIFOQueueService fifoQueueService;

    public FIFOQueueController(FIFOQueueService fifoQueueService) {
        this.fifoQueueService = fifoQueueService;
    }

    @GetMapping("/queue/trucks")
    public ModelAndView getTrucks() {
        ModelAndView modelAndView = new ModelAndView("truckQueue");
        try {
            modelAndView.addObject("trucks", fifoQueueService.getAllTrucksInQueue());
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

