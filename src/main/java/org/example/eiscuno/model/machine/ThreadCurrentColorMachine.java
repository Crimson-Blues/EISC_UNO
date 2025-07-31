package org.example.eiscuno.model.machine;

import org.example.eiscuno.listener.CurrentColorListener;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.table.Table;

public class ThreadCurrentColorMachine implements Runnable{

    private GameUno gameUno;
    private Table table;
    private volatile boolean running;
    private CurrentColorListener currentColorListener;
    private boolean firstTextShown;

    /**
     * Thread responsible for saving the current color of the card.
     * It periodically checks whether the color of the previous and the current cards have changed, and if so it saves it.
     */
    public ThreadCurrentColorMachine(GameUno gameUno, Table table) {
        this.gameUno = gameUno;
        running = true;
        this.table = table;
        firstTextShown = false;
    }

    /**
     * Main execution loop of the thread.
     * Continuously runs while {@code running} is true. If the color of the card has changed,
     * the machine waits for a short period before saving it.
     */
    @Override
    public void run() {
        while(running){
            try {
                String previousColor = table.getpreviousCardOnTheTable().getColor();
                String actualColor = table.getCurrentCardOnTheTable().getColor();
                if (previousColor != actualColor) {
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gameUno.setCurrentColor();

                    if (currentColorListener != null) {
                        currentColorListener.onColorChanged();
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                if(!firstTextShown){
                    System.out.println("Primera carta! ");
                    firstTextShown = true;
                }
            }
        }
    }

    /**
     * Sets the listener to be triggered when the color of the cards have changed.
     *
     * @param currentColorListener the listener to handle COLOR-related behavior
     */
    public void setCurrentColorListener(CurrentColorListener currentColorListener) {
        this.currentColorListener = currentColorListener;
    }

    /**
     * Sets the boolean running false, therefore, ends the run loop
     */
    public void stopThread() {
        this.running = false;
    }
}
