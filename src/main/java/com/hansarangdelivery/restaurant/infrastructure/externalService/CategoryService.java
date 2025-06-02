package com.hansarangdelivery.restaurant.infrastructure.externalService;

import com.hansarangdelivery.category.model.Category;
import java.util.UUID;

public interface CategoryService {
  Category getCategoryById(UUID categoryId);
  Category getByName(String name);
  boolean existsById(UUID categoryId);
}
