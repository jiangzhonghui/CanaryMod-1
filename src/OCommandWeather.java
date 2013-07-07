import java.util.List;
import java.util.Random;

public class OCommandWeather extends OCommandBase {

    public OCommandWeather() {}

    public String c() {
        return "weather";
    }

    public int a() {
        return 2;
    }

    public String c(OICommandSender oicommandsender) {
        return "commands.weather.usage";
    }

    public void b(OICommandSender oicommandsender, String[] astring) {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (astring.length >= 2) {
                i = a(oicommandsender, astring[1], 1, 1000000) * 20;
            }

            OWorldServer oworldserver = OMinecraftServer.F().getWorld(oicommandsender.f_().name, 0); // CanaryMod - multiworld fix
            OWorldInfo oworldinfo = oworldserver.N();

            oworldinfo.g(i);
            oworldinfo.f(i);
            if ("clear".equalsIgnoreCase(astring[0])) {
                oworldinfo.b(false);
                oworldinfo.a(false);
                a(oicommandsender, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(astring[0])) {
                oworldinfo.b(true);
                oworldinfo.a(false);
                a(oicommandsender, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(astring[0])) {
                    throw new OWrongUsageException("commands.weather.usage", new Object[0]);
                }

                oworldinfo.b(true);
                oworldinfo.a(true);
                a(oicommandsender, "commands.weather.thunder", new Object[0]);
            }
        } else {
            throw new OWrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    public List a(OICommandSender oicommandsender, String[] astring) {
        return astring.length == 1 ? a(astring, new String[] { "clear", "rain", "thunder"}) : null;
    }
}
