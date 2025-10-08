package controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Producto;
import modelo.ProductoDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Servlet implementation class ProductoServlet
 */
@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
	ProductoDAO dao = new ProductoDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String accion = request.getParameter("accion");
		
		switch (accion) {
		case "Guardar":
			// Crear y llenar objeto Cliente con datos del formulario
			Producto c = new Producto();
			c.setNombre_producto(request.getParameter("nombre_producto"));
			c.setMarca(request.getParameter("marca"));
			c.setPrecio(Integer.parseInt(request.getParameter("precio")));
			dao.agregar(c);

			break;

		case "Actualizar":
			// Crear objeto Cliente con ID y nuevos datos
			Producto c2 = new Producto();
			c2.setCodigo_barras(Integer.parseInt(request.getParameter("codigo_barras")));
			c2.setNombre_producto(request.getParameter("nombre_producto"));
			c2.setMarca(request.getParameter("marca"));
			c2.setPrecio(Integer.parseInt(request.getParameter("precio")));
			dao.actualizar(c2);

			break;

		case "Eliminar":
			// Obtener ID del cliente a eliminar
			int idEliminar = Integer.parseInt(request.getParameter("codigo_barras"));
			dao.eliminar(idEliminar);

			break;

		case "Consultar":
			// Buscar cliente por ID
		    int idBuscar = Integer.parseInt(request.getParameter("codigo_barras"));
			Producto ProductoEncontrado = dao.buscar(idBuscar);

			// Enviar datos al formulario o mostrar mensaje
			if (ProductoEncontrado != null) {
				request.setAttribute("producto", ProductoEncontrado);
			} else {
				request.setAttribute("mensaje", "Producto no encontrado");
			}

			RequestDispatcher dispatcher1 = request.getRequestDispatcher("formulario3.jsp");
			dispatcher1.forward(request, response);
			return;

		case "ReporteGeneral":
			// Generar PDF con todos los clientes
			generarReporteGeneral(response);
			return;
		}

	response.sendRedirect("ProductoServlet");
}


    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
	    int recordsPerPage = 10;

	    try {
	        if (request.getParameter("page") != null) {
	            page = Integer.parseInt(request.getParameter("page"));
	            if (page < 1) page = 1;
	        }
	    } catch (NumberFormatException e) {
	        page = 1;
	    }

	    List<Producto> todosProducto = dao.listar();
	    int totalProducto = todosProducto.size();
	    int totalPages = (int) Math.ceil((double) totalProducto / recordsPerPage);

	    if (page > totalPages && totalPages > 0) {
	        page = totalPages;
	    }

	    int start = (page - 1) * recordsPerPage;
	    int end = Math.min(start + recordsPerPage, totalProducto);

	    List<Producto> ProductoPaginados = new ArrayList<>();
	    if (start < totalProducto) {
	    	ProductoPaginados = todosProducto.subList(start, end);
	    }

	    request.setAttribute("productos", ProductoPaginados);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalPages", totalPages);

	    RequestDispatcher dispatcher = request.getRequestDispatcher("listar3.jsp");
	    dispatcher.forward(request, response);
	    
	}
    
    private void generarReporteGeneral(HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=reporte_general_Productos.pdf");

			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.LETTER);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			PDType1Font fontRegular = PDType1Font.HELVETICA;
			PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
			float fontSize = 9f;

			float margin = 50;
			float yStart = 730;
			float yPosition = yStart;
			float rowHeight = 15;
			float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

			// Definir columnas y encabezados
			float[] colWidths = { 40, 80, 100, 100, 100, 80 }; 
			String[] headers = { "Codigo de Barras", "Nombre Producto", "Marca", "Precio" };

			// Titulo
			contentStream.beginText();
			contentStream.setFont(fontBold, 14);
			contentStream.newLineAtOffset(margin, yPosition);
			contentStream.showText("Reporte General de Productos");
			contentStream.endText();

			yPosition -= 25;

			// Dibujar encabezados de tabla
			float textY = yPosition - 10;
			float nextX = margin;

			contentStream.setFont(fontBold, fontSize);
			for (int i = 0; i < headers.length; i++) {
				contentStream.beginText();
				contentStream.newLineAtOffset(nextX, textY);
				contentStream.showText(headers[i]);
				contentStream.endText();
				nextX += colWidths[i];
			}

			yPosition -= rowHeight;

			// Obtener datos
			List<Producto> Productos = dao.listar();

			contentStream.setFont(fontRegular, fontSize);

			for (Producto Producto : Productos) {
				// Crear nueva pagina si se agota el espacio
				if (yPosition < margin + rowHeight) {
					contentStream.close();
					page = new PDPage(PDRectangle.LETTER);
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);

					contentStream.setFont(fontRegular, fontSize);

					yPosition = yStart;
				}

				float xText = margin;
				textY = yPosition - 10;

				// Datos del cliente por columna
				String[] data = { String.valueOf(Producto.getCodigo_barras()),
						Producto.getNombre_producto(), Producto.getMarca(), String.valueOf(Producto.getPrecio()) };

				for (int i = 0; i < data.length; i++) {
					contentStream.beginText();
					contentStream.newLineAtOffset(xText, textY);
					contentStream.showText(truncateText(data[i], colWidths[i], fontRegular, fontSize));
					contentStream.endText();
					xText += colWidths[i];
				}

				yPosition -= rowHeight;
			}

			contentStream.close();
			document.save(response.getOutputStream());
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private String truncateText(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
		String result = text;
		while (font.getStringWidth(result) / 1000 * fontSize > maxWidth && result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		if (!result.equals(text)) {
			result += "...";
		}
		return result;
	}

	// Divide un texto largo en múltiples líneas dependiendo del ancho
	private List<String> breakLines(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
		List<String> lines = new ArrayList<>();

		String[] words = text.split(" ");
		StringBuilder line = new StringBuilder();

		for (String word : words) {
			String temp = line.length() == 0 ? word : line + " " + word;
			float size = font.getStringWidth(temp) / 1000 * fontSize;
			if (size > maxWidth) {
				if (line.length() > 0)
					lines.add(line.toString());
				line = new StringBuilder(word);
			} else {
				line = new StringBuilder(temp);
			}
		}

		if (line.length() > 0)
			lines.add(line.toString());
		return lines;
	}

	// Devuelve la cantidad de líneas necesarias para envolver un texto
	private int wrapText(String text, float maxWidth, float fontSize) {
		try {
			return breakLines(text, maxWidth, PDType1Font.HELVETICA, fontSize).size();
		} catch (IOException e) {
			return 1;
		}
	}
    
}
