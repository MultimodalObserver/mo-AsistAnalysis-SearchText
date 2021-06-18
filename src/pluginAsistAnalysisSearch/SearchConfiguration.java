/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginAsistAnalysisSearch;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mo.analysis.NotesPlayer;
import mo.analysis.NotesRecorder;
import mo.analysis.PlayableAnalyzableConfiguration;
import mo.core.ui.GridBConstraints;
import mo.core.ui.Utils;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.visualization.Playable;

/**
 *
 * @author Lathy
 */
public class SearchConfiguration implements PlayableAnalyzableConfiguration  {
    
    private final String[] creators = {};
    
    private List<File> files;
    private String id;
    private NotesPlayer player;

    private List<SearchVisualization> playables;
    private List<SearchVisualization> visualizables;
    
    private static final Logger logger = Logger.getLogger(SearchConfiguration.class.getName());
    private boolean stopped;

    private NotesRecorder recorder;

    private ProjectOrganization org;
    private File stageFolder;
    private Participant participant;

    private List<NotesRecorder> notesRecorders;
    private File folder;

    
    public SearchConfiguration(String name) {

    }

    public SearchConfiguration() {
        //System.out.println("MoTranscriptionAnalysisConfiguration");
        playables = new ArrayList<>();
        visualizables = new ArrayList<>();
        files = new ArrayList<>();
    }
    
    public void addPlayable(SearchVisualization playable) {
        playables.add(playable);
    }

    public void addVisualizable(SearchVisualization visualizable) {
        visualizables.add(visualizable);
    }
    
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public List<String> getCompatibleCreators() {
        return Arrays.asList(creators);
    }

    @Override
    public void addFile(File file) {
        if (!files.contains(file)) {
            files.add(file);
        }
    }
    
    @Override
    public void removeFile(File file) {
        File toRemove = null;
        for (File f : files) {
            if (f.equals(file)) {
                toRemove = f;
            }
        }
        
        if (toRemove != null) {
            files.remove(toRemove);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public File toFile(File parent) {
        File f = new File(parent, "search"+id+".xml");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return f;
    }
    
    @Override
    public Configuration fromFile(File file) {
        String fileName = file.getName();

        if (fileName.contains("_") && fileName.contains(".")) {
            String name = fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("."));//Se obtiene el id de la configuracion
            SearchConfiguration config = new SearchConfiguration ();
            config.id = name;
            return config;
        }
        return null;
    }
    
    
    
     
    public void init(File folder) { 
        //System.out.println("init");
        this.id = Long.toString((new Date()).getTime()); 
        this.folder = folder; 
    }
    
    @Override
    public void setupAnalysis(File stageFolder, ProjectOrganization org, Participant p) {
        //System.out.println("SetupAnalysis");
        this.stageFolder = stageFolder;
        this.org = org;
        this.participant = p;
        
    }

    @Override
    public void cancelAnalysis() {
        
    }

    
    @Override
    public Playable getPlayer() {
        
        return this.player;
    }

    @Override
    public void startAnalysis() {
        System.out.println("Start Analysis");
    }
    
    
    
    
    
}
