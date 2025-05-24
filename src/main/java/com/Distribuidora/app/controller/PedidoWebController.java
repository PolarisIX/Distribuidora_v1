// PedidoWebController.java
package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Pedido;
import com.Distribuidora.app.model.Cliente;

import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.repository.PedidoRepository;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.RepartidorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoWebController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @GetMapping
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        model.addAttribute("pedidos", pedidos);
        return "pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("repartidores", repartidorRepository.findAll());
        return "indexCliente";
    }

    @PostMapping("/guardar")
    public String guardarPedido(@Valid @ModelAttribute Pedido pedido,
                                BindingResult result,
                                @RequestParam(name = "articuloNombre") List<String> nombres,
                                @RequestParam(name = "cantidad") List<Integer> cantidades,
                                @RequestParam(name = "precio") List<Double> precios,
                                @RequestParam(name = "direccionEntrega") String direccionEntrega,
                                @RequestParam(name = "notas", required = false) String notas,
                                HttpSession session,
                                Model model) {

        String clienteId = (String) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }

        // Validar listas de artículos
        if (nombres.isEmpty() || cantidades.isEmpty() || precios.isEmpty() ||
            nombres.size() != cantidades.size() || nombres.size() != precios.size()) {
            model.addAttribute("error", "Debe agregar al menos un artículo con cantidades y precios válidos.");
            model.addAttribute("pedido", pedido);
            return "inicio-administrador";
        }

        // **Asigna clienteId y artículos antes de la validación manual**
        pedido.setClienteId(clienteId);

        List<Articulo> articulos = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            Articulo articulo = new Articulo();
            articulo.setNombre(nombres.get(i));
            articulo.setCantidad(cantidades.get(i));
            articulo.setPrecio(precios.get(i));
            articulos.add(articulo);
        }
        pedido.setArticulos(articulos);
        pedido.setDireccionEntrega(direccionEntrega);
        pedido.setNotas(notas);

        // Aquí validas el pedido sólo manualmente si quieres (por ejemplo, revisar campos obligatorios):
        if (pedido.getClienteId() == null || pedido.getArticulos().isEmpty()) {
            model.addAttribute("error", "Cliente o lista de artículos inválidos.");
            model.addAttribute("pedido", pedido);
            return "indexCliente";
        }

        // Guardar total y demás
        double total = articulos.stream().mapToDouble(a -> a.getCantidad() * a.getPrecio()).sum();
        pedido.setTotal(total);

        // Guardar pedido en BD
        pedidoRepository.save(pedido);

        return "redirect:/pedidos/mispedidos";
    }

    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable String id, Model model) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("repartidores", repartidorRepository.findAll());
            return "pedidos/formulario";
        } else {
            return "redirect:/pedidos";
        }
    }
    
    @GetMapping("/mispedidos")
    public String verMisPedidos(HttpSession session, Model model) {
        String clienteId = (String) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/administrador/"; // O redirigir al login
        }

        List<Pedido> pedidosCliente = pedidoRepository.findByClienteId(clienteId); 
        model.addAttribute("pedidos", pedidosCliente);
        return "mispedidos";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable String id) {
        pedidoRepository.deleteById(id);
        return "redirect:/pedidos";
    }

    // Aceptar un pedido
    @PostMapping("/aceptar")
    public String aceptarPedido(@RequestParam("id") String id) {
        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setEstado("aceptado");
            pedidoRepository.save(pedido);
        }
        return "redirect:/repartidores/pedidos";
    }

    // Rechazar un pedido
    @PostMapping("/rechazar")
    public String rechazarPedido(@RequestParam("id") String id) {
        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setEstado("rechazado");
            pedidoRepository.save(pedido);
        }
        return "redirect:/repartidores/pedidos";
    }

    // Cambiar estado desde un select
    @PostMapping("/cambiarEstado")
    public String cambiarEstado(@RequestParam("id") String id, @RequestParam("estado") String estado) {
        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setEstado(estado);
            pedidoRepository.save(pedido);
        }
        return "redirect:/repartidores/pedidos";
    }
}
