package tk.fireware.knockffa;

import java.util.UUID;

public class Damage {
    private UUID damager;
    private long time;

    public Damage(UUID damager, long time) {
        this.setTime(time);
        this.setDamager(damager);
    }

    public UUID getDamager() {
        return damager;
    }

    public void setDamager(UUID damager) {
        this.damager = damager;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;

    }
}
