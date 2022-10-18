package wkimenai.mc-lights.setup;

import wkimenai.mc-lights.database.data.LazerData;
import wkimenai.mc-lights.database.data.EffectData;
import wkimenai.mc-lights.database.data.StaffData;
import wkimenai.mc-lights.database.data.GeneralData;
import java.io.File;
import wkimenai.mc-lights.mc-lights;

public class SetupDatabase
{
    public SetupDatabase(final mc-lights mc-lights) {
        final File file = new File(String.valueOf(mc-lights.getDataFolder().getPath()) + "/data");
        if (!file.exists()) {
            file.mkdir();
        }
        new GeneralData().registerData();
        new StaffData().deserializeData();
        new EffectData().deserializeData();
        new LazerData().deserializeData();
    }
}
