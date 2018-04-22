Challenge Statement

Cross-blogs is a backend blogging application written by a startup company called WritingForAll. It allows users to create / update / delete their articles, accepting comments for each article.

Notes:
- Articles have a 120 character limit for their title, and a 32k limit for their body.
- The frontend application is excluded from the current scope. It is a separate, fully-functional application handled by another team, so we do not want to modify it by performing any API modifications.


Your tasks:
- Increase unit test coverage to reach 70%, 
achieving more than 70% will only consume your valuable time without extra score.
- Find bugs and fix them, hint: we provided Cross-Blogs application in a good structure, so no need to spend your valuable time on structure modifications,  just focus on fixing bugs.
- Articles search title endpoint is very slow, please optimize it.

Prerequisites:
	Any IDE
	Java 8
    MySQL 5.5+


Development Environment:
  Mysql:
    Crossblogs applications require MYSQL database to store its data. Make sure to update application.properties with spring.datasource.url(change hostname only), spring.datasource.username, and  spring.datasource.password.
  Crossblogs Application:
    To start the application run CrossBlogsApplication.java main method from your IDE
    and to check that it is running open http://localhost:8080/articles/1 from your browser.


Production Environment:
  This is how we are going to run and evaluate your submission, so please make sure to run below steps before submiting your answer.

  1) Make sure to run unit tests,check code coverage, ensure application is compiling and all dependencies are included.
  2) Remove any build directories, compress whole project, and rename zipfile to be crossblogs-java-<YOUR-FULL-NAME>.zip. Please use dash "-" instead of any space



