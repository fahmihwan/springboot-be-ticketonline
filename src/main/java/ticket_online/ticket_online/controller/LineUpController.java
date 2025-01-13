package ticket_online.ticket_online.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;
import ticket_online.ticket_online.service.LineUpService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/lineup")
public class LineUpController {

    @Autowired
    LineUpService lineUpService;


    @GetMapping("/{slug}")
    public  ResponseEntity<ApiResponse<List<LineUp>>> getAllLineUpBySlug(@PathVariable String slug){
        try {
            List<LineUp> response = lineUpService.getAllLineUpBySlug(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Line up created successfully", response));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/{slug}")
    public ResponseEntity<ApiResponse<LineUp>> storeLineUp(@RequestBody LineUp lineUp, @PathVariable String slug){
        try {
            LineUp response = lineUpService.createLineUp(lineUp, slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Line up created successfully", response));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeLineUp(@PathVariable Long id){
        System.out.println(id);
        try {
            Boolean response = lineUpService.removeLineup(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Line up deleted successfully", response));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

}
