package kr.pe.advenoh.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Deprecated
@Entity
@Table(name = "login_history")
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_history_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "device_detail")
    private String deviceDetail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_loggedin", nullable = false)
    private Date lastLoggedIn;

//    @Column(name = "user_id")
//    private Long user_id;

    @Builder
    public LoginHistory(String deviceDetail, Date lastLoggedIn) {
        this.deviceDetail = deviceDetail;
        this.lastLoggedIn = lastLoggedIn;
    }
}