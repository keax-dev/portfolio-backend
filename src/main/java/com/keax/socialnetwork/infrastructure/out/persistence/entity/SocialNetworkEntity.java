package com.keax.socialnetwork.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "social_network")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialNetworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_network_id")
    private Long socialNetworkId;

    @Column(name = "social_network_name", unique = true, nullable = false)
    private String socialNetworkName;

    @Column(name = "social_network_icon", nullable = false)
    private String socialNetworkIcon;

    @Column(name = "social_network_color", nullable = false)
    private String socialNetworkColor;

    @Column(name = "social_network_position", nullable = false)
    private int socialNetworkPosition;

    @Column(name = "social_network_url", nullable = false)
    private String socialNetworkUrl;

    @Column(name = "social_network_deleted", nullable = false)
    private Boolean socialNetworkDeleted;

}
