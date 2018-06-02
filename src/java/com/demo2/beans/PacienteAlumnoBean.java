/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo2.beans;

import com.demo2.dao.PacienteAlumnoDAO;
import com.demo2.models.PacienteAlumno;
import com.demo2.utils.ClienteRPC;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.client.Client;

/**
 *
 * @author raul
 */
@ManagedBean(name = "pacienteAlumnoBean")
@ViewScoped
public class PacienteAlumnoBean implements Serializable {

    private int dniAlumno;
    private int codigoAlumno;
    private String nombresAlumno;
    private String apellidosAlumno;
    private String facultad;
    private String escuela;
    private String sexo;
    private String telefono;
    private String direccion;
    private int tipoDescuento;
    private String response;
    ClienteRPC clienteRPC;
    private String msjdataalumno;
    private String msjdsctoalumno;
    
    public PacienteAlumnoBean() throws IOException, TimeoutException {
        clienteRPC = new ClienteRPC();
    }

    public void addPacienteAlumno() {
        PacienteAlumno pacienteAlumno = new PacienteAlumno(dniAlumno, codigoAlumno, nombresAlumno, apellidosAlumno, facultad, escuela, sexo, telefono, direccion, tipoDescuento);
        PacienteAlumnoDAO pacienteAlumnoDAO = new PacienteAlumnoDAO();
        pacienteAlumnoDAO.addPacienteAlumno(pacienteAlumno);
        limpiarCampos();
    }

    public void cargarAlumno() throws IOException, TimeoutException {

        //ClienteRPC clienteRPC =  new ClienteRPC();
        response = clienteRPC.consumirApi("1-" + codigoAlumno);  
        System.out.println(response);
        
        String[] parts = response.split(" ");
        String part1 = parts[0];
        
        if (part1.equals("[AttributeError")) {
            msjdataalumno = "Alumno no encontrado";
            limpiarCampos();
            
        } else {
            msjdataalumno = "";
            JsonParser parser = new JsonParser();

            JsonArray gsonArr = parser.parse(response).getAsJsonArray();

            // for each element of array
            for (JsonElement obj : gsonArr) {

                JsonObject gsonObj = obj.getAsJsonObject();

                dniAlumno = gsonObj.get("dnialumno").getAsInt();
                codigoAlumno = gsonObj.get("codigoalumno").getAsInt();
                nombresAlumno = gsonObj.get("nombresalumno").getAsString();
                apellidosAlumno = gsonObj.get("apellidosalumno").getAsString();
                facultad = gsonObj.get("facultad").getAsString();
                escuela = gsonObj.get("escuela").getAsString();
                sexo = gsonObj.get("sexo").getAsString();
                telefono = gsonObj.get("telefono").getAsString();
                direccion = gsonObj.get("direccion").getAsString();
            }

        }

    }

    public void cargarDescuento() throws IOException, TimeoutException {
        //ClienteRPC clienteRPC1 =  new ClienteRPC();

        response = clienteRPC.consumirApi("2-" + dniAlumno);
        System.out.println(response);
        
        if (response.equals("[{\"detail\":\"Not found.\"}]")) {
            msjdataalumno = "Alumno no encontrado";
            limpiarCampos();
        }
        else{
            JsonParser parser = new JsonParser();

        JsonArray gsonArr = parser.parse(response).getAsJsonArray();

        // for each element of array
        for (JsonElement obj : gsonArr) {

            JsonObject gsonObj = obj.getAsJsonObject();

            tipoDescuento = gsonObj.get("tipodescuento").getAsInt();

        }
        }
        

    }

    public void limpiarCampos() {
        dniAlumno = 0;
        codigoAlumno = 0;
        nombresAlumno = "";
        apellidosAlumno = "";
        facultad = "";
        escuela = "";
        sexo = "";
        telefono = "";
        direccion = "";
        tipoDescuento = 0;
    }

    public void returnPacienteAlumnoById() {

    }

    public int getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(int dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public int getCodigoAlumno() {
        return codigoAlumno;
    }

    public void setCodigoAlumno(int codigoAlumno) {
        this.codigoAlumno = codigoAlumno;
    }

    public String getNombresAlumno() {
        return nombresAlumno;
    }

    public void setNombresAlumno(String nombresAlumno) {
        this.nombresAlumno = nombresAlumno;
    }

    public String getApellidosAlumno() {
        return apellidosAlumno;
    }

    public void setApellidosAlumno(String apellidosAlumno) {
        this.apellidosAlumno = apellidosAlumno;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(int tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMsjdataalumno() {
        return msjdataalumno;
    }

    public void setMsjdataalumno(String msjdataalumno) {
        this.msjdataalumno = msjdataalumno;
    }

    public String getMsjdsctoalumno() {
        return msjdsctoalumno;
    }

    public void setMsjdsctoalumno(String msjdsctoalumno) {
        this.msjdsctoalumno = msjdsctoalumno;
    }
    
    
    
    
}
