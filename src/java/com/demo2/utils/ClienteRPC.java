package com.demo2.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author raul
 */
public class ClienteRPC {

    /**
     * @param args the command line arguments
     */
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;

    public ClienteRPC() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
    }

    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });

        return response.take();
    }

    public void close() throws IOException {
        connection.close();
    }

    public String consumirApi(String codigoAlumno) {
        ClienteRPC apli_client = null;
        String response = null;
        try {
            apli_client = new ClienteRPC();
            response = apli_client.call(codigoAlumno);
            response = "[" + response + "]";
            
            /*
            JsonParser parser = new JsonParser();

            JsonArray gsonArr = parser.parse(response).getAsJsonArray();

            // for each element of array
            for (JsonElement obj : gsonArr) {

                JsonObject gsonObj = obj.getAsJsonObject();

                int dniAlumno = gsonObj.get("dnialumno").getAsInt();
                int codigoAlumno = gsonObj.get("codigoalumno").getAsInt();
                String nombresAlumno = gsonObj.get("nombresalumno").getAsString();
                String apellidosAlumno = gsonObj.get("apellidosalumno").getAsString();
                String facultad = gsonObj.get("facultad").getAsString();
                String escuela = gsonObj.get("escuela").getAsString();
                String sexo = gsonObj.get("sexo").getAsString();
                String telefono = gsonObj.get("telefono").getAsString();
                String direccion = gsonObj.get("direccion").getAsString();
                //String tipoDato = gsonObj.get("tipoDato").getAsString();
                
                /*
                System.out.println(dniAlumno);
                System.out.println(codigoAlumno);
                System.out.println(nombresAlumno);
                System.out.println(apellidosAlumno);
                System.out.println(facultad);

                System.out.println(escuela);
                System.out.println(sexo);
                System.out.println(telefono);
                System.out.println(direccion);
                //System.out.println(tipoDato);
            }
             */
            //System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (apli_client != null) {
                try {
                    apli_client.close();
                } catch (IOException _ignore) {
                }
            }
        }
        return response;

    }
}
