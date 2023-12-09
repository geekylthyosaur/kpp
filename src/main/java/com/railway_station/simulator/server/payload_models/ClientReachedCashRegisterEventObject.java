package com.railway_station.simulator.server.payload_models;

public class ClientReachedCashRegisterEventObject{
    public int clientId;
    public int cashRegisterId;
    public String clientName;
    public int desiredTicketsCount;
    public String clientType;
}
