package com.company;

public class Event
{
    int type ;
    double instant  ;
    int idClient ;

    public Event(int type, double instant) {
        this.type = type;
        this.instant = instant;
    }

    public Event(int type, double instant, int idClient) {
        this.type = type;
        this.instant = instant;
        this.idClient = idClient;
    }

    public int getType() {
        return type;
    }

    public double getInstant() {
        return instant;
    }

    public int getIdClient() {
        return idClient;
    }
}
