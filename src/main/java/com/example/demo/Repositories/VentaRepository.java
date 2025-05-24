package com.example.demo.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fecha >= :inicio AND v.fecha < :fin")
    long contarVentasHoy(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fecha >= :inicio AND v.fecha < :fin")
    double obtenerIngresosHoy(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(v) FROM Venta v WHERE EXTRACT(MONTH FROM v.fecha) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) AND EXTRACT(YEAR FROM v.fecha) = EXTRACT(YEAR FROM CURRENT_TIMESTAMP)")
    long contarVentasMes();

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE EXTRACT(MONTH FROM v.fecha) = EXTRACT(MONTH FROM CURRENT_TIMESTAMP) AND EXTRACT(YEAR FROM v.fecha) = EXTRACT(YEAR FROM CURRENT_TIMESTAMP)")
    double obtenerIngresosMes();

    @Query("SELECT v.metodoPago, COUNT(v) FROM Venta v GROUP BY v.metodoPago")
    List<Object[]> contarPorMetodoPago();

    @Query("SELECT d.producto.nombre, SUM(d.cantidad), SUM(d.subtotal) FROM DetalleVenta d GROUP BY d.producto.nombre ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> productosMasVendidos();

    @Query(value = """
                SELECT
                    CAST(v.fecha AS date) AS fecha,
                    SUM(v.total) AS total
                FROM venta v
                WHERE v.fecha >= CURRENT_DATE - INTERVAL '30 days'
                GROUP BY fecha
                ORDER BY fecha
            """, nativeQuery = true)
    List<Object[]> obtenerVentasPorDia();

    @Query(value = """
                SELECT
                    EXTRACT(YEAR FROM v.fecha) AS anio,
                    EXTRACT(MONTH FROM v.fecha) AS mes,
                    SUM(v.total) AS total
                FROM venta v
                GROUP BY anio, mes
                ORDER BY anio, mes
            """, nativeQuery = true)
    List<Object[]> obtenerVentasPorMes();

    List<Venta> findAllByOrderByFechaDesc();

}
