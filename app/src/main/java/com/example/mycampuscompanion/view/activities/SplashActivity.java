package com.example.mycampuscompanion.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampuscompanion.R;

/**
 * Activité de démarrage (SplashScreen) avec animations
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 secondes

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private TextView textViewSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialiser les vues
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);
        textViewSlogan = findViewById(R.id.textViewSlogan);

        // Démarrer les animations
        startAnimations();

        // Rediriger vers MainActivity après 3 secondes
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            // Animation de transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }

    /**
     * Démarrer toutes les animations
     */
    private void startAnimations() {
        // Animation du logo (scale + fade in)
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        imageViewLogo.startAnimation(scaleAnimation);

        // Animation du nom (slide up)
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUpAnimation.setStartOffset(500); // Démarre après 500ms
        textViewAppName.startAnimation(slideUpAnimation);

        // Animation du slogan (fade in)
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInAnimation.setStartOffset(800); // Démarre après 800ms
        textViewSlogan.startAnimation(fadeInAnimation);
    }
}