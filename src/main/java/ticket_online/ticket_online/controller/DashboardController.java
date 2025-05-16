package ticket_online.ticket_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.cart.CartResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.DashboardService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats-ui")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDataStatUi(){
        try {
            Map<String, Object> data = dashboardService.getDataStatUi();
            return  ResponseEntity.ok(new ApiResponse<>(true, "stat data retrieved", data));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-five-new-event")
    public ResponseEntity<ApiResponse<List<Event>>> getFiveNewEvent(){
        try {
            List<Event> data = dashboardService.getFiveNewEvent();
            return  ResponseEntity.ok(new ApiResponse<>(true, "five new event data retrieved", data));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-number-of-transaction-per-month")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNumberOfTransactionPerMonth(){
        try {
            List<Map<String, Object>> data = dashboardService.getNumberOfTransactionPerMonth();
            return  ResponseEntity.ok(new ApiResponse<>(true, "number of transaction per month data retrieved", data));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
