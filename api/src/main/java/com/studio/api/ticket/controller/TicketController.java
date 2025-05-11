package com.studio.api.ticket.controller;


import com.studio.api.ticket.service.TicketService;
import com.studio.core.global.enums.order.search.TicketSearchCondition;
import com.studio.core.global.repository.Display;
import com.studio.core.global.response.SuccessResponse;
import com.studio.core.ticket.dto.AllTicketSearchDto;
import com.studio.core.ticket.dto.GetTicketTableResponseDto;
import com.studio.core.ticket.repository.TicketQueryJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/ticket")
@Tag(name = "ticket-controller", description = "티켓 관리 API")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    private final TicketQueryJpaRepository queryJpaRepository;

    @Operation(summary = "티켓 리스트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetTicketTableResponseDto.class)))
    })
    @GetMapping
    public SuccessResponse<?> tickets(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @ModelAttribute AllTicketSearchDto allTicketSearchDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return SuccessResponse.ok(queryJpaRepository.getTicketList(pageable, allTicketSearchDto));

    }


    @Operation(summary = "티켓 발급", description = "주문관리 > 티켓 발급/취소관리 > 티켓 발급/취소 > 발급<br>" +
        "자체에서만 가능")
    @PostMapping("/{orderId}")
    public SuccessResponse<?> insertTicket(@PathVariable(value = "orderId") Long orderId) {

        ticketService.insertTicket(orderId);

        return SuccessResponse.ok();

    }

    @Operation(summary = "티켓 사용", description = "티켓 사용처리 리스트 > 티켓 사용처리")
    @PutMapping("/{ticketKey}")
    public SuccessResponse<Void> useTicket(@PathVariable(value = "ticketKey") String ticketKey) {

        ticketService.useTicket(ticketKey);

        return SuccessResponse.ok();

    }

}
