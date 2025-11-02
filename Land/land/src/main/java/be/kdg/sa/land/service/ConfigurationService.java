package be.kdg.sa.land.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

@Service
public class ConfigurationService {
    @Value("${kdg.appointment.from}")
    private LocalTime appointmentFrom;
    @Value("${kdg.appointment.until}")
    private LocalTime appointmentUntil;
    @Value("${kdg.last.appointment.allowed}")
    private LocalTime lastAppointmentAllowed;
    @Value("${kdg.max.appointments.per.hour}")
    private int maxAppointmentsPerHour;
    @Value("${kdg.arrival.window}")
    private int arrivalWindow;

    public LocalTime getAppointmentFrom() {
        return appointmentFrom;
    }

    public LocalTime getAppointmentUntil() {
        return appointmentUntil;
    }

    public LocalTime getLastAppointmentAllowed() {
        return lastAppointmentAllowed;
    }

    public int getMaxAppointmentsPerHour() {
        return maxAppointmentsPerHour;
    }

    public int getArrivalWindow() {
        return arrivalWindow;
    }
}
