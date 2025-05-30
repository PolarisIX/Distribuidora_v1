package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.repository.ArticuloRepository;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.VendedorRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articulos")
public class ArticuloWebController {

    @Autowired
    private ArticuloRepository articuloRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    // Ruta para guardar imágenes dentro de resources/static/img (debe existir)
    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img";

    // Listar todos los artículos
    @GetMapping
    public String listarArticulos(Model model) {
        model.addAttribute("articulos", articuloRepo.findAll());
        return "verArticulos";
    }
    
    @GetMapping("/cliente/catalogo")
    public String mostrarCatalogoCliente(HttpSession session, Model model) {
        String clienteId = (String) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/administrador/"; // redirige a login si no está logueado
        }

        Optional<Cliente> clienteOpt = clienteRepo.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            return "redirect:/administrador/"; // cliente no encontrado
        }

        Cliente cliente = clienteOpt.get();
        List<Articulo> articulos = articuloRepo.findAll();

        model.addAttribute("articulos", articulos);
        model.addAttribute("cliente", cliente);

        return "indexCliente";
    }



    
    @Autowired
    private VendedorRepository vendedorRepo;

    @GetMapping("/cliente/catalogoV")
    public String mostrarCatalogoClientes(Model model) {
        List<Articulo> articulos = articuloRepo.findAll();
        List<Vendedor> vendedores = vendedorRepo.findAll(); // <-- Obtener los vendedores

        model.addAttribute("articulos", articulos);
        model.addAttribute("vendedores", vendedores); // <-- Agregar al modelo

        return "indexVendedor"; // Vista que contiene el modal y el formulario
    }



    // Mostrar formulario para nuevo artículo
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("articulo", new Articulo());
        return "formArticulo";
    }

 // Guardar nuevo artículo con imagen SIN conflicto
    @PostMapping("/guardar")
    public String guardarArticulo(@ModelAttribute Articulo articulo,
                                  @RequestParam("imagenFile") MultipartFile imagenFile) {
        try {
            if (!imagenFile.isEmpty()) {
                String nombreArchivo = Paths.get(imagenFile.getOriginalFilename()).getFileName().toString();

                Path rutaDirectorio = Paths.get(uploadDir);
                if (!Files.exists(rutaDirectorio)) {
                    Files.createDirectories(rutaDirectorio);
                }

                Path rutaArchivo = rutaDirectorio.resolve(nombreArchivo);
                Files.write(rutaArchivo, imagenFile.getBytes());

                articulo.setImagen(nombreArchivo); // Solo guardamos el nombre
            }

            articuloRepo.save(articulo);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/articulos";
    }

    // Actualizar artículo SIN conflicto
    @PostMapping("/actualizar/{id}")
    public String actualizarArticulo(@PathVariable String id,
                                     @ModelAttribute Articulo articuloActualizado,
                                     @RequestParam("imagenFile") MultipartFile imagenFile) {
        try {
            Articulo articuloExistente = articuloRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado con id: " + id));

            articuloExistente.setNombre(articuloActualizado.getNombre());
            articuloExistente.setCategoria(articuloActualizado.getCategoria());
            articuloExistente.setMarca(articuloActualizado.getMarca());
            articuloExistente.setPrecio(articuloActualizado.getPrecio());
            articuloExistente.setCantidad(articuloActualizado.getCantidad());
            articuloExistente.setDescripcion(articuloActualizado.getDescripcion());

            if (!imagenFile.isEmpty()) {
                // Eliminar imagen anterior si existe
                if (articuloExistente.getImagen() != null) {
                    Path rutaImagenAnterior = Paths.get(uploadDir, articuloExistente.getImagen());
                    Files.deleteIfExists(rutaImagenAnterior);
                }

                String nombreArchivoNuevo = Paths.get(imagenFile.getOriginalFilename()).getFileName().toString();
                Path rutaNueva = Paths.get(uploadDir, nombreArchivoNuevo);
                Files.write(rutaNueva, imagenFile.getBytes());

                articuloExistente.setImagen(nombreArchivoNuevo);
            }

            articuloRepo.save(articuloExistente);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/articulos";
    }
    
 // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Optional<Articulo> articuloOpt = articuloRepo.findById(id);
        if (articuloOpt.isPresent()) {
            model.addAttribute("articulo", articuloOpt.get());
            return "formArticulo";
        } else {
            return "redirect:/articulos";
        }
    }
    
 // Eliminar artículo
    @GetMapping("/eliminar/{id}")
    public String eliminarArticulo(@PathVariable String id) {
        Optional<Articulo> articuloOpt = articuloRepo.findById(id);
        if (articuloOpt.isPresent()) {
            Articulo articulo = articuloOpt.get();
            // Eliminar imagen física si existe
            if (articulo.getImagen() != null) {
                Path rutaImagen = Paths.get(uploadDir, articulo.getImagen());
                try {
                    Files.deleteIfExists(rutaImagen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            articuloRepo.deleteById(id);
        }
        return "redirect:/articulos";
    }



}

