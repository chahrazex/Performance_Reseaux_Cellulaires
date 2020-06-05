package com.company;

public class Client
{
    double tempsArrivee ;
    double dureeService ;
    double finService ;

    public Client(double tempsArrivee, double dureeService,double finService)
    {
        this.tempsArrivee = tempsArrivee;
        this.dureeService = dureeService;
        this.finService = finService ;

    }

    public double getTempsArrivee() {
        return tempsArrivee;
    }

    public double getDureeService() {
        return dureeService;
    }

    public double getFinService() {
        return finService;
    }
}
