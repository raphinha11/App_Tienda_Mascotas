<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    modelo.Usuario usuario = (modelo.Usuario) session.getAttribute("usuario");
    if (usuario != null) {
        if ("admin".equals(usuario.getRol())) {
            response.sendRedirect("admin.jsp");
        } else if ("veterinario".equals(usuario.getRol())) {
            response.sendRedirect("veterinario.jsp");
        }
        return;
    }
%>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión - Tienda de Mascotas</title>

    <!-- Bootstrap local -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">

    <style>
        body {
            background: linear-gradient(135deg, #a8e6cf, #dcedc1);
            font-family: "Poppins", sans-serif;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-card {
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            padding: 40px 35px;
            width: 100%;
            max-width: 400px;
        }

        .login-card img {
            width: 80px;
            margin-bottom: 15px;
        }

        .login-card h3 {
            color: #2e7d32;
            font-weight: 600;
            margin-bottom: 25px;
        }

        .btn-login {
            background-color: #4CAF50;
            border: none;
            color: #fff;
            font-weight: 500;
            transition: background 0.3s;
        }

        .btn-login:hover {
            background-color: #43a047;
        }

        .error {
            color: #e53935;
            font-size: 14px;
            margin-top: 10px;
        }

        .footer {
            text-align: center;
            font-size: 13px;
            color: #777;
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="login-card text-center">
        <img src="https://cdn-icons-png.flaticon.com/512/616/616408.png" alt="Logo Mascota">
        <h3>Tienda de Mascotas</h3>

        <form action="LoginServlet" method="post">
            <div class="mb-3 text-start">
                <label for="usuario" class="form-label">Usuario</label>
                <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Ingrese su usuario" required>
            </div>

            <div class="mb-3 text-start">
                <label for="contrasena" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="contrasena" name="contrasena" placeholder="Ingrese su contraseña" required>
            </div>

            <button type="submit" class="btn btn-login w-100 py-2">Ingresar</button><br><br>
            <a href="registro.jsp" class="btn btn-success me-2">Registrase</a>
            

            <p class="error">
                <%= request.getAttribute("mensaje") != null ? request.getAttribute("mensaje") : "" %>
            </p>
        </form>

        <div class="footer">
            <p>© 2025 Tienda de Mascotas 🐾</p>
        </div>
    </div>

    <!-- Bootstrap JS (opcional si usas componentes dinámicos) -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
