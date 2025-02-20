package ru.aston.service;

import ru.aston.dto.CommentDto;
import ru.aston.dto.NewCommentDto;
import ru.aston.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long userId, Long productId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long commentId, UpdateCommentDto updateCommentDto);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getCommentsByProductId(Long productId);

    void deleteComment(Long commentId);
}