package com.redrd.calculadora_clase;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.redrd.calculadora_clase.databinding.ActivityMainBinding;
import com.redrd.calculadora_clase.utilidades.Constantes;
import com.redrd.calculadora_clase.utilidades.Operador;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Double inputValue1 = 0.0;
    private Double inputValue2 = null;
    private Double result = null;
    private Operador currentOperador = null;
    private final StringBuilder ecuacion = new StringBuilder().append(Constantes.ZERO);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Establece la vista correcta!
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setListenerLa();
        setNightModeIndicator();
    }

    private void setListenerLa() {
        // Acortacion por funcion lambda
        for (Button button : getNumericButtons()) {
            button.setOnClickListener(v -> onNumberClicked(button.getText().toString()));
        }
        binding.buttonCero.setOnClickListener(v -> onZeroClicked());
        binding.buttonDobleCero.setOnClickListener(v -> onDobleZeroClicked());
        binding.buttonDecimalPunto.setOnClickListener(v -> onDecimalPunto());
        binding.buttonSuma.setOnClickListener(v -> onOperatorClicked(Operador.SUMA));
        binding.buttonResta.setOnClickListener(v -> onOperatorClicked(Operador.RESTA));
        binding.buttonMultiplicacion.setOnClickListener(v -> onOperatorClicked(Operador.MULTIPLICACION));
        binding.buttonDivision.setOnClickListener(v -> onOperatorClicked(Operador.DIVISION));
        binding.buttonIgual.setOnClickListener(v -> onIgualClicked());
        binding.buttonAllClear.setOnClickListener(v -> onAllClearClicked());
        binding.buttonPlusMinus.setOnClickListener(v -> onPlusMinusClicked());
        binding.buttonPorcentaje.setOnClickListener(v -> onPercentageClicked());
        binding.imageNightMode.setOnClickListener(v -> toggleNightMode());
        /*binding.buttonCero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onZeroClicked();
            }
        });

        binding.buttonDobleCero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDobleZeroClicked();
            }
        });*/
    }

    // Explicacion del listener - sin lambda
    private void setListeners() {
        for (Button button : getNumericButtons()) {
            // final String buttonText = button.getText().toString();  // Guardar el valor en una variable final
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MainActivity", "Botón presionado: " + button.getText().toString());
                    onNumberClicked(button.getText().toString());
                }
            });
        }
    }

    private void toggleNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    private void setNightModeIndicator(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            binding.imageNightMode.setImageResource(R.drawable.contrast);
        } else {
            binding.imageNightMode.setImageResource(R.drawable.half_moon);
        }
    }

    private void onPercentageClicked(){
        if (inputValue2 == null){
            double percentage = getInputValue1() / 100;
            inputValue1 = percentage;
            ecuacion.setLength(0);
            ecuacion.append(percentage);
            updateInputDisplay();
        }else {
            double percentageOfValue1 = (getInputValue1() * getInputValue2()) / 100;
            double percentageOfValue2 = getInputValue2() / 100;
            switch (currentOperador) {
                case SUMA:
                    result = getInputValue1() + percentageOfValue1;
                    break;
                case RESTA:
                    result = getInputValue1() - percentageOfValue1;
                    break;
                case MULTIPLICACION:
                    result = getInputValue1() * percentageOfValue2;
                    break;
                case DIVISION:
                    result = getInputValue1() / percentageOfValue2;
                    break;

                default:
                    throw new IllegalArgumentException("Operador no reconocido: " + currentOperador);
            }
            ecuacion.setLength(0);
            ecuacion.append(Constantes.ZERO);
            updateResultOnDisplay(true);
            inputValue1 = result;
            result = null;
            inputValue2 = null;
            currentOperador = null;
        }
    }

    private void onPlusMinusClicked(){
        if (ecuacion.toString().startsWith(Constantes.MINUS)){
            ecuacion.deleteCharAt(0);
        } else {
            ecuacion.insert(0, Constantes.MINUS);
        }
        setInput();
        updateInputDisplay();
    }

    private void onAllClearClicked(){
        inputValue1 = 0.0;
        inputValue2 = null;
        currentOperador = null;
        result = null;
        ecuacion.setLength(0);
        ecuacion.append(Constantes.ZERO);
        clearDisplay();
    }

    private void onOperatorClicked(Operador operador){
        onIgualClicked();
        currentOperador = operador;
    }

    private void onIgualClicked() {
        if (inputValue2 != null) {
            result = calcular();
            if (result != null) {
                ecuacion.setLength(0);
                ecuacion.append(result);
                updateResultOnDisplay(false);
                inputValue1 = result;
            }
            result = null;
            inputValue2 = null;
            currentOperador = null;
        } else {
            ecuacion.setLength(0);
            ecuacion.append(Constantes.ZERO);
        }
    }

    private Double calcular() {
        if (currentOperador == null) {
            mostrarError("⚠️ Error: No se ha seleccionado un operador.");
            return null;
        }
        try {
            double val1 = getInputValue1();
            double val2 = getInputValue2();

            switch (currentOperador) {
                case SUMA:
                    return val1 + val2;

                case RESTA:
                    return val1 - val2;

                case MULTIPLICACION:
                    return val1 * val2;

                case DIVISION:
                    if (val2 == 0.0) {
                        mostrarError("❌ No se puede dividir entre cero.");
                        return null; // Retornar null para evitar errores
                    }
                    return val1 / val2;

                default:
                    mostrarError("❌ Error: Operador desconocido.");
                    return null;
            }

        } catch (Exception e) {
            mostrarError("🚨 Error inesperado en el cálculo.");
            Log.e("MainActivity", "Error en cálculo", e);
            return null;
        }
    }

    private void mostrarError(String mensaje) {
        binding.textInput.setText(mensaje);
        binding.textEquation.setText(null);
        textViewErrorInput(mensaje);
    }

    private void textViewErrorInput(String mensaje) {
        TextView textInput = findViewById(R.id.textInput);
        textInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textInput.setTextColor(ContextCompat.getColor(this, R.color.error_text));
        textInput.setText(mensaje);
    }

    private void onDecimalPunto(){
        normalizedTextInput();
        if (ecuacion.toString().contains(Constantes.PUNTO_DECIMAL)) return;
        ecuacion.append(Constantes.PUNTO_DECIMAL);
        setInput();
        updateInputDisplay();
    }

    private void onZeroClicked(){
        normalizedTextInput();
        setInput();
        updateInputDisplay();
        if (ecuacion.length() == 1 && ecuacion.toString().equals(Constantes.ZERO)) return;
        onNumberClicked(Constantes.ZERO);
    }

    private void onDobleZeroClicked(){
        if (ecuacion.toString().startsWith(Constantes.ZERO)) return;
        onNumberClicked(Constantes.DOBLE_ZERO);
    }

    private Button[] getNumericButtons() {
        return new Button[] {
                binding.buttonUno,
                binding.buttonDos,
                binding.buttonTres,
                binding.buttonCuatro,
                binding.buttonCinco,
                binding.buttonSeis,
                binding.buttonSiete,
                binding.buttonOcho,
                binding.buttonNueve
        };
    }

    private void onNumberClicked(String numberText){
        normalizedTextInput();
        if (ecuacion.toString().startsWith(Constantes.ZERO)) {
            ecuacion.deleteCharAt(0);
        } else if (ecuacion.toString().startsWith(Constantes.MINUS + Constantes.ZERO)) {
            ecuacion.deleteCharAt(1);
        }
        ecuacion.append(numberText);
        setInput();
        updateInputDisplay();
    }

    private void setInput(){
        if (currentOperador == null) {
            inputValue1 = Double.parseDouble(ecuacion.toString());
        } else {
            inputValue2 = Double.parseDouble(ecuacion.toString());
        }
    }

    private void clearDisplay(){
        binding.textInput.setText(getFormatDisplayValue(getInputValue1()));
        binding.textEquation.setText(null);
    }

    private void updateResultOnDisplay(boolean isPercentage) {
        // Asignar el texto a textInput con el valor formateado de result
        binding.textInput.setText(getFormatDisplayValue(result));
        // Obtener el texto formateado de input2
        String input2Text = getFormatDisplayValue(getInputValue2());
        // Si es un porcentaje, agregar el símbolo de porcentaje al texto
        if (isPercentage) { input2Text = input2Text + getString(R.string.percentage);}
        // Formatear y asignar el texto a textEquation
        binding.textEquation.setText(String.format(
                "%s %s %s",
                getFormatDisplayValue(getInputValue1()),
                getOperadorSymbolo(),
                input2Text
        ));
    }

    private void updateInputDisplay() {
        if (result == null) { binding.textEquation.setText(null);}
        binding.textInput.setText(ecuacion);
    }

    private Double getInputValue1() { return (inputValue1 != null) ? inputValue1 : 0.0; }

    private Double getInputValue2() { return (inputValue2 != null) ? inputValue2 : 0.0; }

    private String getOperadorSymbolo(){
        if (currentOperador == null) {throw new NullPointerException("currentOperador no puede ser nulo");}
        switch (currentOperador) {
            case SUMA:
                return getString(R.string.addition);
            case RESTA:
                return getString(R.string.substracion);
            case MULTIPLICACION:
                return getString(R.string.multiplicaton);
            case DIVISION:
                return getString(R.string.division);

            default:
                throw new IllegalArgumentException("Operador desconocido");
        }
    }

    private void normalizedTextInput(){
        TextView textInput = findViewById(R.id.textInput);
        textInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
        textInput.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
        textInput.setText(String.valueOf(result));
    }

    private String getFormatDisplayValue(Double value){
        if (value == null) {
            return null;
        }
        if (value % 1 == 0.0) {
            return Integer.toString(value.intValue());
        } else {
            return value.toString();
        }
    }
}

