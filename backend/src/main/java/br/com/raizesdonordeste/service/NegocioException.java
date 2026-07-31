package br.com.raizesdonordeste.service;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
