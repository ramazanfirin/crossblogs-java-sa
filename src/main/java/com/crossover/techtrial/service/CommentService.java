package com.crossover.techtrial.service;

import java.util.List;

import com.crossover.techtrial.model.Article;
import com.crossover.techtrial.model.Comment;

public interface CommentService {

  /*
   * Returns all the Comments related to article along with Pagination information.
   */
  List<Comment> findAll(Long articleId);

  /*
   * Save the default article.
   */
  Comment save(Comment comment);
  
  /*
   * FindById will find the specific user form list.
   * 
   */
  Comment findById(Long id);

  /*
   * Delete a Comment article with id
   */
  void delete(Long id);
  
  Comment findByIdWithRealtions(Long commentId);
  
  

}
