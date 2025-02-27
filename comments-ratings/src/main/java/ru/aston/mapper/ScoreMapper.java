package ru.aston.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.ScoreDto;
import ru.aston.entity.Score;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreMapper INSTANCE = Mappers.getMapper(ScoreMapper.class);

    ScoreDto toDto(Score score);
}
