package kr.pe.advenoh.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Setter
@Entity
@NoArgsConstructor
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
}