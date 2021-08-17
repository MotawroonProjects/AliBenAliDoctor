package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class PatinetDataModel extends StatusResponse implements Serializable {
    private List<PatientModel> data;

    public List<PatientModel> getData() {
        return data;
    }
}
