package com.nt.model;

import lombok.Data;

@Data
public class Booking {
    private String bookingId;
    private String user;
    private String showName;
    private String slot;
    private int persons;
}
