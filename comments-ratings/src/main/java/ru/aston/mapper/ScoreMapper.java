package ru.aston.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.ScoreDto;
import ru.aston.entity.Score;

@Mapper(componentModel = "spring")
public interface ScoreMapper {

    ScoreMapper INSTANCE = Mappers.getMapper(ScoreMapper.class);

    @Mapping(target = "score", expression = "java(score.getTotalRatings() == 0 " +
            "? 0.0 " +
            ": (double) score.getSumRatings() / score.getTotalRatings())")
    ScoreDto toDto(Score score);
}