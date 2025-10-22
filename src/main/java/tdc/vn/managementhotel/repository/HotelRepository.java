package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationId(Long locationId);
    List<Hotel> findByUserId(Long userId);

    //chức năng tìm kiem

    // tìm theo tên khách sạn
    List<Hotel> findByNameContainingIgnoreCase(String name);

    // Tìm theo trạng thái (ví dụ: "GẦN BIỂN", "GẦN TRUNG TÂM")
    List<Hotel> findByStatusContainingIgnoreCase(String status);

    // Tìm theo tên thành phố (location.name)
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.location.name) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Hotel> findByCity(@Param("city") String city);

    // Tìm theo khoảng giá phòng (minPrice - maxPrice)
    @Query("""
SELECT h FROM Hotel h
WHERE EXISTS (
    SELECT 1 FROM Room r
    WHERE r.hotel.id = h.id
      AND r.price BETWEEN :minPrice AND :maxPrice
)
""")
    List<Hotel> findByRoomPriceBetween(@Param("minPrice") BigDecimal minPrice,
                                       @Param("maxPrice") BigDecimal maxPrice);

    // Tìm kết hợp: theo tên + thành phố + trạng thái + giá
    @Query("""
SELECT h FROM Hotel h
WHERE 
  (:name IS NULL OR TRIM(:name) = '' OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
  AND (:city IS NULL OR TRIM(:city) = '' OR (h.location IS NOT NULL AND LOWER(h.location.name) LIKE LOWER(CONCAT('%', :city, '%'))))
  AND (:status IS NULL OR TRIM(:status) = '' OR LOWER(h.status) LIKE LOWER(CONCAT('%', :status, '%')))
  AND (
       (:minPrice IS NULL OR :maxPrice IS NULL)
       OR EXISTS (
           SELECT 1 FROM Room r 
           WHERE r.hotel.id = h.id
             AND (:minPrice IS NULL OR r.price >= :minPrice)
             AND (:maxPrice IS NULL OR r.price <= :maxPrice)
       )
  )
""")
    List<Hotel> searchHotels(
            @Param("name") String name,
            @Param("city") String city,
            @Param("status") String status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );





}
