package com.railway_station.simulator.client.generator;

import com.google.gson.Gson;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.ClientType;
import com.railway_station.simulator.client.factory.ClientFactory;
import com.railway_station.simulator.client.generation_strategy.GenerationStrategy;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.server.ServerRunner;
import com.railway_station.simulator.station_building.StationBuilding;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientGenerator {
    private GenerationStrategy currentStrategy;
    long timeCreationInterval;
    public static boolean generationRunning = false;
    private StationBuilding stationBuilding = StationBuilding.getInstance();
    private int currentMaxClientId = -1;
    private boolean maxClientsCountReached = false;

    public ClientGenerator(GenerationStrategy strategy) {
        this.currentStrategy = strategy;
        timeCreationInterval = this.currentStrategy.getNextInterval();
    }

    public Client generate() {
        ClientGenerator.generationRunning = true;
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeCreationInterval);
            } catch (InterruptedException e) {
                return null;
            }
            if (stationBuilding.getClientsInsideCount() >= Configuration.getInstance().getMaxClientsCountInsideBuilding()) {
                maxClientsCountReached = true;
                continue;
            } else if (maxClientsCountReached && (stationBuilding.getClientsInsideCount() >= (Configuration.getInstance().getMaxClientsCountInsideBuilding() * 0.7))) {
                continue;
            } else {
                maxClientsCountReached = false;
            }
            String clientName = ClientFactory.generateRandomName();
            int desiredTicketsCount = ClientFactory.generateDesiredTicketsCount();
            String clientType = generateRandomClientType();

            Map<String, Object> clientData = new HashMap<>();
            currentMaxClientId++;
            clientData.put("clientId", currentMaxClientId);
            clientData.put("clientName", clientName);
            clientData.put("desiredTicketsCount", desiredTicketsCount);
            clientData.put("clientType", clientType);

            Gson gson = new Gson();
            String json = gson.toJson(clientData);

            ServerRunner server = ServerRunner.getInstance();
            server.socketEmit("client_created", json);
            StationBuilding.getInstance().increaseClientsInsideCount();
        }
    }

    private String generateRandomClientType() {
        List<ClientType> clientTypes = new ArrayList<>(){};
        clientTypes.add(ClientType.common);
        clientTypes.add(ClientType.disabled);
        clientTypes.add(ClientType.military);
        clientTypes.add(ClientType.with_child);

        Random random = new Random();
        int randomIndex = random.nextInt(clientTypes.size());
        return clientTypes.get(randomIndex).name();
    }
}
