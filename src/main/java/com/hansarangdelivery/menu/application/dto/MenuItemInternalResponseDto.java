package com.hansarangdelivery.menu.application.dto;

import java.util.UUID;

public record MenuItemInternalResponseDto(
    UUID id, String name, Integer price, Boolean isAvailable
) {

}
