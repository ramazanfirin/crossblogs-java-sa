package com.crossover.techtrial.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.model.Article;
import com.crossover.techtrial.model.Comment;
import com.crossover.techtrial.service.ArticleService;
import com.crossover.techtrial.service.CommentService;

@RestController
public class CommentController {
  
  @Autowired
  CommentService commentService;

  @PostMapping(path = "comments")
  public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
	
	  if (comment.getId() != null) {
          return ResponseEntity.badRequest().header("ErrorHeader","There is id in request").body(null);
    }
	
	return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
  }

  @GetMapping(path = "comments/{comments-id}")
  public ResponseEntity<Comment> getCommentById(@PathVariable("comments-id") Long id) {
    Comment comment = commentService.findByIdWithRealtions(id);
    if (comment != null)
      return new ResponseEntity<>(comment, HttpStatus.OK);
    
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping(path = "comments")
  public ResponseEntity<Comment> updateComment(@Valid @RequestBody Comment comment) {
	  
	  if (comment.getId() == null) {
          return ResponseEntity.badRequest().header("ErrorHeader","There is no id in request").body(null);
      }
	  return new ResponseEntity<>(commentService.save(comment), HttpStatus.OK);
  }

  @DeleteMapping(path = "comments/{comment-id}")
  public ResponseEntity<Article> deleteArticleById(@PathVariable("comment-id") Long id) {
    commentService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
