package com.easyvenue.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.easyvenue.backend.model.Booking;

public class BookingAdminViewDto {

    private Long id;
    private LocalDate bookingDate;
    private Integer hoursBooked;
    private String status;
    private Double totalCost;
    private LocalDateTime createdAt;
    private BookingUserDto user;
    private BookingVenueDto venue;

    public static BookingAdminViewDto from(Booking booking) {
        BookingAdminViewDto dto = new BookingAdminViewDto();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setHoursBooked(booking.getHoursBooked());
        dto.setStatus(booking.getStatus().name());
        dto.setTotalCost(booking.getTotalCost());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUser(new BookingUserDto(
                booking.getUser().getId(),
                booking.getUser().getName(),
                booking.getUser().getName(),
                booking.getUser().getEmail()
        ));
        dto.setVenue(new BookingVenueDto(
                booking.getVenue().getId(),
                booking.getVenue().getName(),
                booking.getVenue().getLocation()
        ));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getHoursBooked() {
        return hoursBooked;
    }

    public void setHoursBooked(Integer hoursBooked) {
        this.hoursBooked = hoursBooked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookingUserDto getUser() {
        return user;
    }

    public void setUser(BookingUserDto user) {
        this.user = user;
    }

    public BookingVenueDto getVenue() {
        return venue;
    }

    public void setVenue(BookingVenueDto venue) {
        this.venue = venue;
    }

    public static class BookingUserDto {
        private final Long id;
        private final String name;
        private final String username;
        private final String email;

        public BookingUserDto(Long id, String name, String username, String email) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class BookingVenueDto {
        private final Long id;
        private final String name;
        private final String location;

        public BookingVenueDto(Long id, String name, String location) {
            this.id = id;
            this.name = name;
            this.location = location;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }
    }
}
