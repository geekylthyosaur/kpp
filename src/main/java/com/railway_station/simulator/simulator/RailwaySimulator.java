package com.railway_station.simulator.simulator;

import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.client.ClientType;
import com.railway_station.simulator.client.CommonClient;
import com.railway_station.simulator.client.decorator.Disabled;
import com.railway_station.simulator.client.decorator.Military;
import com.railway_station.simulator.client.decorator.WithChild;
import com.railway_station.simulator.client.generation_strategy.GenerationStrategy;
import com.railway_station.simulator.client.generator.ClientGenerator;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.station_building.CashRegister;
import com.railway_station.simulator.station_building.StationBuilding;

import java.util.ArrayList;
import java.util.List;

public class RailwaySimulator {
    private Thread clientGenerationThread;

    public RailwaySimulator(int cashRegistersCount, int minServingTime, int maxServingTime, String generationStrategy, int maxClientsCountInsideBuilding) {
        Configuration config = Configuration.getInstance();
        config.setCashRegistersCount(cashRegistersCount);
        config.setMinClientServingTime(minServingTime);
        config.setMaxClientServingTime(maxServingTime);
        config.setClientGenerationStrategy(generationStrategy);
        config.setMaxClientsCountInsideBuilding(maxClientsCountInsideBuilding);
    }


    public void startSimulation() {
        Configuration config = Configuration.getInstance();
        StationBuilding stationBuilding = StationBuilding.getInstance();

        int cashRegistersCount = config.getCashRegistersCount();
        GenerationStrategy clientGenerationStrategy = config.getClientGenerationStrategy();
        ClientGenerator clientGenerator = new ClientGenerator(clientGenerationStrategy);

        stationBuilding.createAndStartCashRegisters(cashRegistersCount);
        this.clientGenerationThread = new Thread(clientGenerator::generate);
        clientGenerationThread.start();
    }

    public void stopSimulation() {
        StationBuilding stationBuilding = StationBuilding.getInstance();
        Thread reservecCashRegisterThread = stationBuilding.getReservedCashRegisterThread();
        if (reservecCashRegisterThread != null) {
            reservecCashRegisterThread.interrupt();
        }
        List<CashRegister> cashRegisters = stationBuilding.getCashRegisters();
        for (var cashRegister: cashRegisters) {
            cashRegister.close();
        }
        stationBuilding.setCashRegisters(new ArrayList<CashRegister>());
        stationBuilding.setClientsInsideCount(0);
        if (clientGenerationThread != null) {
            clientGenerationThread.interrupt();
        }
    }

    public Client createClientWithAlteredType(int clientId, String clientName, int desiredTicketsCount, String clientType) {
        Client client = new CommonClient(clientId, clientName, desiredTicketsCount);
        if (clientType == ClientType.disabled.name()) {
            client = new Disabled(client);
        }
        if (clientType == ClientType.military.name()) {
            client = new Military(client);
        }
        if (clientType == ClientType.with_child.name()) {
            client = new WithChild(client);
        }
        return client;
    }
}
