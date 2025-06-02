package com.hansarangdelivery.restaurant.application;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.restaurant.application.dto.RestaurantRequestDto;
import com.hansarangdelivery.restaurant.application.dto.RestaurantResponseDto;
import com.hansarangdelivery.user.model.User;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {

  /**
   * 새로운 음식점을 등록한다.
   * @param requestDto 음식점 등록 요청 정보
   * @return 등록된 음식점 정보
   */
  RestaurantResponseDto register(RestaurantRequestDto requestDto);

  /**
   * 특정 음식점의 상세 정보를 조회한다.
   * @param restaurantId 음식점 ID
   * @return 음식점 상세 정보
   */
  RestaurantResponseDto getRestaurantInfo(UUID restaurantId);

  /**
   * 특정 음식점의 정보를 수정한다.
   * @param restaurantId 음식점 ID
   * @param requestDto 수정할 정보
   * @return 수정된 음식점 정보
   */
  RestaurantResponseDto updateRestaurant(UUID restaurantId, RestaurantRequestDto requestDto);

  /**
   * 음식점을 삭제 처리한다 (soft delete).
   * @param user 삭제를 요청한 사용자
   * @param restaurantId 음식점 ID
   * @return 삭제 처리된 음식점 정보
   */
  RestaurantResponseDto deleteRestaurant(User user, UUID restaurantId);

  /**
   * 음식점을 검색 조건에 따라 페이징하여 조회한다.
   * @param pageable 페이지 요청 정보
   * @param search 검색 키워드
   * @param category 카테고리 이름 (nullable)
   * @return 검색된 음식점 목록 페이지
   */
  PageResponseDto<RestaurantResponseDto> searchRestaurants(Pageable pageable, String search, String category);
}
