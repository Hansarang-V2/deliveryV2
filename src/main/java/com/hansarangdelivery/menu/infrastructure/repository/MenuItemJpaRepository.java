package com.hansarangdelivery.menu.infrastructure.repository;

import com.hansarangdelivery.menu.domain.model.MenuItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemJpaRepository extends JpaRepository<MenuItem, UUID> {

  List<MenuItem> findAllByRestaurantId(UUID restaurantId);

  boolean existsByRestaurantIdAndName(UUID restaurantId, String name);

  boolean existsByRestaurantId(UUID restaurantId);
}
