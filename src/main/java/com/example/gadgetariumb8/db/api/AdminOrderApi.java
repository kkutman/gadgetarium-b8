package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Order Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminOrderApi {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get All Orders", description = "This method getAll Orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaginationResponse<OrderResponse> getAllOrders(@RequestParam(required = false) String keyWord,
                                                          @RequestParam(defaultValue = "PENDING") String status,
                                                          @RequestParam(required = false) LocalDate from,
                                                          @RequestParam(required = false) LocalDate before,
                                                          @RequestParam(required = false, defaultValue = "1") int page,
                                                          @RequestParam(required = false, defaultValue = "5") int pageSize){
        return orderService.getAllOrders(keyWord, status,from,before,page,pageSize);
    }
}
