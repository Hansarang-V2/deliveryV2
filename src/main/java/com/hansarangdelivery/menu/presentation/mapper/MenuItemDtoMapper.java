package com.hansarangdelivery.menu.presentation.mapper;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.menu.application.dto.MenuItemInternalResponseDto;
import com.hansarangdelivery.menu.presentation.dto.MenuItemResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MenuItemDtoMapper {

  public MenuItemResponseDto toResponse(MenuItemInternalResponseDto dto) {

    return new MenuItemResponseDto(dto.id(), dto.name(), dto.price(), dto.isAvailable());
  }

  public List<MenuItemResponseDto> toResponseList(List<MenuItemInternalResponseDto> menuItems) {

    return menuItems.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public PageResponseDto<MenuItemResponseDto> toPageResponse(
      PageResponseDto<MenuItemInternalResponseDto> response) {

    List<MenuItemResponseDto> menuItems = response.getContent().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());

    return new PageResponseDto<>(
        menuItems,
        response.getPage(),
        response.getSize(),
        response.getTotalElements(),
        response.getTotalPages()
    );
  }
}
