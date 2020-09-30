package tk.wolfsbau.payback.gameState;

public class GameState {
    public static IGameState PREPARING = new GameState_preparing();
    public static IGameState RUNNING = new GameState_running();
    public static IGameState FINISHED = new GameState_finished();
}
