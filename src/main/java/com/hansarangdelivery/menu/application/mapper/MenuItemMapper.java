package com.hansarangdelivery.menu.application.mapper;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.menu.application.dto.CreateMenuItemInternalRequestDto;
import com.hansarangdelivery.menu.application.dto.MenuItemInternalResponseDto;
import com.hansarangdelivery.menu.domain.model.MenuItem;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

  public MenuItem toEntity(CreateMenuItemInternalRequestDto request) {

    return new MenuItem(request.name(), request.price(), request.restaurantId());
  }

  public MenuItemInternalResponseDto toResponse(MenuItem menuItem) {

    return new MenuItemInternalResponseDto(
        menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.getIsAvailable()
    );
  }

  public List<MenuItemInternalResponseDto> toResponseList(List<MenuItem> menuItems) {

    return menuItems.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public PageResponseDto<MenuItemInternalResponseDto> toResponsePage(
      Page<MenuItem> menuItems) {

    Page<MenuItemInternalResponseDto> responses = menuItems.map(this::toResponse);

    return new PageResponseDto<>(responses);
  }
}
