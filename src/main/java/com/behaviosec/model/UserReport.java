package com.behaviosec.model;

import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "userreport")
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;
    @Column(name = "username")
    @NotEmpty(message = "*Please provide a Username")
    private String username;
     @Column(name = "other")
    private String other;


    @java.lang.SuppressWarnings("all")
    public static class UserReportBuilder {
        @java.lang.SuppressWarnings("all")
        private int id;
        @java.lang.SuppressWarnings("all")
        private String username;
        @java.lang.SuppressWarnings("all")
        private String other;
 
        @java.lang.SuppressWarnings("all")
        UserReportBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public UserReportBuilder id(final int id) {
            this.id = id;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public UserReportBuilder username(final String username) {
            this.username = username;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public UserReportBuilder other(final String other) {
            this.other = other;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public UserReport build() {
            return new UserReport(id, username, other);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "UserReport.UserBuilder(id=" + this.id + ", username=" + this.username + ", other=" + this.other + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static UserReportBuilder builder() {
        return new UserReportBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public int getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public String getUsername() {
        return this.username;
    }

    @java.lang.SuppressWarnings("all")
    public String getOther() {
        return this.other;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final int id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setusername(final String username) {
        this.username = username;
    }

    @java.lang.SuppressWarnings("all")
    public void setOther(final String other) {
        this.other = other;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof UserReport)) return false;
        final UserReport other = (UserReport) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final java.lang.Object this$username = this.getUsername();
        final java.lang.Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final java.lang.Object this$other = this.getOther();
        final java.lang.Object other$other = other.getOther();
        if (this$other == null ? other$other != null : !this$other.equals(other$other)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof UserReport;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final java.lang.Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final java.lang.Object $other = this.getOther();
        result = result * PRIME + ($other == null ? 43 : $other.hashCode());
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "UserReport(id=" + this.getId() + ", username=" + this.getUsername() + ", other=" + this.getOther() + ")";
    }

    @java.lang.SuppressWarnings("all")
    public UserReport(final int id, final String username, final String other) {
        this.id = id;
        this.username = username;
        this.other = other;
    }

    @java.lang.SuppressWarnings("all")
    public UserReport() {
    }
}
