package com.railway_station.simulator.client.decorator;

import com.railway_station.simulator.client.Client;

public class Military extends ClientDecorator {
    public Military(Client client) {
        super(client);
    }

    @Override
    public int getPriority() {
        return super.getPriority() * 3;
    }
}
