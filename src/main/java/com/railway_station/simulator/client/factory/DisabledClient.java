package com.railway_station.simulator.client.factory;

import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.CommonClient;
import com.railway_station.simulator.client.decorator.Disabled;

public class DisabledClient extends ClientFactory {
    public Client createClient(int id) {
        Client client = new CommonClient(id, generateRandomName(), generateDesiredTicketsCount());
        return new Disabled(client);
    }
}
