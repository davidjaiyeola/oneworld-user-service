package com.oneworld.accuracy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private UserStatus status;
    @NonNull
    private UserRole role;
    private String title;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    @NonNull
    private String email;
    private String mobile;
    //@TODO save encrypted
    private String password;
    private boolean Verified;
    private Date dateRegistered;
    private Date dateVerified;
    private Date dateDeactivated;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    public String getFullName(){
        return this.getFirstname() +" " + this.getLastname();
    }
}
