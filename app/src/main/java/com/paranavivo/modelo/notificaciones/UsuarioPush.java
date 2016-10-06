package com.paranavivo.modelo.notificaciones;

/**
 * Created by Claudio on 04/06/2016.
 */
public class UsuarioPush{

    private String nombre;
    private String apellido;
    private String email;
    private String nrocta;
    private String imei;
    private String idRegistro;
    private int codRubro01;


    public UsuarioPush() {
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNrocta() {
        return nrocta;
    }

    public void setNrocta(String nrocta) {
        this.nrocta = nrocta;
    }

    public int getCodRubro01() {
        return codRubro01;
    }

    public void setCodRubro01(int codRubro01) {
        this.codRubro01 = codRubro01;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
