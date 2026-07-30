package com.keax.socialnetwork.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.jdbc.Expectation;

@Entity
@Table(name = "social_network")
@SQLDelete(
        sql = "UPDATE social_network SET social_network_deleted = true, version = version + 1 "
                + "WHERE social_network_id = ? AND version = ?",
        verify = Expectation.RowCount.class
)
@SQLRestriction("social_network_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialNetworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_network_id")
    private Long socialNetworkId;

    @Column(name = "social_network_name", nullable = false)
    private String socialNetworkName;

    @Column(name = "social_network_icon", nullable = false)
    private String socialNetworkIcon;

    @Column(name = "social_network_color", nullable = false)
    private String socialNetworkColor;

    @Column(name = "social_network_position", nullable = false)
    private int socialNetworkPosition;

    @Column(name = "social_network_url", nullable = false, length = 2048)
    private String socialNetworkUrl;

    @Column(name = "social_network_deleted", nullable = false)
    private Boolean socialNetworkDeleted = false;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public SocialNetworkEntity(
            Long socialNetworkId,
            String socialNetworkName,
            String socialNetworkIcon,
            String socialNetworkColor,
            int socialNetworkPosition,
            String socialNetworkUrl,
            Boolean socialNetworkDeleted
    ) {
        this(
                socialNetworkId,
                socialNetworkName,
                socialNetworkIcon,
                socialNetworkColor,
                socialNetworkPosition,
                socialNetworkUrl,
                socialNetworkDeleted,
                null
        );
    }

}
