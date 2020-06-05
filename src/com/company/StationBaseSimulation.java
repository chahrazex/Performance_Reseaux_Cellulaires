package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class StationBaseSimulation extends JFrame
{
    static  double lamda ;
    double mu ;
    ListeEvent listeEvent ;
    double t ;
    int nombreCannaux = 2  ;
    int EtatCannaux[] ;
    Vector<QueueCanals> ListeClientEnService ;
    public  static  double probBlocage = 0;

    public StationBaseSimulation(double lamda, double mu)
    {
        this.lamda = lamda;
        this.mu = mu;
        listeEvent = new ListeEvent() ;
        ListeClientEnService = new Vector<>() ;
        EtatCannaux = new int[nombreCannaux] ;
    }
    public double expo(double taux)
    {
        return -Math.log(Math.random())/taux ;
    }
    public void simulate(double simLenght)
    {
        initUI(simLenght);
    }


    private void initUI(double simLenght) {

        XYDataset dataset = createDataset(simLenght);
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset(double simLenght)
    {
        XYSeries series = new XYSeries("");
        double r = 1;

        /*--------Cacucler le R ----------*/
        while (r<=7)
        {
            System.out.println("************************************R="+r+"***************************************");
            ListeClientEnService.clear();
            listeEvent.events.clear();
            probBlocage = 0 ;
            lamda = 2*3.14*Math.pow(r,2);
            t = 0 ;
            Event s1 = new Event(0,t) ;
            listeEvent.addEvents(s1);
            int numCan = 0;
            int nombreClient = 0;
            int idClient=0 ;
            for (int i =0 ; i <nombreCannaux; i++)
            {
                EtatCannaux[i]=0 ;
            }
            while (nombreClient<10)
            {
                for (int i = 0;i<listeEvent.events.size(); i++)
                {
                    int index = 0 ;
                    Event e = listeEvent.events.elementAt(index) ;
                    listeEvent.events.removeElementAt(index);
                    t = e.getInstant() ;
                    System.out.println("----------------------------------------------------------");
                    System.out.println("temps Actuel ="+t);
                    if (e.getType()==0)//arrivee
                    {
                        nombreClient++ ;
                        numCan =-1 ;
                        Event s2 = new Event(0,t+expo(lamda)) ;
                        listeEvent.addEvents(s2);
                        double duree = expo(mu) ;
                        idClient++ ;
                        Client client = new Client(e.getInstant(),duree,t+duree) ;

                        for (int j =0 ; j<nombreCannaux; j++)
                        {
                            if (EtatCannaux[j]==0)//cannaux nn occupeé
                            {
                                numCan = j ; break;
                            }
                        }
                        if (numCan!= -1)//si il'y une cannal le client va générer sont départ
                        {

                            System.out.println("--------------------------Client N°"+idClient+"----------------------");
                            System.out.println("Temps arrive="+ client.tempsArrivee+"    " +
                                    "   Duree Service = "+client.dureeService+"  Fin Service ="+(t+duree));
                            System.out.println("le Client N° "+idClient+ " Passer au service et utilse le cannaux N° "+numCan);
                            EtatCannaux[numCan] = 1 ;
                            ListeClientEnService.add(new QueueCanals(client,numCan,idClient)) ;
                            //En génere le départ et en palce dans le bone palce   ;
                            Event event = new Event(1,t+duree,idClient) ;
                            listeEvent.addEvents(event);
                        }
                        if (numCan == -1)//l'appael sera réffusé donc probBlocage++
                        {
                            System.out.println("--------------------------Client N°"+idClient+"----------------------");
                            System.out.println("Cette client est  Réffussé");
                            probBlocage++ ;
                            System.out.println("proba blocage = "+probBlocage);
                        }
                    }
                    else  //départ
                    {
                        System.out.println("C'est le départ de Client N°"+e.getIdClient());
                        for (int j = 0 ; j<ListeClientEnService.size() ; j++)
                        {
                            if (ListeClientEnService.elementAt(j).idClient==e.getIdClient())
                            {
                                EtatCannaux[ ListeClientEnService.elementAt(j).numCann] = 0;
                                ListeClientEnService.removeElementAt(j);
                            }
                        }

                    }
                }

            }

            double p = probBlocage/idClient ;
            System.out.println();
            System.out.println("proba de plocage ="+ p);
            series.add(r, p);
            r = r + 0.5 ;
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Probability blocage  per Rayon ",
                "Rayon",
                "Probability de blocage ",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return chart;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            StationBaseSimulation baseSimulation =  new StationBaseSimulation(0.5,1.0) ;
            baseSimulation.simulate(500);
            baseSimulation.setVisible(true);
        });
    }
}