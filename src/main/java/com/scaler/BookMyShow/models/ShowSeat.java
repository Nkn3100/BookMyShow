package com.scaler.BookMyShow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ShowSeat extends BaseModel{
    @ManyToOne
    private MovieShow movieShow;

    @Enumerated(EnumType.ORDINAL) // in one to many case along we need @ElementCollection
    private ShowSeatStatus showSeatStatus;

    @ManyToOne
    private Seat seat;
    private double price;
}
