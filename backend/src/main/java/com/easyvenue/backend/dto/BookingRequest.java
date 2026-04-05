package com.easyvenue.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingRequest {

    private Long venueId;

    private LocalDate bookingDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer hoursBooked;

    public BookingRequest() {
    }

    public BookingRequest(Long venueId,
                          LocalDate bookingDate, LocalDateTime startTime, LocalDateTime endTime, Integer hoursBooked) {
        this.venueId = venueId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursBooked = hoursBooked;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String toString() {
        return "BookingRequest{" +
                "venueId=" + venueId +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", hoursBooked=" + hoursBooked +
                '}';
    }
}
