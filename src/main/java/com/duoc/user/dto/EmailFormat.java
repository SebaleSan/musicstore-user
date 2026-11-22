package com.duoc.user.dto;

public final class EmailFormat {
    public static final String REGEXP = "^[^\\s@]+@[^\\s@]+\\.[A-Za-z]{2,}$";
    public static final String MESSAGE = "El correo debe tener un formato válido, por ejemplo nombre@dominio.cl";

    private EmailFormat() {
    }
}
