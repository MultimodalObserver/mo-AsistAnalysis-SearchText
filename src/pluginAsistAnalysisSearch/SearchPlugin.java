/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginAsistAnalysisSearch;

import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mo.analysis.AnalysisProvider;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;

/**
 *
 * @author Lathy
 */

@Extension(
    xtends = {
        @Extends(extensionPointId = "mo.analysis.AnalysisProvider")
    }
)

public class SearchPlugin  implements AnalysisProvider{
    private List<Configuration> configurations; 
    private final static String PLUGIN_NAME = "Assistant Analysis - Search Text";
    public final static Logger logger = Logger.getLogger(SearchPlugin.class.getName());
    ResourceBundle dialogBundle = java.util.ResourceBundle.getBundle("properties/principal");
    int numberConfiguration = 1;
    
    
    public SearchPlugin(){
        configurations = new ArrayList<>();
        
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization po) {
        JFrame frame = new JFrame("CloudTags");
        //custom title, warning icon
        JOptionPane.showMessageDialog(frame,
            dialogBundle.getString("SeAccedeAUnPlugin"),
            "Information",
        JOptionPane.INFORMATION_MESSAGE);
        
        
        SearchConfiguration c = new SearchConfiguration();
        
        c.setId("Configuration Search "+numberConfiguration);
        configurations.add(c);
        numberConfiguration++;
            
        return c;
    
    
    }
    

    @Override
    public List<Configuration> getConfigurations() {
         return configurations;
    }

    @Override
    public StagePlugin fromFile(File file) {
        if (file.isFile()) {
            try {
                SearchPlugin mc = new SearchPlugin();
                XElement root = XIO.readUTF(new FileInputStream(file));
                XElement[] pathsX = root.getElements("path");
                for (XElement pathX : pathsX) {
                    String path = pathX.getString();
                    SearchConfiguration c = new SearchConfiguration();
                    Configuration config = c.fromFile(new File(file.getParentFile(), path));
                    if (config != null) {
                        mc.configurations.add(config);
                    }
                }
                return mc;
            } catch (IOException ex) {
                
            }
        }
        return null;
        
    }

    @Override
    public File toFile(File parent) {
        
        return null;
    }
    
}
