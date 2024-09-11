package enerhi.boardservice;

import enerhi.boardservice.domain.Posts;
import enerhi.boardservice.repository.PostsRepository;
import enerhi.boardservice.service.PostsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardserviceApplication.class, args);
	}

}
