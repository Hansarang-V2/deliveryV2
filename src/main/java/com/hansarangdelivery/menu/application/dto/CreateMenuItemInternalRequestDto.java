package com.hansarangdelivery.menu.application.dto;

import java.util.UUID;

public record CreateMenuItemInternalRequestDto(
    String name, Integer price, UUID restaurantId, Boolean isAvailable) {

}
