package com.hansarangdelivery.menu.application.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.exception.DuplicateResourceException;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.menu.application.dto.MenuItemInternalResponseDto;
import com.hansarangdelivery.menu.application.mapper.MenuItemMapper;
import com.hansarangdelivery.menu.domain.model.MenuItem;
import com.hansarangdelivery.menu.domain.repository.MenuItemRepository;
import com.hansarangdelivery.menu.domain.repository.MenuItemRepositoryQuery;
import com.hansarangdelivery.menu.presentation.dto.CreateMenuItemRequestDto;
import com.hansarangdelivery.menu.presentation.dto.MenuItemResponseDto;
import com.hansarangdelivery.menu.presentation.dto.UpdateMenuItemRequestDto;
import com.hansarangdelivery.restaurant.service.RestaurantExistService;
import com.hansarangdelivery.user.model.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService{

  private final MenuItemRepository menuItemRepository;
  private final MenuItemRepositoryQuery menuItemRepositoryQuery;
  private final RestaurantExistService restaurantExistService;
  private final MenuItemMapper menuItemMapper;

  @Override
  public MenuItemInternalResponseDto createMenuItem(CreateMenuItemRequestDto requestDto) {
    MenuItem menuItem = new MenuItem(
        requestDto.getName(), requestDto.getPrice(), requestDto.getRestaurantId()
    );

    // RestaurantId 와 Name 을 기준으로 동일한 정보가 있으면 중복으로 판단
    if (isDuplicate(menuItem)) {
      throw new DuplicateResourceException("이미 존재하는 메뉴입니다.");
    }

    if(!restaurantExistService.isExist(requestDto.getRestaurantId())) {
      throw new ForbiddenActionException("없는 식당 정보입니다.");
    }

    menuItemRepository.save(menuItem);
    return menuItemMapper.toResponse(menuItem);
  }

  @Override
  @Cacheable(
      value = "menuItems",
      key = "#restaurantId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()"
  )
  public PageResponseDto<MenuItemInternalResponseDto> searchAllMenuItem(
      UUID restaurantId, Pageable pageable) {

    Page<MenuItem> menuItems = menuItemRepositoryQuery.searchMenuItemByRestaurantId(restaurantId, pageable);

    if (menuItems.isEmpty()) {
      throw new ResourceNotFoundException("조회된 메뉴가 없습니다.");
    }

    return menuItemMapper.toResponsePage(menuItems);
  }

  @Override
  public MenuItemInternalResponseDto readMenuItem(UUID menuItemId) {
    MenuItem menuItem = menuItemRepository.findById(menuItemId)
        .orElseThrow(() -> new ResourceNotFoundException("찾는 메뉴가 없습니다."));

    return menuItemMapper.toResponse(menuItem);
  }

  @Override
  @Transactional
  public MenuItemInternalResponseDto updateMenuItem(UUID menuItemId, UpdateMenuItemRequestDto requestDto) {
    MenuItem menuItem = menuItemRepository.findById(menuItemId)
        .orElseThrow(() -> new ResourceNotFoundException("찾는 메뉴가 없습니다."));

    menuItem.update(requestDto);

    return menuItemMapper.toResponse(menuItem);
  }

  @Override
  @Transactional
  @CacheEvict(value = "menuItems", allEntries = true)
  public MenuItemInternalResponseDto updateAvailableMenuItem(UUID menuItemId, Boolean isAvailable) {
    MenuItem menuItem = menuItemRepository.findById(menuItemId)
        .orElseThrow(() -> new ResourceNotFoundException("찾는 메뉴가 없습니다."));

    menuItem.setAvailable(isAvailable);

    return menuItemMapper.toResponse(menuItem);
  }

  @Override
  @Transactional
  public MenuItemInternalResponseDto deleteMenuItem(UUID menuItemId, User user) {
    MenuItem menuItem = menuItemRepository.findById(menuItemId)
        .orElseThrow(() -> new ResourceNotFoundException("이미 삭제된 메뉴입니다."));

    // 삭제된 시간과 userId 를 String 으로 변환하여 저장
    menuItem.delete(LocalDateTime.now(), user.getId().toString());

    return menuItemMapper.toResponse(menuItem);
  }

  @Override
  @Transactional
  // 레스토랑이 삭제될 경우 호출
  public List<MenuItemInternalResponseDto> deleteMenuItemByRestaurantId(UUID restaurantId, User user) {
    List<MenuItem> menuItemList = menuItemRepository.findAllByRestaurantId(restaurantId);

    if (menuItemList.isEmpty()) {
      throw new ResourceNotFoundException("이미 삭제된 정보입니다.");
    }

    return menuItemMapper.toResponseList(menuItemList);
  }

  @Override
  public MenuItem getMenuById(UUID menuId) {
    return menuItemRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다: " + menuId));
  }

  @Override
  public boolean isExist(UUID restaurantId) {
    return menuItemRepository.existsByRestaurantId(restaurantId);
  }

  public boolean isDuplicate(MenuItem menuItem) {

    return menuItemRepository.existsByRestaurantIdAndName(menuItem.getRestaurantId(), menuItem.getName());
  }
}
