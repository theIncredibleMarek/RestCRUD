package restcrud;

import com.google.gson.Gson;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.NotFoundException;
import facades.PersonFacade;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import model.Person;

/**
 * @author Lars
 */
public class RestCRUD
{

    static int port = 8080;
    static String ip = "127.0.0.1";
    static String publicFolder = "src/htmlFiles/";
    static String startFile = "index.html";
    static String filesUri = "/pages";

    public void run() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
    //REST Routes

        //HTTP Server Routes
        server.createContext(filesUri, new HandlerFileServer());
        server.createContext("/person", new HandlerPerson());

        server.start();
        System.out.println("Server started, listening on port: " + port);
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length >= 3)
        {
            port = Integer.parseInt(args[0]);
            ip = args[1];
            publicFolder = args[2];
        }
        new RestCRUD().run();
    }

    class HandlerPerson implements HttpHandler
    {

        PersonFacade facade = new PersonFacade(true);

        @Override
        public void handle(HttpExchange he) throws IOException
        {
            String response = "";
            int status = 200;
            String method = he.getRequestMethod().toUpperCase();
            switch (method)
            {
                case "GET":
                    try
                    {
                        String path = he.getRequestURI().getPath();
                        if (path.lastIndexOf("/") > 0)//person/id
                        {
                            int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                            response = facade.getPerson(id);
                        } else //person
                        {
                            response = facade.getPersons();
                        }
                    } catch (NumberFormatException nf)
                    {
                        response = "ID is not a number";
                        status = 406;
                    } catch (NotFoundException ex)
                    {
                        response = ex.getMessage();
                        status = 404;
                    }
                    break;
                case "POST":
                    InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String jsonQuery = br.readLine();
                    Person p = facade.addPerson(jsonQuery);
                    response = new Gson().toJson(p);

                    break;
                case "PUT":
                    break;
                case "DELETE":
                    break;
            }
            he.getResponseHeaders().add("Content-Type", "application/json");
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody())
            {
                os.write(response.getBytes());
            }
        }
    }

    class HandlerFileServer implements HttpHandler
    {

        @Override
        public void handle(HttpExchange he) throws IOException
        {
            int responseCode = 500;
            //Set initial error values if an un expected problem occurs
            String errorMsg = null;
            byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
            String mime = null;

            String requestedFile = he.getRequestURI().toString();
            String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
            try
            {
                String extension = f.substring(f.lastIndexOf("."));
                mime = getMime(extension);
                File file = new File(publicFolder + f);
                System.out.println(publicFolder + f);
                bytesToSend = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
                responseCode = 200;
            } catch (Exception e)
            {
                responseCode = 404;
                errorMsg = "<h1>404 Not Found</h1>No context found for request";
            }
            if (responseCode == 200)
            {
                Headers h = he.getResponseHeaders();
                h.set("Content-Type", mime);
            } else
            {
                bytesToSend = errorMsg.getBytes();
            }
            he.sendResponseHeaders(responseCode, bytesToSend.length);
            try (OutputStream os = he.getResponseBody())
            {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }

        private String getMime(String extension)
        {
            String mime = "";
            switch (extension)
            {
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
