package com.invoicegenerator.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "UUID")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "companyClient_id")
    private Company companyClient;

    @Column(name = "dataCriacao")
    private LocalDateTime dataCriacao;

    @Column(name = "invoiceNumber")
    private String invoiceNumber;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<InvoiceItem> items;

    @Column(name = "comments")
    private String comments;

    @Column(name = "tax")
    private int tax;

    @Column(name = "anotherValue")
    private int anotherValue;

    private double subtotal;

    private double taxAmount;

    private double total;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

}
