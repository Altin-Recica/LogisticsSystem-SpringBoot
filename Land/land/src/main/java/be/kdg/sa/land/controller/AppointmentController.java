package be.kdg.sa.land.controller;

import be.kdg.sa.land.controller.api.RestReciever;
import be.kdg.sa.land.domain.Appointment;
import be.kdg.sa.land.service.AppointmentService;
import be.kdg.sa.land.service.ConfigurationService;
import be.kdg.sa.land.service.TruckService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final RestReciever restReciever;
    private final AppointmentService appointmentService;
    private final TruckService truckService;
    private final ConfigurationService configurationService;

    public AppointmentController(AppointmentService appointmentService, TruckService truckService, RestReciever restReciever, ConfigurationService configurationService) {
        this.appointmentService = appointmentService;
        this.truckService = truckService;
        this.restReciever = restReciever;
        this.configurationService = configurationService;
    }

    @GetMapping
    public String getAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAppointments());
        return "appointments";
    }

    @GetMapping("/create")
    public String showForm(Model model) {
        try {
            model.addAttribute("createAppointmentRequest", new CreateAppointmentRequest(null, "", "", 1));
            model.addAttribute("trucks", truckService.getTrucksWithoutAppointment());
            model.addAttribute("clients", restReciever.getClients());
            model.addAttribute("materials", restReciever.getMaterials());
            return "appointmentForm";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute CreateAppointmentRequest createAppointmentRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "appointmentForm";
        }

        if (!restReciever.checkCapacity(createAppointmentRequest.material())) {
            redirectAttributes.addFlashAttribute("error", "Magazijn is vol!");
            return "redirect:/appointments/create";
        }

        try {
            LocalDateTime plannedTime = createAppointmentRequest.plannedTime();
            LocalTime startHour = configurationService.getAppointmentFrom();
            LocalTime lastAppointmentHour = configurationService.getAppointmentUntil();
            LocalTime lastAllowedHour = configurationService.getLastAppointmentAllowed();
            int maxAppointmentsPerHour = configurationService.getMaxAppointmentsPerHour();


            if (plannedTime.toLocalTime().isBefore(startHour) || plannedTime.toLocalTime().isAfter(lastAppointmentHour)) {
                redirectAttributes.addFlashAttribute("error", "Appointments can only be scheduled between 8:00 and 20:00.");
                return "redirect:/appointments/create";
            }else if (plannedTime.toLocalTime().isAfter(lastAllowedHour)) {
                redirectAttributes.addFlashAttribute("error", "After 19:00 it is too late to schedule an appointment.");
                return "redirect:/appointments/create";
            }

            long appointmentsThisHour = appointmentService.countAppointmentsInHour(plannedTime);
            if (appointmentsThisHour >= maxAppointmentsPerHour) {
                redirectAttributes.addFlashAttribute("error", "Maximum number of appointments per hour reached. Please choose a different time.");
                return "redirect:/appointments/create";
            }

            Optional<Appointment> optionalAppointment = appointmentService.create(
                    createAppointmentRequest.plannedTime(),
                    createAppointmentRequest.licencePlate(),
                    createAppointmentRequest.material(),
                    createAppointmentRequest.clientId()
            );

            if (optionalAppointment.isPresent()) {
                redirectAttributes.addFlashAttribute("message", "Appointment successfully created!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Truck already has an appointment or does not exist!");
            }
            return "redirect:/appointments";
        } catch (NotFoundException e) {
            return "error/404";
        } catch (Exception e) {
            return "error/500";
        }
    }
}
