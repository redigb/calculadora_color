package com.redrd.calculadora_clase.utilidades;

public class Calculadora {
    private Double inputValue1 = 0.0;
    private Double inputValue2 = null;
    private Double resultado = null;
    private Operador operadorActual = null;
    private final StringBuilder ecuacion = new StringBuilder().append(Constantes.ZERO);

    public void agregarNumero(String numero) {
        if (ecuacion.toString().equals("0")) {
            ecuacion.setLength(0);
        }
        ecuacion.append(numero);
        setInput();
    }

    public void agregarOperador(Operador operador) {
        if (operadorActual != null) calcular();
        operadorActual = operador;
    }


    public void calcular() {
        if (inputValue2 == null || operadorActual == null) return;

        switch (operadorActual) {
            case SUMA:
                resultado = inputValue1 + inputValue2;
                break;
            case RESTA:
                resultado = inputValue1 - inputValue2;
                break;
            case MULTIPLICACION:
                resultado = inputValue1 * inputValue2;
                break;
            case DIVISION:
                if (inputValue2 == 0) throw new ArithmeticException("No se puede dividir por 0");
                resultado = inputValue1 / inputValue2;
                break;
        }
        inputValue1 = resultado;
        inputValue2 = null;
        operadorActual = null;
        ecuacion.setLength(0);
        ecuacion.append(resultado);
    }

    public void calcularPorcentaje() {
        if (inputValue2 == null) {
            double porcentaje = inputValue1 / 100;
            inputValue1 = porcentaje;
            ecuacion.setLength(0);
            ecuacion.append(porcentaje);
        } else {
            double porcentajeValor1 = (inputValue1 * inputValue2) / 100;
            double porcentajeValor2 = inputValue2 / 100;

            switch (operadorActual) {
                case SUMA:
                    resultado = inputValue1 + porcentajeValor1;
                    break;
                case RESTA:
                    resultado = inputValue1 - porcentajeValor1;
                    break;
                case MULTIPLICACION:
                    resultado = inputValue1 * porcentajeValor2;
                    break;
                case DIVISION:
                    if (porcentajeValor2 == 0)
                        throw new ArithmeticException("No se puede dividir por 0");
                    resultado = inputValue1 / porcentajeValor2;
                    break;
                default:
                    throw new IllegalArgumentException("Operador no reconocido: " + operadorActual);
            }

            ecuacion.setLength(0);
            ecuacion.append(resultado);
            inputValue1 = resultado;
            inputValue2 = null;
            operadorActual = null;
        }
    }

    public void borrarTodo() {
        inputValue1 = 0.0;
        inputValue2 = null;
        operadorActual = null;
        resultado = null;
        ecuacion.setLength(0);
        ecuacion.append("0");
    }

    public String getEcuacion() {
        return ecuacion.toString();
    }

    private void setInput() {
        if (operadorActual == null) {
            inputValue1 = Double.parseDouble(ecuacion.toString());
        } else {
            inputValue2 = Double.parseDouble(ecuacion.toString());
        }
    }
}
