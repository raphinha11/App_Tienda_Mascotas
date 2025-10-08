<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Usuario" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    modelo.Usuario usuario = (modelo.Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tienda de Mascotas - Administrador</title>

<link rel="stylesheet"
    href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">

<style>
    body {
        background-color: #f8f9fa;
    }
    .navbar {
        margin-bottom: 30px;
    }
    .hero {
        background: url('https://segurossura.com/content/webp-express/webp-images/doc-root/content/uploads/sites/10/2020/12/seguros-sura-corporativo-plan-elige-mascotas.jpg.webp') center/cover no-repeat;
        height: 350px;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        text-shadow: 2px 2px 4px rgba(0,0,0,0.7);
    }
    .card img {
        height: 200px;
        object-fit: cover;
    }
</style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="#">🐾 Tienda de Mascotas</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="ClienteServlet">Clientes</a></li>
                    <li class="nav-item"><a class="nav-link" href="MascotaServlet">Mascotas</a></li>
                    <li class="nav-item"><a class="nav-link" href="ProductoServlet">Productos</a></li>
                    <li class="nav-item"><a class="nav-link text-danger" href="logout.jsp">Cerrar sesión</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero -->
    <section class="hero">
        <h1 class="display-4 fw-bold">Bienvenido, <%= ((modelo.Usuario)session.getAttribute("usuario")).getUsuario() %> 🐶</h1>
    </section>

    <!-- Opciones principales -->
    <div class="container text-center mt-5">
        <div class="row">
            <div class="col-md-6 mb-4">
                <div class="card shadow-sm">
                    <img src="https://blog.coomeva.com.co/uploads/644986e632bec.webp" class="card-img-top" alt="Clientes">
                    <div class="card-body">
                        <h5 class="card-title">Gestión de Clientes</h5>
                        <p class="card-text">Administra la información de tus clientes de manera fácil.</p>
                        <a href="ClienteServlet" class="btn btn-outline-dark w-100">Ir a Clientes</a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="card shadow-sm">
                    <img src="https://aishlatino.b-cdn.net/wp-content/uploads/2021/11/leyes-judias-sobre-mascotas-730x411-SP.jpg" class="card-img-top" alt="Mascotas">
                    <div class="card-body">
                        <h5 class="card-title">Gestión de Mascotas</h5>
                        <p class="card-text">Registra y controla la información de las mascotas.</p>
                        <a href="MascotaServlet" class="btn btn-outline-dark w-100">Ir a Mascotas</a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="card shadow-sm">
                    <img src="https://www.eltelegrafo.com.ec/media/k2/items/cache/a67dc3079982e9c8dc2e57383a943f06_XL.jpg" class="card-img-top" alt="Mascotas">
                    <div class="card-body">
                        <h5 class="card-title">Gestión de Productos</h5>
                        <p class="card-text">Registra y controla la información de los productos.</p>
                        <a href="ProductoServlet" class="btn btn-outline-dark w-100">Ir a Productos</a>
                    </div>
                </div>
            </div>
            
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-3 mt-5">
        <p class="mb-0">&copy; 2025 Tienda de Mascotas. Todos los derechos reservados.</p>
    </footer>

    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
