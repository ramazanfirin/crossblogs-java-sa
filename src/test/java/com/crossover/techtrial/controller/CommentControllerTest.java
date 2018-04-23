package com.crossover.techtrial.controller;

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
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.crossover.techtrial.model.Comment;
import com.crossover.techtrial.repository.CommentRepository;
import com.crossover.techtrial.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class CommentControllerTest {

  @Autowired
  private TestRestTemplate template;

  private MockMvc restUseRecordMockMvc;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private GlobalExceptionHandler exceptionTranslator;
  
  @Autowired
  private EntityManager em;
  
  @Autowired 
  private CommentController commentController;
  
  @Autowired 
  private CommentRepository commentRepository;
    
  private Comment comment;
  
  private static final String DEFAULT_TITLE = "AAAAAAAAAA";
  private static final String UPDATED_TITLE = "BBBBBBBBBB";
  private static final String DEFAULT_EMAIL = "ramzan@adsfai.com";
  private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL = "bbbb@adsfai.com";
  private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now();
  private static final Boolean DEFAULT_PUBLISHED = Boolean.TRUE;
  
  
  @Before
  public void setup() throws Exception {
	  MockitoAnnotations.initMocks(this);
      this.restUseRecordMockMvc = MockMvcBuilders.standaloneSetup(commentController)
         .setControllerAdvice(exceptionTranslator)
         .build();
  }

  public static Article createArticle(EntityManager em) {
	  Article article = new Article();
      article.setContent(DEFAULT_CONTENT);
	  article.setDate(DEFAULT_DATE);
	  article.setEmail(DEFAULT_EMAIL);
	  article.setPublished(DEFAULT_PUBLISHED);
	  article.setTitle(DEFAULT_TITLE);
	  
	  return article;
  }
  
  public static Comment createEntity(EntityManager em) {
	  Comment comment = new Comment();
      comment.setDate(LocalDateTime.now());
      comment.setEmail(DEFAULT_EMAIL);
	  comment.setMessage(DEFAULT_CONTENT); 
	  
	  Article article = new Article();
      article.setContent(DEFAULT_CONTENT);
	  article.setDate(DEFAULT_DATE);
	  article.setEmail(DEFAULT_EMAIL);
	  article.setPublished(DEFAULT_PUBLISHED);
	  article.setTitle(DEFAULT_TITLE);
	  
	  comment.setArticle(article);
	  
	  return comment;
  }

  @Before
  public void initTest() {
	  comment = createEntity(em);
  }
   
  @Test
  public void createComment() throws Exception {
      int databaseSizeBeforeCreate = commentRepository.findAll().size();

      restUseRecordMockMvc.perform(post("/comments")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(comment)))
          .andExpect(status().isCreated());

      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
      Comment testComment = commentList.get(commentList.size() - 1);
      assertThat(testComment.getEmail()).isEqualTo(DEFAULT_EMAIL);
  }
  
  @Test
  public void createCommentExistingId() throws Exception {
      int databaseSizeBeforeCreate = commentRepository.findAll().size();
      comment.setId(1L);

      restUseRecordMockMvc.perform(post("/comments")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(comment)))
          .andExpect(status().isBadRequest());
      
      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeCreate);
  }
  
  @Test
  public void checkEmailRequired() throws Exception {
      int databaseSizeBeforeTest = commentRepository.findAll().size();
      comment.setEmail(null);

      restUseRecordMockMvc.perform(post("/comments")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(comment)))
          .andExpect(status().isBadRequest());

      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeTest);
  }
  
  
  
  @Test
  public void checkMessageLength() throws Exception {
      int databaseSizeBeforeTest = commentRepository.findAll().size();
      StringBuffer temp=new StringBuffer();
      for (int i = 0; i < 35000; i++) {
    	  temp.append(i);
	}
      
      comment.setMessage(temp.toString());

      restUseRecordMockMvc.perform(post("/comments")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(comment)))
          .andExpect(status().isBadRequest());

      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeTest);
  }
  
  @Test
  public void getComment() throws Exception {

	  commentRepository.saveAndFlush(comment);
	  
	  restUseRecordMockMvc.perform(get("/comments/{comments-id}", comment.getId()))
          .andExpect(status().isOk())
          .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
          .andExpect(jsonPath("$.article.email").value(DEFAULT_EMAIL.toString()))
          .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
  }
  
  @Test
  public void getNonExistingComment() throws Exception {
	  restUseRecordMockMvc.perform(get("/comments/{comments-id}", Long.MAX_VALUE))
          .andExpect(status().isNotFound());
  }
  
  @Test
  public void updateComment() throws Exception {
      
	  commentRepository.saveAndFlush(comment);
      int databaseSizeBeforeUpdate = commentRepository.findAll().size();

      Optional<Comment> updatedComment = commentRepository.findById(comment.getId());
      updatedComment.orElseGet(null).setEmail(UPDATED_EMAIL);

      restUseRecordMockMvc.perform(put("/comments")
          .contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(updatedComment.get())))
          .andExpect(status().isOk());

      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
      Comment testComment = commentList.get(commentList.size() - 1);
      assertThat(testComment.getEmail()).isEqualTo(UPDATED_EMAIL);
  }
  
  @Test
  @Transactional
  public void deleteComment() throws Exception {
      commentRepository.saveAndFlush(comment);
      int databaseSizeBeforeDelete = commentRepository.findAll().size();

      restUseRecordMockMvc.perform(delete("/comments/{comments-id}", comment.getId())
          .accept(TestUtil.APPLICATION_JSON_UTF8))
          .andExpect(status().isOk());

      List<Comment> commentList = commentRepository.findAll();
      assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
  }
  
}
