package be.kdg.sa.land.controller;

import be.kdg.sa.land.domain.Appointment;
import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.domain.Weighbridge;
import be.kdg.sa.land.domain.WeighingEvent;
import be.kdg.sa.land.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class TruckController {

    private final TruckService truckService;
    private final FIFOQueueService fifoQueueService;
    private final AppointmentService appointmentService;
    private final TerrainService terrainService;
    private final WeighbridgeService weighbridgeService;
    private final WeighingEventService weighingEventService;
    private final ConfigurationService configurationService;

    public TruckController(TruckService truckService, FIFOQueueService fifoQueueService, AppointmentService appointmentService, TerrainService terrainService, WeighbridgeService weighbridgeService, WeighingEventService weighingEventService, ConfigurationService configurationService) {
        this.truckService = truckService;
        this.fifoQueueService = fifoQueueService;
        this.appointmentService = appointmentService;
        this.terrainService = terrainService;
        this.weighbridgeService = weighbridgeService;
        this.weighingEventService = weighingEventService;
        this.configurationService = new ConfigurationService();
    }

    @GetMapping("/trucks")
    public String getTrucks(Model model) {
        try {
            model.addAttribute("trucks", truckService.getTrucks());
            return "trucks";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @GetMapping("/check-plate")
    public String showLicencePlateForm() {
        return "licencePlate";
    }

    @PostMapping("/check-plate")
    public String checkLicencePlate(@RequestParam("licencePlate") String licencePlate, RedirectAttributes redirectAttributes) {
        try {
            String message;
            Optional<Truck> truckOptional = truckService.hasAppointment(licencePlate, true);

            if (truckOptional.isPresent()) {
                Truck truck = truckOptional.get();
                Optional<Appointment> appointment = appointmentService.getAppointment(truck.getLicencePlate());

                LocalDateTime scheduledTime = appointment.get().getScheduledTime();
                LocalDateTime arrivalTime = truck.getArrivalTime();
                LocalDateTime scheduledTimePlusHour = scheduledTime.plusHours(configurationService.getArrivalWindow());

                if (arrivalTime.isBefore(scheduledTime) || arrivalTime.isAfter(scheduledTimePlusHour)) {
                    message = "The license plate is found, but your arrival time falls outside the scheduled time window. Access denied. You are placed in the queue.";
                    fifoQueueService.addTruckToQueue(truck);
                } else {
                    if (!weighbridgeService.getAvailableWeighbridges().isEmpty()) {
                        weighbridgeService.assignWeighNumber(truck.getTruckID());
                        Weighbridge weighbridge = weighbridgeService.getWeighbridgeByTruck(truck);
                        weighingEventService.sendWeighingEvent(new WeighingEvent(truck.getLicencePlate(), weighbridge.getWeighbridgeID(), LocalDateTime.now()));

                        message = "License plate found. The gate is now opening. You may proceed directly to weighbridge " + weighbridge.getWeighbridgeID();
                    } else {
                        message = "License plate found. The gate is now opening. There are no weighbridges available, so you must wait in the terrain.";
                        terrainService.addTruckToTerrain(truck);
                    }
                }
            } else {
                Optional<Truck> truckWithoutAppointment = truckService.hasAppointment(licencePlate, false);
                if (truckWithoutAppointment.isPresent()) {
                    Truck truck = truckWithoutAppointment.get();
                    fifoQueueService.addTruckToQueue(truck);
                    message = "License plate found but no appointment found! You are placed in the queue.";
                } else {
                    message = "License plate not found. Access denied.";
                }
            }

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/gate";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }



    @GetMapping("/gate")
    public String showResultPage() {
        return "gate";
    }

    @GetMapping("/create-truck")
    public String showCreateTruckForm(Model model) {
        try {
            model.addAttribute("truck", new CreateTruckRequest("", 0, 0, 0, null, false));
            return "truckForm";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @PostMapping("/trucks")
    public String createTruck(@Valid @ModelAttribute CreateTruckRequest request, RedirectAttributes redirectAttributes) {
        try {
            Truck truck = new Truck(
                    request.licencePlate(),
                    request.capacity(),
                    request.currentWeight(),
                    request.tareWeight(),
                    request.arrivalTime(),
                    false,
                    null
            );

            truckService.saveTruck(truck);
            redirectAttributes.addFlashAttribute("message", "Truck created successfully!");
            return "redirect:/trucks";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }
}
