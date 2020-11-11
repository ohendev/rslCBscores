package org.laurent.rsl.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Enumerated;
import javax.persistence.GenerationType;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter
@Getter
public class CBresults {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;
    private Double score;

    @Override
    public String toString() {
        return "CBresults{" +
                "dateTime=" + dateTime +
                ", difficulty=" + difficulty +
                ", score=" + score +
                '}';
    }
}
