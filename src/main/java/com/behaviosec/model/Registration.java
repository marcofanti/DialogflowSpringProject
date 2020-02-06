package com.behaviosec.model;

import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registration")
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "registration_key")
	private int key;
	@Column(name = "name")
	@Length(min = 8, message = "*Please provide a valid Name (8 characters)")
	@NotEmpty(message = "*Please provide a Name for this registration key")
	private String name;
	@Column(name = "keysecret")
	@Length(min = 8, message = "*Please provide a valid Secret (8 characters)")
	@NotEmpty(message = "*Please provide a Secret")
	private String keysecret;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
	@Column(name = "max")
	@Min(1)
	@Max(20)
	private int max;
	@Column(name = "other", columnDefinition = "TEXT")
	private String other;
}
