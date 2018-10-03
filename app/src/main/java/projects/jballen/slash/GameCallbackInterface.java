package projects.jballen.slash;

public interface GameCallbackInterface {
    void setArrow(ArrowAttributes attributes);
    void updateProgressBar(int newValue);
    void updateScore(int newValue);
}
