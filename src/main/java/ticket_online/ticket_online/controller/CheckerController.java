package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.cart.CartResDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.dto.checker.ListCheckerResDto;
import ticket_online.ticket_online.dto.checker.ScanTicketReqDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Checker;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.service.CartService;
import ticket_online.ticket_online.service.CheckerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checker")
public class CheckerController {


    @Autowired
    private CheckerService checkerService;

    @PreAuthorize("hasRole('CHECKER')")
    @GetMapping("/{userId}/log-checker")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> logChecker(@PathVariable Long userId){
        try {
            List<Map<String, Object>> listLogChecker = checkerService.logChecker(userId);

            return  ResponseEntity.ok(new ApiResponse<>(true, "list log checker", listLogChecker));
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('CHECKER')")
    @PostMapping()
    public ResponseEntity<ApiResponse<Boolean>> scanTicket(@RequestBody ScanTicketReqDto scanTicketReqDto){
        try {
            Boolean scanTicket = checkerService.scanTicket(scanTicketReqDto.getTicket_code(), scanTicketReqDto.getUserId());
            return  ResponseEntity.ok(new ApiResponse<>(true, "Scan Ticket Successfully", scanTicket));
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('CHECKER')")
    @GetMapping("/{userId}/event")
    public ResponseEntity<ApiResponse<List<EventResDto>>> getEventByCheckerUserId(@PathVariable Long userId){
        try {
            List<EventResDto> eventResDtos = checkerService.getEventByCheckerUser(userId);
            return ResponseEntity.ok(new ApiResponse<List<EventResDto>>(true, "event list retrived ", eventResDtos));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse<Page<CheckerListEventDto>>> getCheckerList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                 @RequestParam(value = "size",defaultValue = "5") int size){
        try {
            Page<CheckerListEventDto> checkers = checkerService.getEventCheckerPagination(page,size);
            return ResponseEntity.ok(new ApiResponse<Page<CheckerListEventDto>>(true, "checker list retrived ", checkers));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{slug}/get-list-checker")
    public ResponseEntity<ApiResponse<List<ListCheckerResDto>>> getListChecker(@PathVariable String slug){
        try {
            List<ListCheckerResDto> checkers = checkerService.getListChecker(slug);
            return ResponseEntity.ok(new ApiResponse<List<ListCheckerResDto>>(true, "checker list retrived ", checkers));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{slug}/regis-checker")
    public ResponseEntity<ApiResponse<ListCheckerResDto>> storeChecker(@RequestBody RegisterReqDto registerReqDto, @PathVariable String slug){
        try {
            ListCheckerResDto checker = checkerService.storeChecker(registerReqDto, slug);
            return  ResponseEntity.ok(new ApiResponse<>(true, "list cart user", checker));

        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeChecker(@PathVariable Long id){
        try {
            checkerService.removeChecker(id);
            return  ResponseEntity.ok(new ApiResponse<>(true, "remove checker successfully", true));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }



}
