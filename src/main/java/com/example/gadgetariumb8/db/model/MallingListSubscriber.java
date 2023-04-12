package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "malling_list_subscribers")
@NoArgsConstructor
@AllArgsConstructor
public class MallingListSubscriber {
    @Id
    @SequenceGenerator(name = "malling_list_subscriber_gen",sequenceName = "malling_list_subscriber_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "malling_list_subscriber_gen")
    private Long id;
    @ElementCollection
    private List<String> usersEmails;
}