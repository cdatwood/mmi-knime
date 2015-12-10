package com.pg.knime.vault;

import com.pg.knime.secure.DefaultVault;

public class FacebookVault extends DefaultVault {

    private static String APP_ID = "959814390733501";
    private static String APP_SECRET = "46ace08cce9c040eca0dbd080d83c252";
    //private static String APP_ID = "128490190584651";
    //private static String APP_SECRET = "ebcf28fe6505902c967f60ba40fbd7d0";

    public FacebookVault() {
            this.addKey("DEFAULT_APP_ID", APP_ID);
            this.addKey("DEFAULT_APP_SECRET", APP_SECRET);
    }
}