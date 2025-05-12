package com.nt.repository;

import com.nt.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public void save(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> findAll() {
        return bookings;
    }

    public void delete(Booking booking) {
        bookings.remove(booking);
    }
}
