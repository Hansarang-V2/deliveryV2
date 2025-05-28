package com.hansarangdelivery.menu.presentation.controller;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.menu.application.dto.MenuItemInternalResponseDto;
import com.hansarangdelivery.menu.presentation.dto.CreateMenuItemRequestDto;
import com.hansarangdelivery.menu.presentation.dto.MenuItemResponseDto;
import com.hansarangdelivery.menu.presentation.dto.UpdateMenuItemRequestDto;
import com.hansarangdelivery.menu.presentation.mapper.MenuItemDtoMapper;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.menu.application.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemDtoMapper menuItemDtoMapper;

    @Operation(summary = "메뉴 추가", description = "새로운 메뉴를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 저장 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> createMenuItem(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = CreateMenuItemRequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "메뉴 추가 예시",
                        value = """
                    {
                      "name": "햄버거",
                      "price": 5000,
                      "description": "맛있는 햄버거",
                      "restaurantId": "123e4567-e89b-12d3-a456-426614174000"
                    }
                    """
                    )
                }
            )
        )
        @RequestBody CreateMenuItemRequestDto requestDto) {
        MenuItemInternalResponseDto internalResponse = menuItemService.createMenuItem(requestDto);
        MenuItemResponseDto response = menuItemDtoMapper.toResponse(internalResponse) ;
        return new ResultResponseDto<>("메뉴 저장 성공", 200, response);
    }

    @Operation(summary = "레스토랑 메뉴 조회", description = "특정 레스토랑의 모든 메뉴를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 조회 성공")
    @Parameters({
        @Parameter(name = "restaurantId", description = "레스토랑 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000"),
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: name,asc)", example = "name,asc")
    })
    @GetMapping("/restaurant")
    public ResultResponseDto<PageResponseDto<MenuItemResponseDto>> searchAllMenuItem(
        @RequestParam UUID restaurantId,
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<MenuItemInternalResponseDto> responseDtoList = menuItemService.searchAllMenuItem(restaurantId, pageable);
        PageResponseDto<MenuItemResponseDto> response = menuItemDtoMapper.toPageResponse(responseDtoList);
        return new ResultResponseDto<>("메뉴 조회 성공", 200, response);
    }

    @Operation(summary = "특정 메뉴 조회", description = "메뉴 ID로 특정 메뉴를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 조회 성공")
    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음", content = @Content)
    @GetMapping("/{menuItemId}")
    public ResultResponseDto<MenuItemResponseDto> readMenuItem(
        @Parameter(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID menuItemId) {
        MenuItemInternalResponseDto responseDto = menuItemService.readMenuItem(menuItemId);
        MenuItemResponseDto response = menuItemDtoMapper.toResponse(responseDto);
        return new ResultResponseDto<>("메뉴 조회 성공", 200, response);
    }

    @Operation(summary = "메뉴 정보 수정", description = "메뉴 ID로 메뉴 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음", content = @Content)
    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> updateMenuItem(
        @Parameter(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID menuItemId,
        @RequestBody UpdateMenuItemRequestDto requestDto) {
        MenuItemInternalResponseDto responseDto = menuItemService.updateMenuItem(menuItemId, requestDto);
        MenuItemResponseDto response = menuItemDtoMapper.toResponse(responseDto);
        return new ResultResponseDto<>("메뉴 수정 성공", 200, response);
    }

    @Operation(summary = "메뉴 가용성 수정", description = "메뉴 ID로 메뉴의 숨김 처리 상태를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음", content = @Content)
    @PutMapping("/available/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> updateAvailableMenuItem(
        @Parameter(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID menuItemId,
        @Parameter(description = "메뉴 가용성", example = "true")
        @RequestParam Boolean isAvailable) {
        MenuItemInternalResponseDto responseDto = menuItemService.updateAvailableMenuItem(menuItemId, isAvailable);
        MenuItemResponseDto response = menuItemDtoMapper.toResponse(responseDto);
        return new ResultResponseDto<>("메뉴 수정 성공", 200, response);
    }

    @Operation(summary = "메뉴 삭제", description = "메뉴 ID로 메뉴를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음", content = @Content)
    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> deleteMenuItem(
        @Parameter(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID menuItemId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MenuItemInternalResponseDto responseDto = menuItemService.deleteMenuItem(menuItemId, userDetails.getUser());
        MenuItemResponseDto response = menuItemDtoMapper.toResponse(responseDto);
        return new ResultResponseDto<>("메뉴 삭제 성공", 200, response);
    }

    @Operation(summary = "레스토랑 전체 메뉴 삭제", description = "특정 레스토랑의 모든 메뉴를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 전체 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "레스토랑을 찾을 수 없음", content = @Content)
    @DeleteMapping("/all/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<List<MenuItemResponseDto>> deleteAllMenuItem(
        @Parameter(description = "레스토랑 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID restaurantId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MenuItemInternalResponseDto> reponseList = menuItemService.deleteMenuItemByRestaurantId(restaurantId, userDetails.getUser());
        List<MenuItemResponseDto> responses = menuItemDtoMapper.toResponseList(reponseList);
        return new ResultResponseDto<>("메뉴 전체 삭제 성공", 200, responses);
    }

}
