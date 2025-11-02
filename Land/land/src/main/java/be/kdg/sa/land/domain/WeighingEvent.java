package be.kdg.sa.land.domain;

import java.time.LocalDateTime;

public class WeighingEvent {
    private String kenteken;
    private final double gewicht;
    private final LocalDateTime tijdstip;

    public WeighingEvent(String kenteken, double gewicht, LocalDateTime tijdstip) {
        this.kenteken = kenteken;
        this.gewicht = gewicht;
        this.tijdstip = tijdstip;
    }

    public String getKenteken() {
        return kenteken;
    }

    public void setKenteken(String kenteken) {
        this.kenteken = kenteken;
    }

    public double getGewicht() {
        return gewicht;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }
}
