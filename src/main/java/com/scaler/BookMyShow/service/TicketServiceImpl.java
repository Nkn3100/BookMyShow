package com.scaler.BookMyShow.service;

import com.scaler.BookMyShow.exception.ShowSeatNotAvailable;
import com.scaler.BookMyShow.exception.TicketNotFoundException;
import com.scaler.BookMyShow.exception.UserNotFoundException;
import com.scaler.BookMyShow.models.*;
import com.scaler.BookMyShow.repository.ShowRepository;
import com.scaler.BookMyShow.repository.ShowSeatRepository;
import com.scaler.BookMyShow.repository.TicketRepository;
import com.scaler.BookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(Long userId, List<Long> showSeatIds, Long showId) {
        User bookedByUser = userRepository.findById(userId).get();
        MovieShow show = showRepository.findById(showId).get();
        for(Long showSeatId : showSeatIds){
            ShowSeat showSeat = showSeatRepository.findById(showSeatId).get();
            if(showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)){
                showSeat.setShowSeatStatus(ShowSeatStatus.LOCKED);

            }else{
                throw new ShowSeatNotAvailable("Show seat is not available");
            }
            showSeatRepository.save(showSeat);
        }
        boolean paymentDone = paymentCheck();
        List<ShowSeat> showSeats = new ArrayList<>();
        double amount = 0;

        if(paymentDone){
            for(Long showSeatId : showSeatIds){
                ShowSeat showSeat = showSeatRepository.findById(showSeatId).get();
                showSeat.setShowSeatStatus(ShowSeatStatus.BOOKED);
                showSeat = showSeatRepository.save(showSeat);
                showSeats.add(showSeat);
                amount += showSeat.getPrice();
            }
        }

        return ticketGenerator(bookedByUser,show,showSeats,amount);

    }

    @Override
    public Ticket cancelTicket(Long ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(ticketOptional.isEmpty()){
            throw new TicketNotFoundException("Ticket for given ID is not found");
        }
        Ticket ticket = ticketOptional.get();
        ticket.setBookingStatus(BookingStatus.CANCELLED);
        for(ShowSeat showSeat : ticket.getShowSeats()){
            showSeat.setShowSeatStatus(ShowSeatStatus.AVAILABLE);
            showSeatRepository.save(showSeat);
        }
        ticketRepository.save(ticket);
        for(Payment p : ticket.getPayments()){
            p.getRefNo();
            //send a message to 3rd party with payment refNo for refund
        }

        return ticket;
    }

    @Override
    public Ticket transferTicket(Long ticketId, Long fromUserId, Long toUserId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(ticketOptional.isEmpty()){
            throw new TicketNotFoundException("Ticket for given ID is not found");
        }
        Optional<User> fromUserOptional = userRepository.findById(fromUserId);
        Optional<User> toUserOptional = userRepository.findById(toUserId);
        if(fromUserOptional.isEmpty() || toUserOptional.isEmpty()){
            throw new UserNotFoundException("user details given for ticket transfer not found");
        }
        Ticket ticket = ticketOptional.get();
        User fromUser = fromUserOptional.get();
        List<Ticket> bookedTicketHistoryFromUser = fromUser.getTickets();
        bookedTicketHistoryFromUser.remove(ticket);
        fromUser.setTickets(bookedTicketHistoryFromUser);
        userRepository.save(fromUser);

        User toUser = toUserOptional.get();
        List<Ticket> bookedTicketHistoryToUser = toUser.getTickets();
        bookedTicketHistoryToUser.add(ticket);
        toUser.setTickets(bookedTicketHistoryToUser);
        userRepository.save(toUser);

        ticket.setUser(toUser);
        ticketRepository.save(ticket);
        return null;
    }
    private boolean paymentCheck(){
        return true;
    }
    private Ticket ticketGenerator(User user, MovieShow show, List<ShowSeat> showSeats, double amount){
        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setUser(user);
        ticket.setShowSeats(showSeats);
        ticket.setAmount(amount);
        ticket.setBookedAt(LocalDateTime.now());
        ticket.setBookingStatus(BookingStatus.CONFIRMED);
        return ticketRepository.save(ticket);
    }
}
