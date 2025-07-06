package com.example.GameREST.Utils;

import com.example.GameREST.DTO.*;
import com.example.GameREST.Entity.*;

public class ConvertToDTO {

    public static PublisherDTO convertToPublisherDTO(PublisherEntity publisher)
    {
        return PublisherDTO.builder()
                .id(publisher.getId())
                .publisherName(publisher.getPublisherName())
                .build();
    }

    public static PlatformDTO convertToPlatformDTO(PlatformEntity platform)
    {
        return PlatformDTO.builder()
                .platformName(platform.getPlatformName())
                .id(platform.getId())
                .build();
    }

    public static GenreDTO convertToGenreDTO(GenreEntity genre)
    {
        return GenreDTO.builder()
                .genreName(genre.getGenreName())
                .id(genre.getId())
                .build();
    }

    public static RegionDTO convertToRegionDTO(RegionEntity region)
    {
        return RegionDTO.builder()
                .id(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public static RegionSalesDTO convertToRegionSalesDTO(RegionSalesEntity regionSalesEntity) {
        return RegionSalesDTO.builder()
                .regionId(regionSalesEntity.getId().getRegionId())
                .gamePlatformId(regionSalesEntity.getId().getGamePlatformId())
                .numSales(regionSalesEntity.getNumSales())
                .build();
    }

    public static ReadOnlyUserDTO convertToReadOnlyUserDTO(UserEntity user)
    {
        var authorities = user.getAuthorities().stream().map(UserAuthority::getAuthority).toList();
        return ReadOnlyUserDTO.builder()
                .username(user.getUsername())
                .id(user.getId())
                .authority(authorities.toString())
                .build();
    }

}
