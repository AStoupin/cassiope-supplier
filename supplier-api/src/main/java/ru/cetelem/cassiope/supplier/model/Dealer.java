package ru.cetelem.cassiope.supplier.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DEALER")
public class Dealer {

    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)
    private int id;

    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "dealer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DealerLimit limit;

    public Dealer() {
        limit = new DealerLimit(this);
    }

    public Dealer(String code, String name) {
        this.code = code;
        this.name = name;
        limit = new DealerLimit(this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.code == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Dealer dealer = (Dealer) o;
        if (dealer.code == null) return false;

        return this.code.equals(dealer.code);
    }

    public DealerLimit getLimit() {
        return limit;
    }

    public void setLimit(DealerLimit limit) {
        this.limit = limit;
    }

}
