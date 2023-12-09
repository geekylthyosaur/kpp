package com.railway_station.simulator.server.payload_models;

import java.util.List;

public class ValueData {

    public List<ValueItems> information;

    public void addInfo(ValueItems item) {
        information.add(item);
    }
}
