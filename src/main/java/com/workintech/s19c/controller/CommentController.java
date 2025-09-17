package com.workintech.s19c.controller;


import com.workintech.s19c.dto.CommentDto;
import com.workintech.s19c.dto.CommentUpdateDto;
import com.workintech.s19c.entity.Comment;
import com.workintech.s19c.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestParam Long userId, @RequestParam Long tweetId, @RequestBody Comment comment) {

        CommentDto newComment = commentService.createComment(userId, tweetId, comment);
        return ResponseEntity.ok(newComment);

    }

    // PUT /comment/:id
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("id") Long commentId, @RequestBody CommentUpdateDto commentUpdateDto) {

        CommentDto updatedComment = commentService.updateComment(commentId, commentUpdateDto.getContent());
        return ResponseEntity.ok(updatedComment);

    }

    // DELETE /comment/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}