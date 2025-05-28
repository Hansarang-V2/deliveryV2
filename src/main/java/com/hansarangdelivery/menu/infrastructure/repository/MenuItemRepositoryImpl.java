package com.hansarangdelivery.menu.infrastructure.repository;

import com.hansarangdelivery.menu.domain.model.MenuItem;
import com.hansarangdelivery.menu.domain.repository.MenuItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {

  private final MenuItemJpaRepository jpaRepository;

  @Override
  public void save(MenuItem menuItem) {
    jpaRepository.save(menuItem);
  }

  @Override
  public Optional<MenuItem> findById(UUID menuItemId) {
    return jpaRepository.findById(menuItemId);
  }

  @Override
  public List<MenuItem> findAllByRestaurantId(UUID restaurantId) {
    return jpaRepository.findAllByRestaurantId(restaurantId);
  }

  @Override
  public boolean existsByRestaurantIdAndName(UUID restaurantId, String name) {
    return jpaRepository.existsByRestaurantIdAndName(restaurantId, name);
  }

  @Override
  public boolean existsByRestaurantId(UUID restaurantId) {
    return jpaRepository.existsByRestaurantId(restaurantId);
  }
}
