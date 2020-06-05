package com.company;

import java.util.Vector;

public class ListeEvent
{
    Vector<Event> events ;

    public ListeEvent()
    {
        events = new Vector<>();
    }
    public void addEvents(Event newEvent)
    {
        int insertIndex = 0;
        while (insertIndex < events.size())
        {
            Event e = events.elementAt(insertIndex) ;
            if (e.getInstant() > newEvent.getInstant()) break;
            insertIndex++ ;
        }
        events.add(insertIndex,newEvent);
    }
}
