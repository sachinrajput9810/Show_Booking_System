#  Show Booking System

The **Show Booking System** is a console-based application that allows organizers to manage live shows and users to book tickets. It supports features like show registration, slot onboarding, ticket booking, cancellation, and viewing available shows based on genres.

##  Features

### For Organizers:
- **Register Shows**: Add new live shows with a specified genre (e.g., Comedy, Theatre, Tech, Singing, etc.).
- **Onboard Show Slots**: Declare show timings with capacities for 1-hour time slots (e.g., 9:00-10:00, 10:00-11:00).
  - Slots should not overlap for the same show.
  - Multiple shows can run in parallel.
  
### For Users:
- **Search Shows by Genre**: View available shows and their slots for a specific genre.
- **Book Tickets**: Reserve tickets for available slots .
- **Cancel Bookings**: Free up a slot for others by canceling an existing booking.

##  Rules & Constraints
- **Slot Management**: Slots are divided into 1-hour intervals (e.g., 9:00-10:00, 10:00-11:00).
- **Booking Rules**:
  - Users can book multiple tickets for different shows.
  - Users cannot book tickets for two shows in the same time slot.
  - Tickets cannot be partially booked (e.g., a slot with 2 seats cannot accommodate a booking for 3 seats).
- **Cancellations**: Canceling a booking releases the slot capacity for others.

##  Technology Stack
- **Language**: Java
- **Persistence**: In-memory data structures (No external databases used)
- **Architecture**: Modular, clean code with separation of concerns.
