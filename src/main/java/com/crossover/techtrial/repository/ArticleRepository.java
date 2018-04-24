package com.crossover.techtrial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Article;

@RepositoryRestResource(exported = false)
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>,JpaRepository<Article,Long>  {

  List<Article> findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title,
      String content);

  /*
   * For string columns, MySQL indexes the left side of a string. 
   * That means an index can speed a like query that has a wildcard on the right side:
   * 
   * for title and content field, creating indexes change log  will added. 
   */
  @Query("select a from Article a where a.title like :keyword ||'%' or a.content like :keyword ||'%'")
  List<Article> findAutoCompleteOptions(@Param("keyword") String keyword);
}
