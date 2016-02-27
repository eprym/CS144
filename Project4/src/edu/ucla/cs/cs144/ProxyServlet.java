package edu.ucla.cs.cs144;

import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String targetUrl="http://google.com/complete/search?output=toolbar&q="+request.getParameter("q");

        URL url=null;
        BufferedReader reader=null;
        StringBuilder sb=null;
		ServletOutputStream os=null;

        try{
        	url=new URL(targetUrl);
        	HttpURLConnection connection=(HttpURLConnection)url.openConnection();

        	connection.setRequestMethod("GET");
        	connection.setReadTimeout(5000);
        	connection.connect();

        	reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

        	sb=new StringBuilder();
        	//sb.append("Powered by Google:");
        	//sb.append(targetUrl);


        	response.setContentType("text/xml");
        	response.setStatus(200);
        	os=response.getOutputStream();
        	// os.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        	String line=null;
        	while((line=reader.readLine())!=null){
        		//os.println(line);
        		sb.append(line+"\n");
        	}
			os.println(sb.toString());
        	os.flush();
        	request.setAttribute("suggests",sb.toString());
        	request.getRequestDispatcher("/suggest.jsp").forward(request,response);
        	
        	




        }catch(Exception e){
        	e.printStackTrace();
        	throw e;
        }
        finally{
        	if(reader!=null){
        		try{
        			reader.close();
        		}catch(Exception e){
        			e.printStackTrace();
        		}
        	}
        	if(os!=null){
        		try{
        			os.close();
        		}catch(Exception e){
        			e.printStackTrace();
        		}
        	}
        }
    }
}
