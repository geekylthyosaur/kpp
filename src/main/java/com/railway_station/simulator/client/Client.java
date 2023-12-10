package com.railway_station.simulator.client;


public interface Client {
    int getId();
    String getName();
    int getDesiredTicketsCount();
    int getPriority();
    String getType();
}

