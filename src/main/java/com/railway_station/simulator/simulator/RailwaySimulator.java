package com.railway_station.simulator.simulator;

import com.railway_station.simulator.client.generator.ClientGenerator;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.station_building.StationBuilding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RailwaySimulator {
    private static RailwaySimulator instance;
    private Configuration configuration;
    private StationBuilding building;
    private ClientGenerator clientGenerator;

    private boolean isSimulationRunning;
    Thread runningThread;

    private RailwaySimulator() {}

    public static RailwaySimulator getInstance() {
        if (instance == null) {
            instance = new RailwaySimulator();
        }
        return instance;
    }

    public void startSimulation() {
        int entiesCount = configuration.getEntryCount();
        ExecutorService executorService = Executors.newFixedThreadPool(entiesCount);
        for (int i = 0; i < entiesCount; i++) {
            executorService.submit(() -> {

                // Your task logic here
            });
        }

        Runnable runnable = () -> {

        };
        runningThread = new Thread(runnable);
        runningThread.start();
        isSimulationRunning = true;
        // get config values from client
    }

    public void stopSimulation() {
        runningThread.interrupt();
        isSimulationRunning = false;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public StationBuilding getStationBuilding() {
        return building;
    }

    public void setStationBuilding(StationBuilding stationBuilding) {
        this.building = stationBuilding;
    }
}
