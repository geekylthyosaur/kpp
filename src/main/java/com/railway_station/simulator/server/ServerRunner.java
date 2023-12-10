package com.railway_station.simulator.server;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.server.payload_models.ClientReachedCashRegisterEventObject;
import com.railway_station.simulator.server.payload_models.ConfigPayload;
import com.railway_station.simulator.server.payload_models.StopGeneration;
import com.railway_station.simulator.simulator.RailwaySimulator;
import com.railway_station.simulator.station_building.CashRegister;
import com.railway_station.simulator.station_building.StationBuilding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerRunner implements CommandLineRunner {

    private static ServerRunner instance;
    private final SocketIOServer server;
    private RailwaySimulator simulator;

    @Autowired
    public ServerRunner(SocketIOServer server) {
        this.server = server;
        ServerRunner.instance = this;
        this.server.addConnectListener(onConnected());
        this.server.addDisconnectListener(onDisconnected());
        this.server.addEventListener("configuration", ConfigPayload.class, onConfiguration());
        this.server.addEventListener("client_reached_cash_register", ClientReachedCashRegisterEventObject.class, onClientReachedCashRegister());
        this.server.addEventListener("stop_generation", StopGeneration.class, onStopGeneration());
    }

    private ConnectListener onConnected() {
        return client -> {
            System.out.println("Connected");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            System.out.println("Disconnected");
            if (simulator != null) {
                simulator.stopSimulation();
            }
        };
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }

    // handler for "configuration" event
    private DataListener<ConfigPayload> onConfiguration() {
        return (client, configPayload, ackSender) -> {
            if (simulator != null) {
                simulator.stopSimulation();
            }

            int cashRegistersCount = configPayload.cashRegisterCount;
            int minServingTime = configPayload.minServingTime;
            int maxServingTime = configPayload.maxServingTime;
            String generationStrategy = configPayload.generationStrategy;
            int maxClientsCountInsideBuilding = configPayload.maxClientsInsideBuilding;

            simulator = new RailwaySimulator(cashRegistersCount, minServingTime, maxServingTime, generationStrategy, maxClientsCountInsideBuilding);
            simulator.startSimulation();
        };
    }

    // handler for "client_reached_cash_register" event
    private DataListener<ClientReachedCashRegisterEventObject> onClientReachedCashRegister() {
        return (client, eventPayload, ackSender) -> {
            int cashRegisterId = eventPayload.cashRegisterId;
            int clientId = eventPayload.clientId;
            String clientName = eventPayload.clientName;
            int desiredTicketsCount = eventPayload.desiredTicketsCount;
            String clientType = eventPayload.clientType;

            Client stationClient = simulator.createClientWithAlteredType(clientId, clientName, desiredTicketsCount, clientType);

            CashRegister cashRegister = StationBuilding.getInstance().getCashRegister(cashRegisterId);
            cashRegister.addClient(stationClient);
        };
    }

    // handler for "stop_generation" event
    private DataListener<StopGeneration> onStopGeneration() {
        return (client, eventPayload, ackSender) -> {
            if (simulator != null) {
                simulator.stopSimulation();
            }
        };
    }

    public void socketEmit(String eventName, String jsonifiedData) {
        server.getBroadcastOperations().sendEvent(eventName, jsonifiedData);
    }

    public static ServerRunner getInstance() {
        return instance;
    }

}

