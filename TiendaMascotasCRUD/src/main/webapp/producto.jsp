<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, modelo.Producto" %>
<%
    // Se obtiene la lista de productos desde el servlet o controlador
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    if (productos == null) {
        productos = new ArrayList<>();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tienda de Mascotas 🐶 - Productos</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f9f9f9;
        }
        .navbar {
            background-color: #ff6f61;
        }
        .navbar-brand, .nav-link, .form-control {
            color: white !important;
        }
        .card {
            border: none;
            box-shadow: 0px 3px 6px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }
        .card:hover {
            transform: scale(1.03);
        }
        .product-img {
            height: 180px;
            object-fit: cover;
            border-radius: 10px;
        }
        .btn-primary {
            background-color: #ff6f61;
            border: none;
        }
        .btn-primary:hover {
            background-color: #e35b50;
        }
        
        .form-control {
    background-color: white !important;  /* Fondo blanco */
    color: #333 !important;              /* Texto oscuro */
    border: 2px solid #ff6f61 !important;/* Borde color coral */
    border-radius: 8px;
        }

        .form-control::placeholder {
        color: #999 !important;              /* Texto del placeholder gris */
        }

        .form-control:focus {
    background-color: #fff !important;
    color: #000 !important;
    box-shadow: 0 0 5px rgba(255,111,97,0.7);
    border-color: #ff6f61 !important;
       }
        
        
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="producto.jsp">🐾 Tienda Mascotas</a>
            <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
            <li class="nav-item"><a class="nav-link text-danger" href="logout.jsp">Cerrar sesión</a></li>
             </ul>
                <form class="d-flex ms-auto" action="ControladorProducto" method="GET">
                    <input class="form-control me-2" type="search" name="buscar" placeholder="Buscar producto..." aria-label="Search">
                    <button class="btn btn-light" type="submit">Buscar</button>
                </form>
                <a href="carrito.jsp" class="btn btn-light ms-3">🛒 Carrito</a>
            </div>
        </div>
    </nav>

    <!-- Productos -->
    <div class="container mt-4">
        <h3 class="mb-4 text-center text-secondary">Nuestros Productos</h3>
        <div class="row">
            <%
                if (productos.isEmpty()) {
            %>
                <div class="text-center text-muted">No hay productos disponibles.</div>
            <%
                } else {
                    for (Producto p : productos) {
            %>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="imagenes_productos/<%=p.getCodigo_barras()%>.jpg" class="card-img-top product-img" alt="<%=p.getNombre_producto()%>">
                        <div class="card-body text-center">
                            <h5 class="card-title"><%=p.getNombre_producto()%></h5>
                            <p class="text-muted mb-1">Marca: <%=p.getMarca()%></p>
                            <p class="fw-bold">$<%=String.format("%.2f", p.getPrecio())%></p>
                            <form action="ControladorCarrito" method="POST">
                                <input type="hidden" name="accion" value="agregar">
                                <input type="hidden" name="codigo_barras" value="<%=p.getCodigo_barras()%>">
                                <input type="number" name="cantidad" class="form-control mb-2" value="1" min="1">
                                <button type="submit" class="btn btn-primary w-100">Añadir al carrito 🛒</button>
                            </form>
                        </div>
                    </div>
                </div>
            <%
                    }
                }
            %>
        </div>
    </div>

</body>
</html>
