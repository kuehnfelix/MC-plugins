package tk.wolfsbau.payback.db;
/**
 * enum for the options
 */
public enum Option {
    TEAMSIZE("teamSize"),

    DAILYTIME ("dailyTime"),
    STARTTIME ("startTime"),
    MAXVORPRODUZIERT ("maxvorproduziert"),

    BORDERSTARTSIZE ("borderStartSize"),
    BORDERSTARTSHRINKDAY("borderStartShrinkDay"),
    BORDERSHRINKAMOUNT("borderShrinkAmount"),
    BORDERSHRINKDELAYDAYS("borderShrinkDelayDays"),
    BORDERMINIMUMSIZE ("borderMinimumSize"),
    BORDERSIZE ("borderSize"),

    MITTEX ("mitteX"),
    MITTEZ("mitteY")
    ;

    private final String text;

    Option(String text) {
        this.text = text;
    }

    public String text(){return text;}

    public static Option fromString(String s) {
        switch(s) {
            case "teamSize":
                return TEAMSIZE;
            case "dailyTime":
                return DAILYTIME;
            case "startTime":
                return STARTTIME;
            case "maxvorproduziert":
                return MAXVORPRODUZIERT;
            case "borderStartSize":
                return BORDERSTARTSIZE;
            case "borderStartShrinkDay":
                return BORDERSTARTSHRINKDAY;
            case "borderShrinkAmount":
                return BORDERSHRINKAMOUNT;
            case "borderShrinkDelayDays":
                return BORDERSHRINKDELAYDAYS;
            case "borderMinimumSize":
                return BORDERMINIMUMSIZE;
            case "borderSize":
                return BORDERSIZE;
            case "mitteX":
                return MITTEX;
            case "mitteY":
                return MITTEZ;
            default:
                return null;
        }
    }
}
