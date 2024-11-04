package com.invoicegenerator.invoice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoicegenerator.emitterCompany.EmitterCompany;
import com.invoicegenerator.recipientCompany.RecipientCompany;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_emitter_id")
    private EmitterCompany emitterCompany;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "company_recipient_id")
    private RecipientCompany recipientCompany;


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

    @Column(name = "taxRate")
    private int taxRate;

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
