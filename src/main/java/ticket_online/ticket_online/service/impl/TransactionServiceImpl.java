package ticket_online.ticket_online.service.impl;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.transaction.AddCartTicketReqDto;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.Transaction;
import ticket_online.ticket_online.model.Visitor;
import ticket_online.ticket_online.repository.DetailTransactionRepository;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.TransactionRepository;
import ticket_online.ticket_online.repository.VisitorRepository;
import ticket_online.ticket_online.service.TransactionService;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    private DetailTransactionRepository detailTransactionRepository;
    private VisitorRepository visitorRepository;

    private EventRepository eventRepository;

    public DetailTransaction createCartTicket(AddCartTicketReqDto addCartTicketReqDto){

        System.out.println(addCartTicketReqDto);

        try {
            if(addCartTicketReqDto.getDetailTransactions().isEmpty()){
                Optional<Event> event = eventRepository.findFirstBySlugAndIsActiveTrue(addCartTicketReqDto.getSlug());
            }

//            if(event.isPresent()){
//                System.out.println(event);
//
//
//            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }


        return  null;
    }
}
