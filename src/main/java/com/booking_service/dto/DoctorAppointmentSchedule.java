package com.booking_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class DoctorAppointmentSchedule {
    private Long id;

    private Doctor doctor;

    private List<TimeSlots> timeSlots;

    public List<TimeSlots> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlots> timeSlots) {
        this.timeSlots = timeSlots;
    }

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}