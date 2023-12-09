package com.railway_station.simulator.client.decorator;

import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.ClientState;

public class ClientDecorator implements Client {
    private Client wrapee;

    public ClientDecorator(Client client) {
        this.wrapee = client;
    }

    @Override
    public int getId() {
        return wrapee.getId();
    }

    @Override
    public String getName() {
        return wrapee.getName();
    }

    @Override
    public int getDesiredTicketsCount() {
        return wrapee.getDesiredTicketsCount();
    }

    @Override
    public int getPriority() {
        return wrapee.getPriority();
    }

    @Override
    public ClientState getState() {
        return wrapee.getState();
    }

    @Override
    public String getType() {
        return "";
    }
}
