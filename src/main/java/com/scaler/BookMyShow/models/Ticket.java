package com.scaler.BookMyShow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Ticket extends BaseModel{

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    @ManyToOne
    private MovieShow show;

    @OneToMany
    private List<Payment> payments;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<ShowSeat> showSeats;
    private int amount;
    private Date bookedAt;
}
/*
Suppose ticket 1 bought Seat1
then ticket1 was cancelled
then ticket2 bought seat1
ticket2 was cancelled
ticket 3 bought seat1

seat1 belongs to 3 tickets,
 */
