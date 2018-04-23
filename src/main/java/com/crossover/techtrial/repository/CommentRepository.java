package com.crossover.techtrial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Comment;

@RepositoryRestResource(exported = false)
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>,JpaRepository<Comment, Long> {

  @Override
  List<Comment> findAll();

  @Query("select c from Comment c left join fetch c.article where c.id=:commentId")
  Comment findByIdWithRealtions(@Param(value = "commentId") Long commentId);
  
  List<Comment> findByArticleIdOrderByDate(Long articleId);
}
