package tdc.vn.managementhotel.config;


import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GlobalStore {
    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    // Lưu value với key id
    public void setValue(String id, String value) {
        store.put(id, value);
    }

    // Lấy value theo key và xóa luôn
    public String getValueAndClear(String id) {
        String value = store.get(id);
        System.out.print("value"+value);
        store.remove(id);
        return value;
    }
    // Lấy value theo key và xóa luôn
    public String getValue(String id) {
        String value = store.get(id);

        return value;
    }
    // Kiểm tra key có tồn tại không
    public boolean contains(String id) {
        return store.containsKey(id);
    }

    // Xóa tất cả
    public void clear() {
        store.clear();
    }

}
