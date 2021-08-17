package com.alibenalidoctor.models;

import java.io.Serializable;
import java.util.List;

public class DateDataModel extends StatusResponse implements Serializable {
    private List<DateModel> data;

    public List<DateModel> getData() {
        return data;
    }
}
