package restcrud;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * @author Lars
 */
public class RestCRUD {

  static int port = 8080;
  static String ip = "127.0.0.1";
  static String publicFolder = "src/htmlFiles/";
  static String startFile = "index.html";
  static String filesUri = "/pages";

  public void run() throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
    //REST Routes
    
    //HTTP Server Routes
    server.createContext(filesUri, new HandlerFileServer());
       
    server.start();
    System.out.println("Server started, listening on port: " + port);
  }

  public static void main(String[] args) throws Exception {
    if (args.length >= 3) {
      port = Integer.parseInt(args[0]);
      ip = args[1];
      publicFolder = args[2];
    }
    new RestCRUD().run();
  }
  
  class HandlerFileServer implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
      int responseCode = 500;
      //Set initial error values if an un expected problem occurs
      String errorMsg = null;
      byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
      String mime = null;

      String requestedFile = he.getRequestURI().toString();
      String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
      try {
        String extension = f.substring(f.lastIndexOf("."));
        mime = getMime(extension);
        File file = new File(publicFolder + f);
        System.out.println(publicFolder + f);
        bytesToSend = new byte[(int) file.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(bytesToSend, 0, bytesToSend.length);
        responseCode = 200;
      } catch (Exception e) {
        responseCode = 404;
        errorMsg = "<h1>404 Not Found</h1>No context found for request";
      }
      if (responseCode == 200) {
        Headers h = he.getResponseHeaders();
        h.set("Content-Type", mime);
      } else {
        bytesToSend = errorMsg.getBytes();
      }
      he.sendResponseHeaders(responseCode, bytesToSend.length);
      try (OutputStream os = he.getResponseBody()) {
        os.write(bytesToSend, 0, bytesToSend.length);
      }
    }

    private String getMime(String extension) {
      String mime = "";
      switch (extension) {
        case ".pdf":
          mime = "application/pdf";
          break;
        case ".png":
          mime = "image/png";
          break;
        case ".js":
          mime = "text/javascript";
          break;
        case ".html":
          mime = "text/html";
          break;
        case ".jar":
          mime = "application/java-archive";
          break;
      }
      return mime;
    }
  }
}
