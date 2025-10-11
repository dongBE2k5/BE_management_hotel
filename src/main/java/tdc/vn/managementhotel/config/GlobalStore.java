package tdc.vn.managementhotel.config;


import org.springframework.stereotype.Component;

@Component
public class GlobalStore {
    private String value;

    public synchronized void setValue(String value) {
        this.value = value;
    }

    public synchronized String getValueAndClear() {
        String temp = this.value;
        this.value = null; // xóa sau khi lấy
        return temp;
    }

}
