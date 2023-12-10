package com.railway_station.simulator.station_building;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.railway_station.simulator.client.Client;
import com.railway_station.simulator.config.Configuration;
import com.railway_station.simulator.server.ServerRunner;
import com.railway_station.simulator.server.payload_models.ChangeCashRegisterEventPayload;
import com.railway_station.simulator.server.payload_models.ClientObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

public class StationBuilding {
    private static StationBuilding instance;
    private List<CashRegister> cashRegisters;
    private int clientsInsideCount;
    private Thread reservedCashRegisterThread;

    private StationBuilding() {
        cashRegisters = new ArrayList<CashRegister>();
    }

    public void createAndStartCashRegisters(int count) {
        if (cashRegisters.isEmpty()) {
            CashRegister reservedCashRegister = new CashRegister(0);
            cashRegisters.add(reservedCashRegister);
            for (int i = 1; i < count; i++) {
                CashRegister cashRegister = new CashRegister(i);
                cashRegister.open();
                cashRegisters.add(cashRegister);
            }
        }

        Thread reservedCashRegisterSimulator = new Thread(() -> {
            try {
                simulateReservedCashRegister(count);
            } catch (JsonProcessingException e) {
            }
        });
        reservedCashRegisterThread = reservedCashRegisterSimulator;
        reservedCashRegisterSimulator.start();
    }

    public void simulateReservedCashRegister(int cashRegistersCount) throws JsonProcessingException {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(Configuration.getInstance().getMaxClientServingTime() * 15);
            } catch (InterruptedException e) {
                return;
            }
            if (getClientsInsideCount() >= Configuration.getInstance().getMaxClientsCountInsideBuilding()) {
                continue;
            }
            Random rand = new Random();
            int cashRegisterId = rand.nextInt((cashRegistersCount - 1)) + 1;

            changeCashRegister(cashRegisterId, 0);

            try {
                TimeUnit.MILLISECONDS.sleep(Configuration.getInstance().getMaxClientServingTime() * 20);
            } catch (InterruptedException e) {
                return;
            }

            changeCashRegister(0, cashRegisterId);

            try {
                TimeUnit.MILLISECONDS.sleep(Configuration.getInstance().getMaxClientServingTime() * 5);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void changeCashRegister(int closedCashRegisterId, int openedCashRegisterId) throws JsonProcessingException {
        CashRegister closedCashRegister = cashRegisters.get(closedCashRegisterId);
        closedCashRegister.close();

        ConcurrentSkipListMap<Integer, Client> entryClients = closedCashRegister.getClients();
        closedCashRegister.setClients(new ConcurrentSkipListMap<>((a, b) -> b.compareTo(a)));

        ArrayList<ClientObj> clients = new ArrayList<>();
        for (Map.Entry<Integer, Client> entry : entryClients.entrySet()) {
            Client client = entry.getValue();
            ClientObj clientObj = new ClientObj();
            clientObj.clientId = client.getId();
            clientObj.clientName = client.getName();
            clientObj.clientType = client.getType();
            clientObj.desiredTicketsCount = client.getDesiredTicketsCount();
            clients.add(clientObj);
        }

        ChangeCashRegisterEventPayload payload = new ChangeCashRegisterEventPayload();
        payload.closedCashRegisterId = closedCashRegisterId;
        payload.openedCashRegisterId = openedCashRegisterId;
        payload.clients = clients;

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ((com.fasterxml.jackson.databind.ObjectWriter) ow).writeValueAsString(payload);
        ServerRunner server = ServerRunner.getInstance();
        server.socketEmit("change_cash_register", json);

        CashRegister openedCashRegister = cashRegisters.get(openedCashRegisterId);
        openedCashRegister.open();
    }

    public void setCashRegisters(List<CashRegister> cashRegisters) {
        this.cashRegisters = cashRegisters;
    }

    public List<CashRegister> getCashRegisters() {
        return cashRegisters;
    }

    public synchronized int getClientsInsideCount() {
        return clientsInsideCount;
    }

    public synchronized void increaseClientsInsideCount() {
        clientsInsideCount += 1;
    }

    public synchronized void decreaseClientsInsideCount() {
        clientsInsideCount -= 1;
    }

    public static StationBuilding getInstance() {
        if (instance == null) {
            instance = new StationBuilding();
        }
        return instance;
    }

    public CashRegister getCashRegister(int id) {
        return cashRegisters.get(id);
    }

    public Thread getReservedCashRegisterThread() {
        if (reservedCashRegisterThread == null) {
            return new Thread();
        }
        return reservedCashRegisterThread;
    }
}
