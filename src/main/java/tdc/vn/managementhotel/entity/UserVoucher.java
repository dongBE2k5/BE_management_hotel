package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_voucher")
public class UserVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "voucher_id")
    private Long voucherId;

    // 🧩 Bắt buộc phải có constructor rỗng
    public UserVoucher() {}

    public UserVoucher(Long userId, Long voucherId) {
        this.userId = userId;
        this.voucherId = voucherId;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }
}
