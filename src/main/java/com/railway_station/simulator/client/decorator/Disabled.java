package com.railway_station.simulator.client.decorator;

import com.railway_station.simulator.client.Client;

public class Disabled extends ClientDecorator {
    public Disabled(Client client) {
        super(client);
    }

    @Override
    public int getPriority() {
        return super.getPriority() * 4;
    }

    @Override
    public String getType() {
        return "disabled";
    }
}
