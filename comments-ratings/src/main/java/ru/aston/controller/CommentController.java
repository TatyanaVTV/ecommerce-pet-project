package ru.aston.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.dto.CommentDto;
import ru.aston.dto.NewCommentDto;
import ru.aston.dto.UpdateCommentDto;
import ru.aston.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{prodId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable Long prodId,
            @RequestParam Long userId,
            @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.createComment(userId, prodId, newCommentDto);
    }

    @GetMapping
    public List<CommentDto> getCommentsByProductId(@PathVariable Long prodId) {
        return commentService.getCommentsByProductId(prodId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return commentService.updateComment(commentId, updateCommentDto);
    }
}