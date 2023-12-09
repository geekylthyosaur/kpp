package com.railway_station.simulator.client;


public interface Client {
    int getId();
    String getName();
    int getDesiredTicketsCount();
    int getPriority();
    ClientState getState();
    String getType();

}

