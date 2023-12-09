package com.railway_station.simulator.server.payload_models;

import java.util.ArrayList;

public class ChangeCashRegisterEventPayload {
    public int closedCashRegisterId;
    public int openedCashRegisterId;
    public ArrayList<ClientObj> clients;
}
