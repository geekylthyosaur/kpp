package com.railway_station.simulator.station_building;

import java.util.ArrayList;
import java.util.List;

public class StationBuilding {
    private static StationBuilding instance;
    private List<CashRegister> cashRegisters;
    private int clientsInsideCount;

    private StationBuilding() {
        cashRegisters = new ArrayList<CashRegister>();
    }

    public void createCashRegisters(int count) {
        if (cashRegisters.isEmpty()) {
            for (int i = 0; i < count; i++) {
                CashRegister cashRegister = new CashRegister(i);
                cashRegister.open();
                cashRegisters.add(cashRegister);
            }
        }
    }

    public void addCashRegister(CashRegister cashRegister) {
        cashRegisters.add(cashRegister);
    }

    public boolean removeCashRegister(CashRegister cashRegister) {
        return cashRegisters.remove(cashRegister);
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
}
