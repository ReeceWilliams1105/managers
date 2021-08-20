/*
* Copyright contributors to the Galasa project 
*/
package dev.galasa.galasaecosystem.internal.properties;

import java.util.List;

import org.apache.commons.logging.LogConfigurationException;

import dev.galasa.framework.spi.cps.CpsProperties;
import dev.galasa.galasaecosystem.GalasaEcosystemManagerException;

/**
 * Galasa Ecosystem RunID Prefix
 * <p>
 * This property indicates what names will be given to the runid prefix of the ecosystem.
 * </p>
 * <p>
 * The property is:-<br>
 * <br>
 * galasaecosystem.runid.prefix=TEST{A-Z}{A-Z}
 * </p>
 * <p>
 * Can be a comma separated list of static or generated names, eg BOB1,BOB9,BOB5
 * </p>
 * <p>
 * default value is TEST{A-Z}{A-Z}
 * </p>
 * 
 * @author Michael Baylis
 *
 */
public class RunIdPrefix extends CpsProperties {

    public static List<String> get() throws GalasaEcosystemManagerException, LogConfigurationException {
        return getStringListWithDefault(GalasaEcosystemPropertiesSingleton.cps(), "TEST{A-Z}{A-Z}",
                "runid", "prefix");
    }

}
