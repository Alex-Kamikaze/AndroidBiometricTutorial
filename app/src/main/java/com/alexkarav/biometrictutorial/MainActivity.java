package com.alexkarav.biometrictutorial;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.alexkarav.biometrictutorial.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    //Как подключается binding не буду расписывать, это все итак знают :)
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Описание диалогового окна, которые появляется при сканировании отпечатка пальца
        BiometricPrompt.PromptInfo prompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Тестовая авторизация")
                .setDescription("Проверка работы сканера отпечатка пальцев")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG) //BIOMETRIC_STRONG - аутентификация с использованием аппаратных средств биометрии (сканирование лица, отпечаток пальца и.т.д)
                .setNegativeButtonText("Отмена")
                .build();
        Executor executor = this.getMainExecutor(); //Нужно для того чтобы основной поток приложения не блокировался окном аутентификации

        //Класс обработчик авторизации
        BiometricPrompt biometricAuthorization = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                //Обработчик ошибок (нет устройств для сканирования, отмена сканирования и.т.д)
                super.onAuthenticationError(errorCode, errString);
                binding.authStatusText.setText(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                //Обработка удачной авторизации
                super.onAuthenticationSucceeded(result);
                binding.authStatusText.setText("Вы успешно авторизованы!");
            }

            @Override
            public void onAuthenticationFailed() {
                //Обработка провала авторизации (отпечаток не совпал с правильным, лицо не совпадает с нужным)
                super.onAuthenticationFailed();
                binding.authStatusText.setText("Авторизация провалена!");
            }
        });
        //Вызов метода для открытия окна с авторизацией
        binding.authButton.setOnClickListener(view -> biometricAuthorization.authenticate(prompt));

    }
}