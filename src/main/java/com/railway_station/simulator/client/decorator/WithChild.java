package com.railway_station.simulator.client.decorator;

import com.railway_station.simulator.client.Client;

public class WithChild extends ClientDecorator {
    public WithChild(Client client) {
        super(client);
    }

    @Override
    public int getPriority() {
        return super.getPriority() * 2;
    }
}
