package projects.jballen.slash;

public interface GameCallbackInterface {
    void setArrow(ArrowAttributes attributes);
    void updateProgressBar(int newValue);
    void updateScore(int newValue);
    boolean isColorblind();
    void goToGameOver(int finalScore);
    void playSuccessSound(GameService.ArrowType newType);
    void playFailureSound();
    void updateAlpha(int newAlpha);
    void setFirstArrow(ArrowAttributes attributes);
    void setBorder(int borerColor);
}
