package com.example.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.Models.Producto;
import com.example.demo.Services.AuditoriaService;
import com.example.demo.Services.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j 

@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping("/{id}")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        log.debug("Solicitud GET para obtener producto con ID: {}", id);
        return productoService.obtenerProductoPorId(id)
                .map(producto -> {
                    log.debug("Producto encontrado: {}", producto.getNombre());
                    return ResponseEntity.ok(producto);
                })
                .orElseGet(() -> {
                    log.warn("Producto con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public ResponseEntity<?> crearProducto(
        @RequestPart("producto") String productoJson,
        @RequestPart(value = "imagen", required = false) MultipartFile imagen,
        @RequestHeader("usuario") String usuario) {

    try {
        // Deserializamos el JSON recibido como texto
        ProductoDTO productoDTO = objectMapper.readValue(productoJson, ProductoDTO.class);

        log.debug("ProductoDTO recibido: {}", productoDTO);
        log.debug("Precio recibido en el backend: {}", productoDTO.getPrecio());

        // Validación del precio
        if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
            log.error("El precio no puede ser nulo o menor que 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El precio es obligatorio y debe ser mayor que 0.");
        }

        // Crear objeto Producto a partir del DTO
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCodigo(productoDTO.getCodigo());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setProveedor(productoDTO.getProveedor());
        producto.setPresentacion(productoDTO.getPresentacion());

        if (productoDTO.getFechaVencimiento() != null) {
            producto.setFechaVencimiento(productoDTO.getFechaVencimiento());
        }

        if (imagen != null) {
            producto.setImagen(imagen.getBytes());
        }

        // Guardar producto en la base de datos
        Producto nuevoProducto = productoService.crearProducto(producto);

        // Registrar auditoría sin el campo "imagen"
        auditoriaService.registrarAuditoria(
                usuario,
                "productos",
                "crear",
                "Producto creado: " + producto.getNombre(),
                null,
                nuevoProducto
        );

        return ResponseEntity.ok(nuevoProducto);

    } catch (Exception e) {
        log.error("Error al crear producto", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al crear producto: " + e.getMessage());
    }
}



    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestPart("producto") String productoJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestHeader("usuario") String usuario) { // Usuario desde el header
        try {
            ProductoDTO productoDTO = objectMapper.readValue(productoJson, ProductoDTO.class);
            Producto producto = productoService.obtenerProductoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            String valorAnterior = producto.toString();

            producto.setNombre(productoDTO.getNombre());
            // Actualizar más campos...

            Producto actualizado = productoService.actualizarProducto(id, producto);

            // Registrar auditoría
            auditoriaService.registrarAuditoria(usuario, "productos", "actualizar", 
                "Producto actualizado: " + producto.getNombre(), valorAnterior, producto.toString());

            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id, @RequestHeader("usuario") String usuario) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            String valorAnterior = producto.toString();

            productoService.eliminarProducto(id);

            // Registrar auditoría
            auditoriaService.registrarAuditoria(usuario, "productos", "eliminar", 
                "Producto eliminado: " + producto.getNombre(), valorAnterior, null);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String codigo) {

        log.debug("Solicitud para listar productos. Filtros - nombre: {}, categoria: {}, codigo: {}",
                nombre, categoria, codigo);

        if (nombre != null) {
            log.debug("Buscando por nombre: {}", nombre);
            return productoService.buscarPorNombre(nombre);
        } else if (categoria != null) {
            log.debug("Buscando por categoría: {}", categoria);
            return productoService.buscarPorCategoria(categoria);
        } else if (codigo != null) {
            log.debug("Buscando por código: {}", codigo);
            return productoService.buscarPorCodigo(codigo);
        } else {
            log.debug("Listando todos los productos");
            return productoService.listarProductos();
        }
    }

    @GetMapping("/alertas/vencimiento")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> productosProximosAVencer(@RequestParam(defaultValue = "7") int dias) {
        log.info("Buscando productos próximos a vencer en {} días", dias);
        return productoService.listarProductosProximosAVencer(dias);
    }

    @GetMapping("/alertas/stock-bajo")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> productosStockBajo(@RequestParam(defaultValue = "10") int stockMinimo) {
        log.info("Buscando productos con stock bajo (menos de {} unidades)", stockMinimo);
        return productoService.listarProductosStockBajo(stockMinimo);
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long id) {
        log.debug("Solicitud para obtener imagen del producto con ID: {}", id);
        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);

        if (productoOpt.isEmpty() || productoOpt.get().getImagen() == null) {
            log.warn("Imagen no encontrada para el producto con ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        byte[] imagenBytes = productoOpt.get().getImagen();
        log.debug("Devolviendo imagen para producto ID: {}. Tamaño: {} bytes", id, imagenBytes.length);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Asegúrate de que la imagen esté en formato JPEG
        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
    }

}
