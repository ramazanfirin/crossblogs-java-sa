package com.crossover.techtrial.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
public class ArticleControllerTest {

  @Autowired
  private TestRestTemplate template;

  private MockMvc restUseRecordMockMvc;
  
  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private GlobalExceptionHandler exceptionTranslator;
  
  @Autowired
  private EntityManager em;
  
  @Autowired 
  private ArticleController articleController;
    
  private Article article;
  
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
	  MockitoAnnotations.initMocks(this);
      this.restUseRecordMockMvc = MockMvcBuilders.standaloneSetup(articleController)
          .setControllerAdvice(exceptionTranslator)
          .build();
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
      int databaseSizeBeforeCreate = articleRepository.findAll().size();

      restUseRecordMockMvc.perform(post("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(article)))
          .andExpect(status().isCreated());

      List<Article> articleList = articleRepository.findAll();
      assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
      Article testArticle = articleList.get(articleList.size() - 1);
      assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
  }
  
  @Test
  public void createArticleExistingId() throws Exception {
      int databaseSizeBeforeCreate = articleRepository.findAll().size();
      article.setId(1L);

      restUseRecordMockMvc.perform(post("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(article)))
          .andExpect(status().isBadRequest());
      
      List<Article> articleList = articleRepository.findAll();
      assertThat(articleList).hasSize(databaseSizeBeforeCreate);
  }
  
  @Test
  @Transactional
  public void checkEmailRequired() throws Exception {
      int databaseSizeBeforeTest = articleRepository.findAll().size();
      article.setEmail(null);

      restUseRecordMockMvc.perform(post("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(article)))
          .andExpect(status().isBadRequest());

      List<Article> articleList = articleRepository.findAll();
      assertThat(articleList).hasSize(databaseSizeBeforeTest);
  }
  
  @Test
  public void checkTitleLength() throws Exception {
      int databaseSizeBeforeTest = articleRepository.findAll().size();
      StringBuffer temp=new StringBuffer();
      for (int i = 0; i < 150; i++) {
    	  temp.append(i);
	}
      
      article.setTitle(temp.toString());

      restUseRecordMockMvc.perform(post("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(article)))
          .andExpect(status().isBadRequest());

      List<Article> articleList = articleRepository.findAll();
      assertThat(articleList).hasSize(databaseSizeBeforeTest);
  }
  
  @Test
  public void checkContentLength() throws Exception {
      int databaseSizeBeforeTest = articleRepository.findAll().size();
      StringBuffer temp=new StringBuffer();
      for (int i = 0; i < 35000; i++) {
    	  temp.append(i);
	}
      
      article.setContent(temp.toString());

      restUseRecordMockMvc.perform(post("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(article)))
          .andExpect(status().isBadRequest());

      List<Article> articleList = articleRepository.findAll();
      assertThat(articleList).hasSize(databaseSizeBeforeTest);
  }
  
  @Test
  public void getArticle() throws Exception {

	  articleRepository.saveAndFlush(article);
	  
	  restUseRecordMockMvc.perform(get("/articles/{article-id}", article.getId()))
          .andExpect(status().isOk())
          .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(jsonPath("$.id").value(article.getId().intValue()))
          .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
  }
  
  @Test
  public void getNonExistingArticle() throws Exception {
	  restUseRecordMockMvc.perform(get("/articles/{article-id}", Long.MAX_VALUE))
          .andExpect(status().isNotFound());
  }
  
  @Test
  public void updateArticle() throws Exception {
      
	  articleRepository.saveAndFlush(article);
      int databaseSizeBeforeUpdate = articleRepository.findAll().size();

      Optional<Article> updatedArticle = articleRepository.findById(article.getId());
      updatedArticle.orElseGet(null).setTitle(UPDATED_TITLE);

      restUseRecordMockMvc.perform(put("/articles")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(updatedArticle.get())))
          .andExpect(status().isOk());

      List<Article> cityList = articleRepository.findAll();
      assertThat(cityList).hasSize(databaseSizeBeforeUpdate);
      Article testCity = cityList.get(cityList.size() - 1);
      assertThat(testCity.getTitle()).isEqualTo(UPDATED_TITLE);
  }
  
  @Test
  public void deleteArticle() throws Exception {
      articleRepository.saveAndFlush(article);
      int databaseSizeBeforeDelete = articleRepository.findAll().size();

      restUseRecordMockMvc.perform(delete("/articles/{article-id}", article.getId())
          .accept(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(status().isOk());

      List<Article> cityList = articleRepository.findAll();
      assertThat(cityList).hasSize(databaseSizeBeforeDelete - 1);
  }
  
  @Test
  public void getAllArticles() throws Exception {
      articleRepository.saveAndFlush(article);
      restUseRecordMockMvc.perform(get("/articles/"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
          .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
  }
  
  @Test
  public void search() throws Exception {
      articleRepository.saveAndFlush(article);
      restUseRecordMockMvc.perform(get("/articles/search?text=AAAA"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
          .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
  }
  
}
