package com.railway_station.simulator.server.payload_models;

public class ConfigObject {
    private int cashRegisterCount;
    private int minServingTime;
    private int maxServingTime;
    String generationStrategy;

    public ConfigObject() {
    }

    public ConfigObject(int cashRegisterCount, int minServingTime, int maxServingTime, String generationStrategy) {
        super();
        this.cashRegisterCount = cashRegisterCount;
        this.minServingTime = minServingTime;
        this.maxServingTime = maxServingTime;
        this.generationStrategy = generationStrategy;
    }

    public int getCashRegisterCount() {
        return cashRegisterCount;
    }
    public void setCashRegisterCount(int cashRegisterCount) {
        this.cashRegisterCount = cashRegisterCount;
    }

    public int getMinServingTime() {
        return minServingTime;
    }
    public void setMinServingTime(int minServingTime) {
        this.minServingTime = minServingTime;
    }

    public int getMaxServingTime() {
        return maxServingTime;
    }
    public void setMaxServingTime(int maxServingTime) {
        this.maxServingTime = maxServingTime;
    }

    public String getGenerationStrategy() {
        return generationStrategy;
    }
    public void setGenerationStrategy(String generationStrategy) {
        this.generationStrategy = generationStrategy;
    }
}
