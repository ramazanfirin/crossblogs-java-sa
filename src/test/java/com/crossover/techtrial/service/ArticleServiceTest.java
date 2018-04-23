package com.crossover.techtrial.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.exceptions.GlobalExceptionHandler;
import com.crossover.techtrial.exceptions.TestUtil;
import com.crossover.techtrial.model.Article;
import com.crossover.techtrial.repository.ArticleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class ArticleServiceTest {

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private EntityManager em;
    
  private Article article;
  
  @Autowired
  ArticleService articleService;
  
  private static final String DEFAULT_TITLE = "AAAAAAAAAA";
  private static final String UPDATED_TITLE = "BBBBBBBBBB";

  private static final String DEFAULT_EMAIL = "ramzan@adsfai.com";
  private static final String UPDATED_CLAZZ = "ramzanasdasd@adsfai.com";
  
  private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
  private static final String UPDATED_CONTENT = "BBBBBBBBBB";
  
  private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now();
  private static final LocalDateTime UPDATED_DATE = LocalDateTime.now();
  
  private static final Boolean DEFAULT_PUBLISHED = Boolean.TRUE;
  private static final Boolean UPDATED_PUBLISHED = Boolean.FALSE;
  
  
  @Before
  public void setup() throws Exception {

  }

  public static Article createEntity(EntityManager em) {
	  Article article = new Article();
      article.setContent(DEFAULT_CONTENT);
	  article.setDate(DEFAULT_DATE);
	  article.setEmail(DEFAULT_EMAIL);
	  article.setPublished(DEFAULT_PUBLISHED);
	  article.setTitle(DEFAULT_TITLE);
	  
	  return article;
  }

  @Before
  public void initTest() {
	  article = createEntity(em);
  }

  
  @Test
  public void createArticle() throws Exception {
      Article articleTemp = articleService.save(article);
      assertThat(articleTemp.getTitle()).isEqualTo(DEFAULT_TITLE);
  }
  
  
}
