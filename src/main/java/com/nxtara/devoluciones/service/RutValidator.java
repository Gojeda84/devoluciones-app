package com.nxtara.devoluciones.service;

import org.springframework.stereotype.Component;

@Component
public class RutValidator {

    public boolean esValido(String rut) {
        if (rut == null || !rut.matches("^\\d{7,8}-[0-9kK]$")) {
            return false;
        }
        String[] partes = rut.split("-");
        String numero = partes[0];
        char dv = Character.toUpperCase(partes[1].charAt(0));

        return calcularDv(numero) == dv;
    }

    private char calcularDv(String numero) {
        int m = 0, s = 1;
        int t = Integer.parseInt(numero);
        for (; t != 0; t /= 10) {
            s = (s + t % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) (s != 0 ? s + 47 : 'K');
    }
}