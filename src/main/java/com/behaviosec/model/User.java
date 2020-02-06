package com.behaviosec.model;

import org.hibernate.validator.constraints.Length;

import com.behaviosec.validator.Phone;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int id;
	@Column(name = "username")
	@Length(min = 2, message = "*Please provide a valid Username (8 characters)")
	@NotEmpty(message = "*Please provide a Username")
	private String username;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "phone")
    @Phone(message = "*Please provide a valid phone number")
    @NotEmpty(message = "*Please provide a phone number")
    private String phone;
	@Column(name = "password")
	@Length(min = 8, message = "*Your password must have at least 8 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;
	@Column(name = "name")
	@NotEmpty(message = "*Please provide your first name")
	private String name;
	@Column(name = "last_name")
	@NotEmpty(message = "*Please provide your last name")
	private String lastName;
	@Column(name = "active")
	private int active;
	@Column(name = "isUsing2FA")
	private int isUsing2FA;
	@Column(name = "secret")
	private String secret;
	@Column(name = "google_oauth_token")
	private String googleOauthToken;
	@Column(name = "other", columnDefinition = "TEXT")
	private String other;
	@Column(name = "invitationKey")
	@NotEmpty(message = "*Please provide an invitation Key")
	private String invitationKey;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
}
