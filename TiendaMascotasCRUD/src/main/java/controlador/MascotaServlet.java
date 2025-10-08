package controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Mascota;
import modelo.MascotaDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Servlet implementation class MascotaServlet
 */
@WebServlet("/MascotaServlet")
public class MascotaServlet extends HttpServlet {
	MascotaDAO dao = new MascotaDAO();
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String accion = request.getParameter("accion");
		
		switch (accion) {
		case "Guardar":
			Mascota c = new Mascota();
			c.setNombre(request.getParameter("nombre"));
			c.setTipo(request.getParameter("tipo"));
			c.setGenero(request.getParameter("genero"));
			c.setRaza(request.getParameter("raza"));
			c.setCedula_cliente(Integer.parseInt(request.getParameter("cedula_cliente")));
			dao.agregar(c);
			
			break;

		case "Actualizar":
			Mascota c2 = new Mascota();
			c2.setId_mascota(Integer.parseInt(request.getParameter("id_mascota")));
			c2.setNombre(request.getParameter("nombre"));
			c2.setTipo(request.getParameter("tipo"));
			c2.setGenero(request.getParameter("genero"));
			c2.setRaza(request.getParameter("raza"));
			dao.actualizar(c2);
			
			break;
		
		case "Eliminar":
			int idEliminar = Integer.parseInt(request.getParameter("id_mascota"));
			Mascota eliminado = dao.buscar(idEliminar);
			dao.eliminar(idEliminar);
			
			break;
		
		case "Consultar":
			int idBuscar = Integer.parseInt(request.getParameter("id_mascota"));
			Mascota mascotaEncontrada = dao.buscar(idBuscar);
			
			if (mascotaEncontrada != null) {
				request.setAttribute("mascota", mascotaEncontrada);
			} else {
				request.setAttribute("mensaje", "Mascota no encontrada");
			}
			
			RequestDispatcher disparcher1 = request.getRequestDispatcher("formulario2.jsp");
			disparcher1.forward(request, response);
			return;
			
		case "Reporte":
			// Generar PDF con los datos de un cliente
			int idReporte = Integer.parseInt(request.getParameter("id_mascota"));
			Mascota mascotaReporte = dao.buscar(idReporte);

			if (mascotaReporte != null) {
				generarReporte(mascotaReporte, response);
			} else {
				request.setAttribute("mensaje", "Mascota no encontrado para generar el reporte");
				RequestDispatcher dispatcher11 = request.getRequestDispatcher("formulario2.jsp");
				dispatcher11.forward(request, response);
			}
			return;

		case "ReporteGeneral":
			// Generar PDF con todos los clientes
			generarReporteGeneral(response);
			return;
		}
		
		response.sendRedirect("MascotaServlet");
		
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

		    List<Mascota> todosMascota = dao.listar();
		    int totalMascota = todosMascota.size();
		    int totalPages = (int) Math.ceil((double) totalMascota / recordsPerPage);

		    if (page > totalPages && totalPages > 0) {
		        page = totalPages;
		    }

		    int start = (page - 1) * recordsPerPage;
		    int end = Math.min(start + recordsPerPage, totalMascota);

		    List<Mascota> mascotaPaginados = new ArrayList<>();
		    if (start < totalMascota) {
		        mascotaPaginados = todosMascota.subList(start, end);
		    }

		    request.setAttribute("mascotas", mascotaPaginados);
		    request.setAttribute("currentPage", page);
		    request.setAttribute("totalPages", totalPages);

		    RequestDispatcher dispatcher = request.getRequestDispatcher("listar2.jsp");
		    dispatcher.forward(request, response);
		
	}

	private void generarReporte(Mascota mascota, HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition",
					"attachment; filename=certificado_mascota_" + mascota.getId_mascota() + ".pdf");

			// Crear documento y pagina
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.LETTER);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// Titulo del certificado
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
			contentStream.newLineAtOffset(100, 700);
			contentStream.showText("CERTIFICADO DE MASCOTA");
			contentStream.endText();

			// Informacion del cliente
			float y = 660;
			float leading = 20;
			float fontSize = 12;

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, fontSize);
			contentStream.newLineAtOffset(100, y);

			String[] lineas = { "ID Mascota: " + mascota.getId_mascota(),
					"Nombre: " + mascota.getNombre(),
					"Tipo: " + mascota.getTipo(),
					"Genero: " + mascota.getGenero(), "Raza: " + mascota.getRaza() };

			for (String linea : lineas) {
				contentStream.showText(linea);
				contentStream.newLineAtOffset(0, -leading);
			}

			contentStream.endText();
			contentStream.close();

			document.save(response.getOutputStream());
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generarReporteGeneral(HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=reporte_general_mascota.pdf");

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
			String[] headers = { "ID", "Nombre", "Tipo", "Genero", "Raza", "Cedula_Cliente" };

			// Titulo
			contentStream.beginText();
			contentStream.setFont(fontBold, 14);
			contentStream.newLineAtOffset(margin, yPosition);
			contentStream.showText("Reporte General de Mascota");
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
			List<Mascota> mascotas = dao.listar();

			contentStream.setFont(fontRegular, fontSize);

			for (Mascota mascota : mascotas) {
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
				String[] data = { String.valueOf(mascota.getId_mascota()), mascota.getNombre(), mascota.getTipo(),
						mascota.getGenero(), mascota.getRaza(),String.valueOf(mascota.getCedula_cliente()) };

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

	// Método para truncar texto si excede el ancho de columna
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