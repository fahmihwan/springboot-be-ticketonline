package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.Transaction;
import ticket_online.ticket_online.model.Visitor;
import ticket_online.ticket_online.repository.DetailTransactionRepository;
import ticket_online.ticket_online.repository.TransactionRepository;
import ticket_online.ticket_online.repository.VisitorRepository;
import ticket_online.ticket_online.service.TransactionService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    private DetailTransactionRepository detailTransactionRepository;
    private VisitorRepository visitorRepository;

    public Transaction checkoutTicket(CheckoutReqDto checkoutReqDto){

        List<Visitor> visitorReq = checkoutReqDto.getVisitors();
        List<DetailTransaction> detailTransactionReq = checkoutReqDto.getDetailTransactions();

        try {
            if(visitorReq.toArray().length <= 0){
                throw new RuntimeException("visitor doesnt exists");
            }

            if(detailTransactionReq.toArray().length <= 0){
                throw new RuntimeException("detail transaction doesnt exist");
            }


        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }


        return  null;
    }
}
