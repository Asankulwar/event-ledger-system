package com.eventledger.eventgateway.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @NotBlank
    private String eventId;

    @NotBlank
    private String accountId;

    @NotBlank
    @Pattern(regexp = "CREDIT|DEBIT")
    private String type;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotNull
    private Instant eventTimestamp;

    // store metadata as JSON string
    private String metadataJson;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Instant getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(Instant eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getMetadataJson() {
		return metadataJson;
	}

	public void setMetadataJson(String metadataJson) {
		this.metadataJson = metadataJson;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", accountId=" + accountId + ", type=" + type + ", amount=" + amount
				+ ", currency=" + currency + ", eventTimestamp=" + eventTimestamp + ", metadataJson=" + metadataJson
				+ ", getEventId()=" + getEventId() + ", getAccountId()=" + getAccountId() + ", getType()=" + getType()
				+ ", getAmount()=" + getAmount() + ", getCurrency()=" + getCurrency() + ", getEventTimestamp()="
				+ getEventTimestamp() + ", getMetadataJson()=" + getMetadataJson() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

    // getters and setters
}
