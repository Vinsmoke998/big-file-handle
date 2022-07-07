package com.example.demo.model.entity.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "code")
    private String code;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    @Column(name = "modified_date")
    private Date modifiedDate;

}
