package com.railway_station.simulator.client.generator;

import com.google.gson.Gson;
import com.railway_station.simulator.client.Client;
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

    public ClientGenerator(GenerationStrategy strategy) {
        this.currentStrategy = strategy;
        timeCreationInterval = this.currentStrategy.getNextInterval();
    }

    public GenerationStrategy getCurrentStrategy() {
        return currentStrategy;
    }

    public void setCurrentStrategy(GenerationStrategy currentStrategy) {
        this.currentStrategy = currentStrategy;
    }

    public Client generate() {
        ClientGenerator.generationRunning = true;
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeCreationInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stationBuilding.getClientsInsideCount() >= Configuration.getInstance().getMaxClientCount()) {
                continue;
            }
            String clientName = ClientFactory.generateRandomName();
            int desiredTicketsCount = ClientFactory.generateDesiredTicketsCount();

            List<String> specialClientTypes = new ArrayList<>();
            specialClientTypes.add("desabled");
            specialClientTypes.add("military");
            specialClientTypes.add("with_child");

            Random random = new Random();
            int randomIndex = random.nextInt(specialClientTypes.size());
            String clientType = specialClientTypes.get(randomIndex);

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
        }
    }
}
