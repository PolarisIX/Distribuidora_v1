package com.TallerMecanica.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.TallerMecanica.app.model.Factura;
import com.TallerMecanica.app.model.Cliente;
import com.TallerMecanica.app.model.Mecanico;
import com.TallerMecanica.app.model.Repuesto;
import com.TallerMecanica.app.repository.FacturaRepository;
import com.TallerMecanica.app.repository.ClienteRepository;
import com.TallerMecanica.app.repository.MecanicoRepository;
import com.TallerMecanica.app.repository.RepuestoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/facturas")
public class FacturaWebController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private RepuestoRepository repuestoRepository;

    // Método para calcular el total de la factura
    private double calcularTotalFactura(Factura factura) {
        double total = 0;
        
        // Iterar sobre los repuestos seleccionados en la factura
        for (Repuesto repuesto : factura.getRepuestos()) {
            int cantidad = repuesto.getCantidad();  // Obtener la cantidad seleccionada
            total += repuesto.getPrecio() * cantidad;  // Sumar el precio por la cantidad
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
    public String nuevaFactura(Model model) {
        model.addAttribute("factura", new Factura());
        cargarDatosFormulario(model);
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

        // Actualizar las cantidades de los repuestos seleccionados
        for (Repuesto repuesto : factura.getRepuestos()) {
            String cantidadKey = "cantidad-" + repuesto.getId();
            if (cantidades.containsKey(cantidadKey)) {
                int cantidad = Integer.parseInt(cantidades.get(cantidadKey));
                repuesto.setCantidad(cantidad);  // Establecer la cantidad del repuesto
            }
        }

        // Calcular el total de la factura
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

        // Actualizar las cantidades de los repuestos seleccionados
        for (Repuesto repuesto : factura.getRepuestos()) {
            String cantidadKey = "cantidad-" + repuesto.getId();
            if (cantidades.containsKey(cantidadKey)) {
                int cantidad = Integer.parseInt(cantidades.get(cantidadKey));
                repuesto.setCantidad(cantidad);
            }
        }

        // Calcular el total de la factura
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
        model.addAttribute("mecanicos", mecanicoRepository.findAll());
        model.addAttribute("repuestos", repuestoRepository.findAll());
    }
}
