package org.laurent.rsl.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter
@Getter
public class Player {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    protected List<BDCResult> results;

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", results=" + displayResults(results) +
                '}';
    }

    private String displayResults(List<BDCResult> playerResults) {
        StringBuilder sb = new StringBuilder("result on: ");
        for (BDCResult result : playerResults) {
            sb.append(result);
        }

        return sb.toString();
    }
}

