package models;

/**
 * The type Config.
 */
public class Config {
    private String adresseServeur;
    private int portServeur;
    private String username;
    private boolean serveurAmeliore;


    /**
     * Instantiates a new Config.
     *
     * @param adresseServeur the server address
     * @param portServeur    the server port
     * @param username       the username
     */
    public Config(String adresseServeur, int portServeur, String username) {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
        this.username = username;
    }

    /**
     * Instantiates a new Config.
     */
    public Config(){
        defaultConfig();
    }

    private void defaultConfig(){
        adresseServeur = "tresor.josso.fr";
        portServeur = 7236;
        serveurAmeliore = true;

    }


    /**
     * Is serveur ameliore boolean.
     *
     * @return the boolean
     */
    public boolean isServeurAmeliore() {
        return serveurAmeliore;
    }

    /**
     * Sets serveur ameliore.
     *
     * @param serveurAmeliore the serveur ameliore
     */
    public void setServeurAmeliore(boolean serveurAmeliore) {
        this.serveurAmeliore = serveurAmeliore;
    }

    /**
     * Instantiates a new Config.
     *
     * @param username the username
     */
    public Config(String username) {
        defaultConfig();
        this.username = username;
    }

    /**
     * Gets the server address.
     *
     * @return the server address
     */
    public String getAdresseServeur() {
        return adresseServeur;
    }

    /**
     * Sets the server address.
     *
     * @param adresseServeur the server address
     */
    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    /**
     * Gets server port.
     *
     * @return the server port
     */
    public int getPortServeur() {
        return portServeur;
    }

    /**
     * Sets the server port.
     *
     * @param portServeur the server port
     */
    public void setPortServeur(int portServeur) {
        this.portServeur = portServeur;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Reset server config.
     */
    public void resetServerConfig(){
        defaultConfig();
    }


}

