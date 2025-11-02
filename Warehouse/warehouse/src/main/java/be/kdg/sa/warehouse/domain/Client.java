package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    private long clientID;
    private String name;
    private String email;


    public Client() {
    }

    public Client(long clientID, String name, String email) {
        this.clientID = clientID;
        this.name = name;
        this.email = email;
    }

    public void setClientID(long clientID) {
        this.clientID = clientID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getClientID() {
        return clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
}
