package uk.ac.leeds.comp2913.api.ViewModel.Assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import uk.ac.leeds.comp2913.api.Controller.AccountController;
import uk.ac.leeds.comp2913.api.Controller.ActivityController;
import uk.ac.leeds.comp2913.api.Controller.BookingController;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingPagedResourcesAssembler extends RepresentationModelAssemblerSupport<Booking, BookingDTO> {

    public BookingPagedResourcesAssembler(){
        super(BookingController.class, BookingDTO.class);
    }

    @Override
    public BookingDTO toModel(Booking booking){
        BookingDTO bookingDto = instantiateModel(booking);
        bookingDto.add(linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel());
        bookingDto.add(linkTo(methodOn(AccountController.class).getAccountById(booking.getAccount().getId())).withRel("Account"));
        bookingDto.add(linkTo(methodOn(ActivityController.class).getActivityByActivityId(booking.getActivity().getId())).withRel("Activity"));
        //bookingDto.add(linkTo(ReceiptController.class).slash(booking.getReceipt().getId()).slash("receipt").withRel("Receipt"));
        if (booking.getRegularSession()!=null){
             bookingDto.add(linkTo(BookingController.class).slash("cancel").slash(booking.getActivity().getId()).slash(booking.getAccount().getId()).withRel("Unsubscribe from regular session"));
             bookingDto.setRegularBooking(true);
         }
         else{
             bookingDto.setRegularBooking(false);
         }
         bookingDto.setAccountId(booking.getAccount().getId());
        bookingDto.setId(booking.getId());
        bookingDto.setParticipants(booking.getParticipants());
        bookingDto.setAmount(booking.getAmount());
        bookingDto.setSession_id(booking.getActivity().getId());
        bookingDto.setTransactionId(booking.getTransactionId());
        bookingDto.setAmount(booking.getAmount());
        return bookingDto;
    }
}
