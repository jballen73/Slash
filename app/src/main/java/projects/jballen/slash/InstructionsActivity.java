package projects.jballen.slash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {
    private GameArrow regularExampleArrow;
    private GameArrow reverseExampleArrow;
    private GameArrow notExampleArrow;
    private Button backToMainMenuButton;
    private TextView notArrowInstructions;
    private boolean isColorblind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        isColorblind = getIntent().getBooleanExtra(WelcomeActivity.COLORBLIND_MODE, false);
        regularExampleArrow = findViewById(R.id.exampleRegularArrow);
        regularExampleArrow.setDirection(FlingType.RIGHT.ordinal());
        regularExampleArrow.invalidate();
        reverseExampleArrow = findViewById(R.id.exampleReverseArrow);
        reverseExampleArrow.setDirection(FlingType.LEFT.ordinal());
        reverseExampleArrow.setColor(Color.GREEN);
        reverseExampleArrow.invalidate();
        notExampleArrow = findViewById(R.id.exampleNotArrow);
        notExampleArrow.setDirection(FlingType.RIGHT.ordinal());
        notExampleArrow.setColor(isColorblind ? Color.MAGENTA : Color.RED);
        notExampleArrow.invalidate();
        notArrowInstructions = findViewById(R.id.notArrowInstructions);
        notArrowInstructions.setText(getString(R.string.not_arrow_instructions ,(isColorblind ? "magenta" : "red")));
        backToMainMenuButton = findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu();
            }
        });
    }
    private void backToMainMenu() {
        Intent backToMainMenuIntent = new Intent(this, WelcomeActivity.class);
        backToMainMenuIntent.putExtra(WelcomeActivity.COLORBLIND_MODE, isColorblind);
        startActivity(backToMainMenuIntent);
    }
}
