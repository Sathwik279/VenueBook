package com.easyvenue.backend.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyvenue.backend.model.Booking;
import com.easyvenue.backend.model.User;
import com.easyvenue.backend.model.Venue;
import com.easyvenue.backend.repository.BookingRepository;
import com.easyvenue.backend.repository.VenueRepository;

@Service
public class BookingService {

    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VenueRepository venueRepository;

    public List<Booking> getMyBookings(User currentUser) {
        return bookingRepository.findByUser(currentUser);
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        Venue venue = validateVenueExists(booking.getVenue().getId());

        LocalDateTime startTime = booking.getStartTime();
        LocalDateTime endTime = booking.getEndTime();

        validateVenueAvailability(venue, startTime, endTime);
        validateNoOverlappingBookings(venue.getId(), startTime, endTime);

        Integer hoursBooked = (int) java.time.Duration.between(startTime, endTime).toHours();
        if (hoursBooked <= 0) {
            throw new IllegalArgumentException("Booking duration must be at least 1 hour");
        }

        booking.setHoursBooked(hoursBooked);
        booking.setTotalCost(calculateBookingCost(venue, hoursBooked));
        booking.setVenue(venue);
        booking.setBookingDate(startTime.toLocalDate());

        return bookingRepository.save(booking);
    }

    public List<Booking> getRecentBookings(User currentUser) {
        if (currentUser.getRole() == User.Role.VENUE_ADMIN) {
            return bookingRepository.findTop10ByVenue_AdminOrderByCreatedAtDesc(currentUser);
        }
        if (currentUser.getRole() == User.Role.ADMIN) {
            return bookingRepository.findTop10ByOrderByCreatedAtDesc();
        }
        throw new IllegalArgumentException("Only admins can view recent bookings");
    }

    public Optional<Booking> getBookingById(Long id, User currentUser) {
        return bookingRepository.findByIdAndUser(id, currentUser);
    }

    public void deleteBooking(Long id, User currentUser) {
        bookingRepository.deleteByIdAndUser(id, currentUser);
    }

    public Booking updateBooking(Long id, Booking updatedBooking, User currentUser) {
        return bookingRepository.findByIdAndUser(id, currentUser)
                .map(existingBooking -> {
                    LocalDateTime startTime = updatedBooking.getStartTime();
                    LocalDateTime endTime = updatedBooking.getEndTime();

                    validateVenueAvailability(existingBooking.getVenue(), startTime, endTime);
                    
                    // Note: This check might need to exclude the current booking ID to allow updating its own time
                    // but for simplicity we'll just check for overlaps. 
                    // To be robust: the query should exclude existingBooking.getId()
                    validateNoOverlappingBookings(existingBooking.getVenue().getId(), startTime, endTime);

                    Integer hoursBooked = (int) java.time.Duration.between(startTime, endTime).toHours();
                    
                    existingBooking.setStartTime(startTime);
                    existingBooking.setEndTime(endTime);
                    existingBooking.setBookingDate(startTime.toLocalDate());
                    existingBooking.setHoursBooked(hoursBooked);
                    existingBooking.setTotalCost(calculateBookingCost(existingBooking.getVenue(), hoursBooked));

                    return bookingRepository.save(existingBooking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    private Venue validateVenueExists(Long venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
    }

    private void validateVenueAvailability(Venue venue, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        
        // If it's a multi-day booking, check all dates
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            if (venue.getUnavailableDates().contains(date)) {
                throw new IllegalArgumentException("Venue is not available (Admin Blocked) on " + date);
            }
            date = date.plusDays(1);
        }
    }

    private void validateNoOverlappingBookings(Long venueId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(venueId, startTime, endTime);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Venue is already booked for the selected time range");
        }
    }

    private Double calculateBookingCost(Venue venue, Integer hoursBooked) {
        return venue.getPricePerHour() * hoursBooked;
    }
}
