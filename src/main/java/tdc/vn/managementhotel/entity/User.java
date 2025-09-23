package tdc.vn.managementhotel.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Getter
@Setter
public class User {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @Column(nullable = false, unique = true)
    private String username; // tên đăng nhập (Primary Key)
    
    @JsonIgnore  // ⛔️ Ẩn khi trả JSON
    @Column(nullable = false)
    private String password; // mật khẩu (nên lưu dạng mã hoá BCrypt)

    @JsonIgnore  // ⛔️ Ẩn khi trả JSON
    @Column(nullable = false)
    private String roles; // danh sách quyền (dạng chuỗi, phân cách bằng dấu phẩy): "ROLE_USER,ROLE_ADMIN"
    
    
    public String getUsername() { return username; }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
	public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}