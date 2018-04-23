package com.crossover.techtrial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Article;

@RepositoryRestResource(exported = false)
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>,JpaRepository<Article,Long>  {

  List<Article> findTop10ByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title,
      String content);

}
