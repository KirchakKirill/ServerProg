package com.example.GameREST.Service.GameService.DataPackage;

import com.example.GameREST.Entity.GenreEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GamePackage
{
   private String gameName;
   private GenreEntity genre;
}
