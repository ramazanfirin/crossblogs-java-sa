package com.crossover.techtrial.service;

import java.util.List;
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

}
