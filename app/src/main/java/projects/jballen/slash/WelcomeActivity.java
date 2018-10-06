package projects.jballen.slash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class WelcomeActivity extends AppCompatActivity {
    private Button playButton;
    private CheckBox colorblindButton;
    private Button instructionsButton;
    public static final String COLORBLIND_MODE = "colorblindModeEnabled";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGame();
            }
        });
        colorblindButton = findViewById(R.id.colorblindModeButton);
        colorblindButton.setChecked(getIntent().getBooleanExtra(COLORBLIND_MODE, false));
        instructionsButton = findViewById(R.id.instructionsButton);
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToInstructions();
            }
        });
    }
    private void goToGame() {
        Intent goToGameIntent = new Intent(this, GameActivity.class);
        goToGameIntent.putExtra(COLORBLIND_MODE, colorblindButton.isChecked());
        startActivity(goToGameIntent);
    }
    private void goToInstructions() {
        Intent goToInstructionsIntent = new Intent(this, InstructionsActivity.class);
        goToInstructionsIntent.putExtra(COLORBLIND_MODE, colorblindButton.isChecked());
        startActivity(goToInstructionsIntent);
    }
}
