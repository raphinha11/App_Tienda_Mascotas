<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>Verificar Conexión</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f6f8fa;
	padding: 40px;
	text-align: center;
}

.btn {
	background-color: #27ae60;
	color: white;
	padding: 15px 25px;
	text-decoration: none;
	font-weight: bold;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.btn:hover {
	background-color: #219150;
}

.mensaje {
	margin-top: 30px;
	font-size: 18px;
	color: #2c3e50;
}

button {
	padding: 10px 20px;
	margin-right: 10px;
	background-color: #3498db;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
}
</style>
</head>
<body>
	<h2>Probar conexión a la base de datos</h2>
	<form method="get" action="VerificarConexion">
		<button type="submit" class="btn">Probar Conexión</button>
	</form>

	<div class="mensaje">
		<%
            String mensaje = (String) request.getAttribute("mensaje");
            if (mensaje != null) {
        %>
		<p><%= mensaje %></p>
		<%
            }
        %>
	</div>

	<br>
	<a href="ClienteServlet"><button>Volver a la lista</button></a>

</body>
</html>
