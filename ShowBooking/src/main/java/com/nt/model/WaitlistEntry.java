package com.nt.model;

import lombok.Data;

@Data
public class WaitlistEntry {
    private String user;
    private String showName;
    private String slot;
    private int persons;
}
