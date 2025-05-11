package ua.tc.marketplace.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.tc.marketplace.config.MapperConfig;
import ua.tc.marketplace.model.dto.user.CreateUserDto;
import ua.tc.marketplace.model.dto.user.UpdateUserDto;
import ua.tc.marketplace.model.dto.user.UserDto;
import ua.tc.marketplace.model.entity.Ad;
import ua.tc.marketplace.model.entity.User;
import ua.tc.marketplace.model.enums.UserRole;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Mapper interface using MapStruct for converting between User entities and DTOs. Defines mappings
 * for converting User to UserDto, creating User from UserDto or CreateUserDto, and updating an
 * existing User entity with fields from UpdateUserDto.
 */
@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface UserMapper {

    @Mapping(target = "favoriteAdIds", source = "favorites", qualifiedByName = "mapAdsToIds")
    UserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(CreateUserDto dto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "userRole", ignore = true)
//    @Mapping(target = "firstName", ignore = true)
//    @Mapping(target = "lastName", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "profilePicture", ignore = true)
//    @Mapping(target = "favorites", ignore = true)
//    @Mapping(target = "comments", ignore = true)
//    @Mapping(target = "ratings", ignore = true)
//    @Mapping(target = "location", ignore = true)
//    User toEntity(UnverifiedUser user);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(
            target = "userRole",
            source = "userRole",
            qualifiedByName = "mapUserRoleFromStringToEnum")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    void updateEntityFromDto(@MappingTarget User user, UpdateUserDto userDto);

    @Named("mapUserRoleFromStringToEnum")
    default UserRole mapUserRoleFromStringToEnum(String userRole) {
        return UserRole.valueOf(userRole.toUpperCase(Locale.ROOT));
    }

    @Named("mapAdsToIds")
    default List<Long> mapAdsToIds(List<Ad> ads) {
        if (ads == null) {
            ads = Collections.emptyList();
        }
        return ads.stream().map(Ad::getId).collect(Collectors.toList());
    }
}
