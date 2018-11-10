package ru.itis.dockerweb.contracthandler.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Bulat Giniyatullin
 * 10 November 2018
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "contract")
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long interval;
}
