package com.facewnd.ad.modules.test.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    @Column(name = "NAME")
    private String NAME;

    @Column(name = "age")
    private Double age;

    @Column(name = "CreatedBy")
    private Date createdBy;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return NAME
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * @param NAME
     */
    public void setNAME(String NAME) {
        this.NAME = NAME == null ? null : NAME.trim();
    }

    /**
     * @return age
     */
    public Double getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Double age) {
        this.age = age;
    }

    /**
     * @return CreatedBy
     */
    public Date getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(Date createdBy) {
        this.createdBy = createdBy;
    }
}