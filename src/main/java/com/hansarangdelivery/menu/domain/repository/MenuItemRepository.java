package com.hansarangdelivery.menu.domain.repository;

import com.hansarangdelivery.menu.domain.model.MenuItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository {

  void save(MenuItem menuItem);

  Optional<MenuItem> findById(UUID menuItemId);

  List<MenuItem> findAllByRestaurantId(UUID restaurantId);

  boolean existsByRestaurantIdAndName(UUID restaurantId, String name);

  boolean existsByRestaurantId(UUID restaurantId);
}
