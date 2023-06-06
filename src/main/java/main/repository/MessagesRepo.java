package main.repository;

import main.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepo extends JpaRepository<Messages, Long> {

}
