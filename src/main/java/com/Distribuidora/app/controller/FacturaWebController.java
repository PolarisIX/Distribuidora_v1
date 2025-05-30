package com.Distribuidora.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Factura;
import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.repository.FacturaRepository;
import com.Distribuidora.app.repository.VendedorRepository;
import com.Distribuidora.app.repository.ArticuloRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/facturas")
public class FacturaWebController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ArticuloRepository articuloRepository;
    
    @GetMapping("/misfacturas")
    public String verMisFacturas(Model model) {
        List<Factura> facturas = facturaRepository.findAll();
        model.addAttribute("facturas", facturas);
        return "facturas";
    }


    private void cargarDatosFormulario(Model model) {
        model.addAttribute("vendedores", vendedorRepository.findAll());
        model.addAttribute("articulos", articuloRepository.findAll());
    }

    private double calcularTotalFactura(Factura factura) {
        double total = 0;
        if (factura.getArticulos() != null) {
            for (Articulo articulo : factura.getArticulos()) {
                total += articulo.getPrecio() * articulo.getCantidad();
            }
        }
        return total;
    }

    private void procesarFactura(Factura factura, Map<String, String> cantidades, String vendedorId) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId).orElse(null);
        factura.setVendedor(vendedor);

        if (factura.getArticulos() != null) {
            for (Articulo articulo : factura.getArticulos()) {
                String key = "cantidad-" + articulo.getId();
                if (cantidades.containsKey(key)) {
                    int cantidad = Integer.parseInt(cantidades.get(key));
                    articulo.setCantidad(cantidad);
                }
            }
        }

        double total = calcularTotalFactura(factura);
        factura.setTotal(total);
    }

    @GetMapping
    public String listarFacturas(Model model) {
        List<Factura> facturas = facturaRepository.findAll();
        model.addAttribute("facturas", facturas);
        return "facturas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioFactura(Model model) {
        model.addAttribute("factura", new Factura());
        cargarDatosFormulario(model); // Debe cargar vendedores y artículos
        return "formFactura"; // Asegúrate de que coincida con tu vista
    }

    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute Factura factura,
                                 BindingResult result,
                                 @RequestParam(name = "articuloNombre") List<String> nombres,
                                 @RequestParam(name = "cantidad") List<Integer> cantidades,
                                 @RequestParam(name = "precio") List<Double> precios,
                                 @RequestParam(name = "vendedorId") String vendedorId,
                                 Model model) {

        // Buscar el objeto Vendedor por ID
        Optional<Vendedor> vendedorOpt = vendedorRepository.findById(vendedorId);
        if (!vendedorOpt.isPresent()) {
            model.addAttribute("error", "Debe seleccionar un vendedor válido.");
            model.addAttribute("factura", factura);
            cargarDatosFormulario(model);
            return "formFactura";
        }

        // Validación de artículos
        if (nombres.isEmpty() || cantidades.isEmpty() || precios.isEmpty()
                || nombres.size() != cantidades.size()
                || nombres.size() != precios.size()) {
            model.addAttribute("error", "Debe agregar al menos un artículo con cantidades y precios válidos.");
            model.addAttribute("factura", factura);
            cargarDatosFormulario(model);
            return "formFactura";
        }

        // Crear lista de artículos
        List<Articulo> articulos = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            Articulo articulo = new Articulo();
            articulo.setNombre(nombres.get(i));
            articulo.setCantidad(cantidades.get(i));
            articulo.setPrecio(precios.get(i));
            articulos.add(articulo);
        }

        // Asignar artículos y vendedor
        factura.setArticulos(articulos);
        factura.setVendedor(vendedorOpt.get());

        // Calcular total
        double total = articulos.stream()
                                .mapToDouble(a -> a.getCantidad() * a.getPrecio())
                                .sum();
        factura.setTotal(total);

        // Validación final
        if (result.hasErrors()) {
            model.addAttribute("factura", factura);
            cargarDatosFormulario(model);
            return "formFactura";
        }

        // Guardar factura
        facturaRepository.save(factura);
        return "redirect:/facturas/misfacturas";
    }


    @GetMapping("/editar/{id}")
    public String editarFactura(@PathVariable String id, Model model) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura inválida: " + id));

        model.addAttribute("factura", factura);
        cargarDatosFormulario(model);
        return "factura_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarFactura(@PathVariable String id,
                                    @Valid @ModelAttribute Factura factura,
                                    BindingResult result,
                                    Model model,
                                    @RequestParam Map<String, String> cantidades,
                                    @RequestParam("vendedor") String vendedorId) {

        if (vendedorId == null || vendedorId.trim().isEmpty()) {
            result.rejectValue("vendedor", null, "Debe seleccionar un vendedor válido.");
        }

        if (factura.getArticulos() == null || factura.getArticulos().isEmpty()) {
            result.rejectValue("articulos", null, "Debe seleccionar al menos un artículo.");
        }

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "factura_form";
        }

        factura.setId(id);
        procesarFactura(factura, cantidades, vendedorId);
        facturaRepository.save(factura);
        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable String id) {
        facturaRepository.deleteById(id);
        return "redirect:/facturas";
    }

    @GetMapping("/ver/{id}")
    public String verFactura(@PathVariable String id, Model model) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + id));
        model.addAttribute("factura", factura);
        return "factura_detalle"; // Vista opcional
    }
}
