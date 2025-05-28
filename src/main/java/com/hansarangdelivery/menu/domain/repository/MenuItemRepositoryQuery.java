package com.hansarangdelivery.menu.domain.repository;

import com.hansarangdelivery.menu.domain.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemRepositoryQuery {

    Page<MenuItem> searchMenuItemByRestaurantId(UUID restaurantId, Pageable pageable);
}
