package com.nt.controller;

import com.nt.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping("/registerShow")
    public String registerShow(@RequestParam String name, @RequestParam String genre) {
        return showService.registerShow(name, genre);
    }
    
    @PostMapping("/onboard")
    public String onboardShowSlots(@RequestParam String name, @RequestBody Map<String, Integer> slots) {
        return showService.onboardShowSlots(name, slots);
    }

    @GetMapping("/availability/{genre}")
    public Map<String , Map<String, Integer> >  showAvailabilityByGenre(@PathVariable String genre) {
        return showService.showAvailabilityByGenre(genre);
    }

    @PostMapping("/book")
    public String bookTicket(@RequestParam String user, @RequestParam String showName,
                             @RequestParam String slot, @RequestParam int persons) {
        return showService.bookTicket(user, showName, slot, persons);
    }

    @DeleteMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId) {
        return showService.cancelBooking(bookingId);
    }
}
