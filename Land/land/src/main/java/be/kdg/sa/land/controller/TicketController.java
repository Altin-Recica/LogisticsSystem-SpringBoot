package be.kdg.sa.land.controller;

import be.kdg.sa.land.controller.api.RestSender;
import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.domain.WeighbridgeTicket;
import be.kdg.sa.land.handlers.WeighingMessageHandler;
import be.kdg.sa.land.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import java.util.Optional;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final RestSender restSender;
    private final TruckService truckService;
    private final WeighbridgeService weighbridgeService;
    private final WeighingMessageHandler weighingMessageHandler;

    public TicketController(TicketService ticketService, RestSender restSender, TruckService truckService, WeighbridgeService weighbridgeService, WeighingMessageHandler weighingMessageHandler) {
        this.ticketService = ticketService;
        this.restSender = restSender;
        this.truckService = truckService;
        this.weighbridgeService = weighbridgeService;
        this.weighingMessageHandler = weighingMessageHandler;
    }

    @GetMapping("/list")
    public ModelAndView getTickets() {
        ModelAndView modelAndView = new ModelAndView("tickets");
        try {
            modelAndView.addObject("tickets", ticketService.getAllTickets());
        } catch (NotFoundException e) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("errorMessage", "Not found.");
        } catch (Exception e) {
            modelAndView.setViewName("error/500");
            modelAndView.addObject("errorMessage", "Unable to retrieve materials information.");
        }
        return modelAndView;
    }

    @GetMapping("/create")
    public String showForm(Model model) {
        try {
            model.addAttribute("createTicketRequest", new CreateTicketRequest("", 0.0, 0.0, "", 999));
            model.addAttribute("trucks", weighbridgeService.getTrucksByWeighbridge());

            return "ticketForm";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @PostMapping("/create")
    public String createTicket(@Valid @ModelAttribute CreateTicketRequest createTicketRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "ticketForm";
        }
        try {
            Optional<WeighbridgeTicket> optionalWeighbridgeTicket = ticketService.createTicket(
                    createTicketRequest.licencePlate(),
                    createTicketRequest.teraWeight(),
                    createTicketRequest.grossWeight(),
                    createTicketRequest.type(),
                    createTicketRequest.clientId()
            );

            if (optionalWeighbridgeTicket.isPresent()) {
                WeighbridgeTicket ticket = optionalWeighbridgeTicket.get();
                Truck truck = truckService.getTruck(ticket.getLicencePlate());
                weighbridgeService.removeTruckFromWeighbridge(truck);
                restSender.sendTicket(ticket.getClientId(), ticket.getMaterialType(), weighingMessageHandler.getLicencePlate(), weighingMessageHandler.getWeight(), weighingMessageHandler.getTimestamp());

                redirectAttributes.addFlashAttribute("message", "Ticket successfully created!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Ticket could not be created!");
            }
            return "redirect:/ticket/list";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }
}

