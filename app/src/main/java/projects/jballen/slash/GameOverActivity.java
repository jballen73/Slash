package projects.jballen.slash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private Button backToMainMenuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        scoreTextView = findViewById(R.id.gameOverScore);
        scoreTextView.setText(getString(R.string.game_score, getIntent().getIntExtra(GameActivity.FINAL_GAME_SCORE, 0)));
        backToMainMenuButton = findViewById(R.id.gameOverBackToWelcomeButton);
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu();
            }
        });
    }
    private void backToMainMenu() {
        Intent backToMainMenuIntent = new Intent(this, WelcomeActivity.class);
        startActivity(backToMainMenuIntent);
    }
}
