package com.hansarangdelivery.menu.application.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.menu.application.dto.MenuItemInternalResponseDto;
import com.hansarangdelivery.menu.domain.model.MenuItem;
import com.hansarangdelivery.menu.presentation.dto.CreateMenuItemRequestDto;
import com.hansarangdelivery.menu.presentation.dto.MenuItemResponseDto;
import com.hansarangdelivery.menu.presentation.dto.UpdateMenuItemRequestDto;
import com.hansarangdelivery.user.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MenuItemService {

  MenuItemInternalResponseDto createMenuItem(CreateMenuItemRequestDto requestDto);
  PageResponseDto<MenuItemInternalResponseDto> searchAllMenuItem(UUID restaurantId, Pageable pageable);
  MenuItemInternalResponseDto readMenuItem(UUID menuItemId);
  MenuItemInternalResponseDto updateMenuItem(UUID menuItemId, UpdateMenuItemRequestDto requestDto);
  MenuItemInternalResponseDto updateAvailableMenuItem(UUID menuItemId, Boolean isAvailable);
  MenuItemInternalResponseDto deleteMenuItem(UUID menuItemId, User user);
  List<MenuItemInternalResponseDto> deleteMenuItemByRestaurantId(UUID restaurantId, User user);
  MenuItem getMenuById(UUID menuId);
  boolean isExist(UUID restaurantId);
}
