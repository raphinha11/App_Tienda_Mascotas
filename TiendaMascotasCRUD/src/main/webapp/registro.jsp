<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de Cliente</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
</head>
<body style="background: #f5f5f5;">
<div class="container mt-5">
    <div class="card shadow mx-auto" style="max-width: 500px;">
        <div class="card-body">
            <h3 class="text-center text-success mb-4">Registro de Cliente 🐾</h3>

            <form action="RegistroServlet" method="post">
                <div class="mb-3">
                    <label>Usuario</label>
                    <input type="text" name="usuario" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label>Contraseña</label>
                    <input type="password" name="contrasena" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label>Correo</label>
                    <input type="email" name="correo" class="form-control" required>
                </div>

                <button type="submit" class="btn btn-success w-100">Registrarme</button>
            </form>

            <p class="text-center mt-3">
                ¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
