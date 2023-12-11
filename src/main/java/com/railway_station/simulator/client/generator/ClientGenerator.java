package com.railway_station.simulator.client.generator;

import com.google.gson.Gson;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.ClientType;
import com.railway_station.simulator.client.generation_strategy.GenerationStrategy;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.server.ServerRunner;
import com.railway_station.simulator.station_building.StationBuilding;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientGenerator {
    private static final String[] firstNames = {"Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry", "Ivy", "Jack", "Katherine", "Leo", "Mia", "Nathan", "Olivia"};
    private static final String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris"};
    private static final int minTicketsCount = 1;
    private static final int maxTicketsCount = 3;

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
            String clientName = generateRandomName();
            int desiredTicketsCount = generateDesiredTicketsCount();
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

    public static String generateRandomName() {
        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        return firstName + " " + lastName;
    }

    public static int generateDesiredTicketsCount() {
        Random random = new Random();
        return random.nextInt(maxTicketsCount - minTicketsCount + 1) + minTicketsCount;
    }
}
