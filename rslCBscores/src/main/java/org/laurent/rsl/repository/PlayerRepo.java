package org.laurent.rsl.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.laurent.rsl.model.Player;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Long> {

    Player findByName(String name);

}

