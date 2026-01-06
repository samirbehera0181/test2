package com.booking_service.controller;

import com.booking_service.client.DoctorClient;
import com.booking_service.client.PatientClient;
import com.booking_service.client.PaymentClient;
import com.booking_service.dto.*;
import com.booking_service.entity.BookingConfirmation;
import com.booking_service.repository.BookingConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private DoctorClient doctorClient;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private BookingConfirmationRepository bookingConfirmationRepository;

    // http://localhost:8083/api/v1/booking/getdoctor?doctorId=1&patientId=1
    // http://localhost:8083/api/v1/booking/book?doctorId=1&patientId=1&date=2025-12-20&time=13:00
    @GetMapping("/book")
    public StripeResponse getDoctorById(@RequestParam long doctorId, @RequestParam long patientId,
                                @RequestParam LocalDate date,@RequestParam LocalTime time){

        Patient p = patientClient.getPatientById(patientId);
        Doctor d = doctorClient.getDoctorById(doctorId);

        BookingConfirmation bookingConfirmation = new BookingConfirmation();
        bookingConfirmation.setDoctorName(d.getName());
        bookingConfirmation.setPatientName(p.getName());
        bookingConfirmation.setAddress(d.getAddress());

        List<DoctorAppointmentSchedule> appointmentschedules = d.getAppointmentschedules();
        for (DoctorAppointmentSchedule app : appointmentschedules){
            LocalDate localDate = app.getDate();
            if (localDate.isEqual(date)){
                List<TimeSlots> timeSlots = app.getTimeSlots();
                for (TimeSlots ts : timeSlots){
                    if (ts.getTime().equals(time)) {
                        bookingConfirmation.setDate(date);
                        bookingConfirmation.setTime(time);
                    }
                }
            }
        }

        //save the confirmation
        BookingConfirmation savedBookingConfirmation = bookingConfirmationRepository.save(bookingConfirmation);

        ProductRequest pr = new ProductRequest();
        pr.setName(bookingConfirmation.getPatientName());
        pr.setAmount(500000L);
        pr.setCurrency("INR");
        pr.setQuantity(1L);
        pr.setBookingId(savedBookingConfirmation.getId());

        StripeResponse stripeResponse = paymentClient.checkoutProducts(pr);
        return stripeResponse;

    }

    @GetMapping("/bookingid")
    public BookingConfirmation getBookingById(@RequestParam("bookingId") long bookingId){
        return bookingConfirmationRepository.findById(bookingId).get();
    }

    @PutMapping("/updatestatus")
    public void confirmBooking(@RequestBody BookingConfirmation bookingConfirmation){
        bookingConfirmationRepository.save(bookingConfirmation);
    }

}
