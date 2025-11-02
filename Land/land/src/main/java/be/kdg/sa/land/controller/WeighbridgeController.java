package be.kdg.sa.land.controller;

import be.kdg.sa.land.service.WeighbridgeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

@Controller
public class WeighbridgeController {

    private final WeighbridgeService weighbridgeService;

    public WeighbridgeController(WeighbridgeService weighbridgeService) {
        this.weighbridgeService = weighbridgeService;
    }

    @GetMapping("/weighbridges")
    public ModelAndView getWeighbridges() {
        ModelAndView modelAndView = new ModelAndView("weighbridges");
        try {
            modelAndView.addObject("weighbridges", weighbridgeService.getWeighbridges());
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
