package com.nt.service;

import com.nt.exception.BookingException;
import com.nt.exception.ShowNotFoundException;
import com.nt.model.Booking;
import com.nt.model.Show;
import com.nt.model.WaitlistEntry;
import com.nt.repository.BookingRepository;
import com.nt.repository.ShowRepository;
import com.nt.repository.WaitlistRepository;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WaitlistRepository waitlistRepository;

    public String registerShow(String name, String genre) {
    	System.out.println("ShowService.registerShow()");
        Show show = new Show();
        show.setName(name);
        show.setGenre(genre);
        showRepository.save(show);
        return name + " show is registered !!"  ;
    }

    public String onboardShowSlots(String name, Map<String, Integer> slots) {
        System.out.println("ShowService.onboardShowSlots()");
        Show show = showRepository.findByName(name);
        if (show == null) {
            throw new ShowNotFoundException("Show not found!");
        }

        for (String slot : slots.keySet()) {
            if (!slot.matches("^\\d{1,2}:\\d{2}-\\d{1,2}:\\d{2}$")) {
                return "Invalid slot format: " + slot;
            }

            String[] times = slot.split("-");
            String[] start = times[0].split(":");
            String[] end = times[1].split(":");
            int startHour = Integer.parseInt(start[0]);
            int startMinute = Integer.parseInt(start[1]);
            int endHour = Integer.parseInt(end[0]);
            int endMinute = Integer.parseInt(end[1]);

            int durationInMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
            if (durationInMinutes != 60) {
                return "Sorry! Show timings should be exactly 1 hour.";
            }
        }

        show.getSlots().putAll(slots);
        showRepository.save(show);
        System.out.println(showRepository.findAll());
        return "Slots onboarded successfully!";
    }

    
    public Map<String, Map<String, Integer>> showAvailabilityByGenre(String genre) {
        System.out.println("ShowService.showAvailabilityByGenre()");
        System.out.println("Fetching shows for genre: " + genre);
        System.out.println("Current repository state: " + showRepository.findAll());

        Map<String, Show> allShows = showRepository.findAll();
        Map<String, Map<String, Integer>> availabilityMap = new HashMap<>();

        for (String key : allShows.keySet()) {
            Show show = allShows.get(key);
            String genreValue = "\"" + genre + "\"";
            if (genreValue.equals(show.getGenre())) {
                System.out.println("Show name :: " + key);
                System.out.println("Show slots :: " + show.getSlots());

                Map<String, Integer> filteredSlots = new HashMap<>();
                for (String slotKey : show.getSlots().keySet()) {
                    if (show.getSlots().get(slotKey) > 0) {
                        filteredSlots.put(slotKey, show.getSlots().get(slotKey));
                    }
                }

                if (!filteredSlots.isEmpty()) {
                    availabilityMap.put(key, filteredSlots);
                }
            }
        }

        return availabilityMap;
    }





    public String bookTicket(String user, String showName, String slot, int persons) {
    	System.out.println("ShowService.bookTicket()");
    	Show show = showRepository.findByName(showName) ;
    	slot = slot.replace("\"", ""); 

	    if (!slot.matches("^\\d{2}:\\d{2}$")) {
	        throw new IllegalArgumentException("Invalid slot format: " + slot);
	    }

	    String[] timeParts = slot.split(":");
	    int startHour = Integer.parseInt(timeParts[0]); 
	    int endHour = startHour+1 ;
	    String formattedSlot = startHour + ":00-" + endHour + ":00" ;
        
        if (!show.getSlots().containsKey(formattedSlot) || show.getSlots().get(formattedSlot) < persons) {
            WaitlistEntry entry = new WaitlistEntry();
            entry.setUser(user);
            entry.setShowName(showName);
            entry.setSlot(slot); 
            entry.setPersons(persons);
            waitlistRepository.add(entry);
            return "Slot is full or unavailable. Added to waitlist.";
        }

        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUser(user);
        booking.setShowName(showName);
        booking.setSlot(formattedSlot); 
        booking.setPersons(persons);
        bookingRepository.save(booking);

        int remainingCapacity = show.getSlots().get(formattedSlot) - persons;
        show.getSlots().put(formattedSlot, remainingCapacity);
        System.out.println("Booked. Booking ID: " + bookingId);
        return "Booked. Booking ID: " + bookingId;
    }
    public String cancelBooking(String bookingId) {
    	System.out.println("ShowService.cancelBooking()");
        Booking booking = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new BookingException("Booking not found!"));

        Show show = showRepository.findByName(booking.getShowName());
        int updatedCapacity = show.getSlots().get(booking.getSlot()) + booking.getPersons();
        show.getSlots().put(booking.getSlot(), updatedCapacity);
        bookingRepository.delete(booking);

        WaitlistEntry entry = waitlistRepository.findFirst(booking.getShowName(), booking.getSlot());
        if (entry != null) {
            bookTicket(entry.getUser(), entry.getShowName(), entry.getSlot(), entry.getPersons());
            waitlistRepository.remove(entry);
        }
        return "Booking canceled.";
    }
}
