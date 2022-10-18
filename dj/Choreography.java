package wkimenai.mc-lights.dj;

import org.bukkit.entity.ArmorStand;

public abstract class Choreography
{
    private boolean loop;
    protected int timer;
    protected ArmorStand dj;
    
    public Choreography(final Dj dj) {
        this.loop = false;
        this.timer = 5;
        this.dj = dj.getDj();
    }
    
    public abstract void updateChoreography();
    
    public void updateTimer() {
        if (!this.loop) {
            this.timer += 10;
            if (this.timer >= 20) {
                this.loop = true;
            }
        }
        else {
            this.timer -= 10;
            if (this.timer <= 5) {
                this.loop = false;
            }
        }
    }
}
