package org.lemonframework.generator.sample.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Person implements Serializable {
    /**
     * 序列号.
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * id.
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * name.
     *
     * @mbg.generated
     */
    private String name;

    /**
     * createtime.
     *
     * @mbg.generated
     */
    private Date createtime;

    /**
     * income.
     *
     * @mbg.generated
     */
    private BigDecimal income;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}