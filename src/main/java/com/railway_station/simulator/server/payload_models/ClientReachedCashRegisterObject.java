package com.railway_station.simulator.server.payload_models;

public class ClientReachedCashRegisterObject {
    private int clientId;
    private int cashRegisterId;
    private String clientName;
    private int desiredTicketsCount;
    private String clientType;

    public ClientReachedCashRegisterObject() {
    }

    public ClientReachedCashRegisterObject(int clientId, int cashRegisterId, String clientName, int desiredTicketsCount, String clientType) {
        super();
        this.clientId = clientId;
        this.cashRegisterId = cashRegisterId;
        this.clientName = clientName;
        this.desiredTicketsCount = desiredTicketsCount;
        this.clientType = clientType;
    }

    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCashRegisterId() {
        return cashRegisterId;
    }
    public void setCashRegisterId(int cashRegisterId) {
        this.cashRegisterId = cashRegisterId;
    }

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = ClientReachedCashRegisterObject.this.clientName;
    }

    public int getDesiredTicketsCount() {
        return desiredTicketsCount;
    }
    public void setDesiredTicketsCount(int desiredTicketsCount) {
        this.desiredTicketsCount = desiredTicketsCount;
    }

    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
