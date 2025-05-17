package com.Distribuidora.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.model.Factura;
//import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.FacturaRepository;
import com.Distribuidora.app.repository.VendedorRepository;
import com.Distribuidora.app.repository.ArticuloRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/facturas")
public class FacturaWebController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ArticuloRepository articuloRepository;  // Cambiado de RepuestoRepository a ArticuloRepository

    // Método para calcular el total de la factura
    private double calcularTotalFactura(Factura factura) {
        double total = 0;

        for (Articulo articulo : factura.getArticulos()) {
            int cantidad = articulo.getCantidad();
            total += articulo.getPrecio() * cantidad;
        }

        return total;
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

        List<Articulo> articulosDisponibles = articuloRepository.findAll();  // Trae todos los artículos
        model.addAttribute("articulosDisponibles", articulosDisponibles);   // Se los pasa al modelo

        return "factura_form";
    }


    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute Factura factura, 
                                 BindingResult result, 
                                 Model model, 
                                 @RequestParam Map<String, String> cantidades) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "factura_form";
        }

        for (Articulo articulo : factura.getArticulos()) {
            String cantidadKey = "cantidad-" + articulo.getId();
            if (cantidades.containsKey(cantidadKey)) {
                int cantidad = Integer.parseInt(cantidades.get(cantidadKey));
                articulo.setCantidad(cantidad);
            }
        }

        double total = calcularTotalFactura(factura);
        factura.setTotal(total);

        facturaRepository.save(factura);
        return "redirect:/facturas";
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
                                    @RequestParam Map<String, String> cantidades) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "factura_form";
        }

        factura.setId(id);

        for (Articulo articulo : factura.getArticulos()) {
            String cantidadKey = "cantidad-" + articulo.getId();
            if (cantidades.containsKey(cantidadKey)) {
                int cantidad = Integer.parseInt(cantidades.get(cantidadKey));
                articulo.setCantidad(cantidad);
            }
        }

        double total = calcularTotalFactura(factura);
        factura.setTotal(total);

        facturaRepository.save(factura);
        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable String id) {
        facturaRepository.deleteById(id);
        return "redirect:/facturas";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("vendedores", vendedorRepository.findAll());
        model.addAttribute("articulos", articuloRepository.findAll());  // Cambiado de repuestos a articulos
    }
}

