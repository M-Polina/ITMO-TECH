package com.mpolina.cats.dto;

public record UserDto(Long userId, String username, String role, Long ownerId) {
}

