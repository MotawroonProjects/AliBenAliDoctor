package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class SingleReservationDataModel extends StatusResponse implements Serializable {
    private ReservationModel data;

    public ReservationModel getData() {
        return data;
    }
}
