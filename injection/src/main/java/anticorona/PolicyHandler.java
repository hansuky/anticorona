package anticorona;

import anticorona.config.kafka.KafkaProcessor;

import java.util.Optional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    InjectionRepository injectionRepository;


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBooked_AcceptBooking(@Payload Booked booked){

        if(!booked.validate()) return;

        System.out.println("\n\n##### listener AcceptBooking : " + booked.toJson() + "\n\n");

        // 접종예약접수(AcceptBooking) Logic //
        // if(booked.isMe()){
            Injection injection = new Injection();
            injection.setStatus("Injection_Ready");
            injection.setBookingId(booked.getBookingId());            
            injection.setVaccineId(booked.getVaccineId());
            injection.setUserId(booked.getUserId());

            injectionRepository.save(injection);
        // }
          
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookCancelled_CancelAcceptBooking(@Payload BookCancelled bookCancelled){

        if(!bookCancelled.validate()) return;

        System.out.println("\n\n##### listener AcceptCancelBooking : " + bookCancelled.toJson() + "\n\n");

        // 접종예약취소(AcceptCancelBooking) Logic //
        
        // if(bookCancelled.isMe()){
            Optional<Injection> injectionOptional = injectionRepository.findByBookingId(bookCancelled.getBookingId());
            if(injectionOptional.isPresent()){
                Injection injection = injectionOptional.get();
                injection.setStatus("Booking_Cancelled");
                injectionRepository.save(injection);
            }
            
        // }
    }

    @Autowired
    CancellationRepository cancellationRepository;    

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookCancelled_RegCancelBooking(@Payload BookCancelled bookCancelled){

        if(!bookCancelled.validate()) return;

        System.out.println("\n\n##### listener RegCancelBooking : " + bookCancelled.toJson() + "\n\n");

        // 접종예약취소 등록(RegCancelBooking) Logic //
        // if(bookCancelled.isMe()){            
            Cancellation cancellation = new Cancellation();
            
            cancellation.setBookingId(bookCancelled.getBookingId());
            cancellation.setVaccineId(bookCancelled.getVaccineId());
            cancellation.setUserId(bookCancelled.getUserId());
            
            cancellationRepository.save(cancellation);
        // }        
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


    
}
