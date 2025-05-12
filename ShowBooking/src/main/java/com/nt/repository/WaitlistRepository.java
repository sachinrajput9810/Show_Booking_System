package com.nt.repository;

import com.nt.model.WaitlistEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WaitlistRepository {
    private final List<WaitlistEntry> waitlist = new ArrayList<>();

    public void add(WaitlistEntry entry) {
        waitlist.add(entry);
    }

    public WaitlistEntry findFirst(String showName, String slot) {
        return waitlist.stream()
                .filter(entry -> entry.getShowName().equals(showName) && entry.getSlot().equals(slot))
                .findFirst()
                .orElse(null);
    }

    public void remove(WaitlistEntry entry) {
        waitlist.remove(entry);
    }
}
