package ru.cetelem.cassiope.supplier.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEALER_LIMIT")
public class DealerLimit {
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)
    private int id;
	@OneToMany(mappedBy = "limit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.JOIN)
	private List<PayloadItem> payloadItems = new ArrayList<PayloadItem>();
	@ManyToOne(targetEntity = Dealer.class, optional = false, fetch = FetchType.EAGER)
	private Dealer dealer;
	private BigDecimal hardLimit = BigDecimal.ZERO;
	private BigDecimal softLimit = BigDecimal.ZERO;
	private BigDecimal totalFinanced = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private BigDecimal sublimitDeCc = BigDecimal.ZERO;
	private BigDecimal totalFinancedDeCc = BigDecimal.ZERO;
	private BigDecimal availableAmountDeCc = BigDecimal.ZERO;

    public DealerLimit() {
    }

    public DealerLimit(Dealer dealer) {
        this.dealer = dealer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getHardLimit() {
        return hardLimit;
    }

    public void setHardLimit(BigDecimal hardLimit) {
        this.hardLimit = hardLimit;
    }

    public BigDecimal getSoftLimit() {
        return softLimit;
    }

    public void setSoftLimit(BigDecimal softLimit) {
        this.softLimit = softLimit;
    }

    public BigDecimal getTotalFinanced() {
        return totalFinanced;
    }

    public void setTotalFinanced(BigDecimal totalFinanced) {
        this.totalFinanced = totalFinanced;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getSublimitDeCc() {
        return sublimitDeCc;
    }

    public void setSublimitDeCc(BigDecimal sublimitDeCc) {
        this.sublimitDeCc = sublimitDeCc;
    }

    public BigDecimal getTotalFinancedDeCc() {
        return totalFinancedDeCc;
    }

    public void setTotalFinancedDeCc(BigDecimal totalFinancedDeCc) {
        this.totalFinancedDeCc = totalFinancedDeCc;
    }

    public BigDecimal getAvailableAmountDeCc() {
        return availableAmountDeCc;
    }

    public void setAvailableAmountDeCc(BigDecimal availableAmountDeCc) {
        this.availableAmountDeCc = availableAmountDeCc;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public List<PayloadItem> getPayloadItems() {
        return payloadItems;
    }

    public void setPayloadItems(List<PayloadItem> payloadItems) {
        this.payloadItems = payloadItems;
    }

}
