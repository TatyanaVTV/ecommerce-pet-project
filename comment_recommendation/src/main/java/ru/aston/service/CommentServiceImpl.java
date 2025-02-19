package ru.aston.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aston.dto.CommentDto;
import ru.aston.dto.NewCommentDto;
import ru.aston.dto.UpdateCommentDto;
import ru.aston.entity.Comment;
import ru.aston.entity.Score;
import ru.aston.exception.ForbiddenException;
import ru.aston.mapper.CommentMapper;
import ru.aston.repository.CommentRepository;
import ru.aston.repository.ScoreRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ScoreRepository scoreRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long prodId, NewCommentDto newCommentDto) {
        log.info("Создание комментария для продукта {} пользователем {}", prodId, userId);

        if (newCommentDto.getRating() != null) {
            Optional<Score> optionalScore = scoreRepository.findByUserIdAndProductId(userId, prodId);
            Score score = optionalScore.orElseGet(() -> {
                Score newScore = new Score();
                newScore.setProductId(prodId);
                newScore.setUserId(userId);
                return newScore;
            });
            score.setRating(newCommentDto.getRating());
            scoreRepository.save(score);
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setProductId(prodId);
        comment.setComment(newCommentDto.getComment());
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Комментарий успешно создан: {}", savedComment);

        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long commentId, UpdateCommentDto updateCommentDto) {
        log.info("Обновление комментария с ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ForbiddenException("Комментарий не найден"));

        if (updateCommentDto.getComment() != null) {
            comment.setComment(updateCommentDto.getComment());
        }

        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        log.info("Комментарий успешно обновлен: {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        log.info("Получение комментария по ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ForbiddenException("Комментарий не найден"));
        log.info("Комментарий успешно получен: {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByProductId(Long productId) {
        log.info("Получение комментариев для продукта с ID: {}", productId);
        List<Comment> comments = commentRepository.findByProductId(productId);
        log.info("Комментарии успешно получены: {}", comments);
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Удаление комментария с ID: {}", commentId);
        if (!commentRepository.existsById(commentId)) {
            throw new ForbiddenException("Комментарий не найден");
        }
        commentRepository.deleteById(commentId);
        log.info("Комментарий {} успешно удален", commentId);
    }
}