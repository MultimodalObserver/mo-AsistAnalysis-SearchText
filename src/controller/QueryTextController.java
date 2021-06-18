/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.search.spans.*;
import org.apache.lucene.search.spans.SpanWeight.Postings;
import mo.core.filemanagement.project.Project;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;


import model.QueryText;
import model.ResultText;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * FXML Controller class
 *
 * @author Lathy
 */
public class QueryTextController implements Initializable {


    
    @FXML
    private Button btnquery;

    public Object controllerLabel;
    
    private List<File> listFileDocsTemporal = new ArrayList<>();
    @FXML
    private javafx.scene.control.TextField textFieldConsult;
    @FXML
    private TableView tableViewResum;
    @FXML
    private TableColumn<QueryText, String> nameColumn;
    @FXML
    private TableColumn<QueryText, Integer> referenceColumn;
    @FXML
    private TableColumn<QueryText, Button> actionColumn;
    @FXML
    private TableColumn<QueryText, Integer> rankingColumn;
    
    private TextArea textAreaReference;
    
    public static final String CONTENTS = "contents";
    public static final String FILE_NAME = "filename";
    public static final String FILE_PATH = "filepath";
    public static final int MAX_SEARCH = 10;
    
    
    public String indexPath = "index";//Indicar donde se va a guardar la indexacion
    public String docsPath = null;//Donde estan los documentos a indexar
    public boolean create = true;
    
    
    String index = "index";
    String field = "contents";
    
    
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    String queryString = null;
    int hitsPerPage = 10;
    
    
    private HashMap<String,Long> termsFrequency = new HashMap<>();
    
    
    
    ArrayList<ArrayList> termsDocument = new ArrayList<ArrayList>();
    //private List<List<String>> termsDocument = new ArrayList<>();
    ArrayList<String> listTerm = new ArrayList<String>();
    
    Directory dir ;
    Analyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig iwc ;
    IndexWriter writer;
    
    
    private final ObservableList<QueryText> list = FXCollections.observableArrayList();
    @FXML
    private ProgressIndicator pIndicator;
    private Label label;
    
    List<String> paths = new ArrayList<>();
    List<Integer> cantidadVeces = new ArrayList<>();
    ObservableList<QueryText> listWithoutDuplicate = FXCollections.observableArrayList();
    @FXML
    private TabPane tabPane;
      @FXML
    private TableView<ResultText> tableViewReference;
    @FXML
    private TableColumn<ResultText, String> columnPalabra;
    @FXML
    private TableColumn<ResultText, String> columnDocumento;
    @FXML
    private TableColumn<ResultText, Integer> columnLinea;
    @FXML
    private TableColumn<ResultText, String> columnTiempo;
    @FXML
    private TableColumn<ResultText, Button> columnAccion;
    
     private final ObservableList<ResultText> listResult = FXCollections.observableArrayList();
    
    ResultText resultText = new ResultText();
    
    String pathTranscription;
    ResourceBundle dialogBundle = java.util.ResourceBundle.getBundle("properties/principal");
    @FXML
    private Text textSearch;
    @FXML
    private Tab PaneResumen;
    @FXML
    private Tab PaneReferencias;
    @FXML
    private Label labelProgreso;
    
    public Plugin coding;
    public Class<?> classControllerCoding;
    
    public Object instanceCoding = new Object();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarIdioma();
        initListPlugin();
        // TODO
    }    

    
    public void initListPlugin(){
        List<Plugin> pluginAnalisisTodos = new ArrayList<>();
        List<Plugin> listaDePlugins = PluginRegistry.getInstance().getPluginsFor("mo.analysis.AnalysisProvider");
        //List<Plugin> listaDePluginsAnalisis = new ArrayList<>();//Todos los plugin de analisis transcription
        for (Plugin plugin : listaDePlugins ){//Todos los plugin
            //System.out.println("Nombre plugin: "+plugin.getPath().toString() );
            //System.out.println("Nombre plugin: "+plugin.getId() );
            //System.out.println("Nombre plugin: "+plugin.getPath().toString() );
            if(plugin.getPath().toString().contains("Coding")){
                System.out.println("Coooooooooooooooooooooooooooo: "+plugin.getPath().toString() );
                coding=plugin;
            }
        }  
        
        
        
        try {
            URL jarUrl = new URL("file:///"+ coding.getPath()); //Permite cargar el .jar
            URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl});//
            
            classControllerCoding = Class.forName("controller.LabelTextViewController", true, loader);
            
//            Constructor<?> ctorr = classControllerCoding.getConstructor();//Constructor
//            instanceCoding = ctorr.newInstance();//Instancia de controladorCloudTags
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    public QueryTextController (){
       
    }
    

    
    public void cargarIdioma(){
        textSearch.setText(dialogBundle.getString("SearchTittle"));
        
        textFieldConsult.setPromptText(dialogBundle.getString("SearchTextFieldQuery"));
        
        btnquery.setText(dialogBundle.getString("SearchBtnBuscar"));
        
        labelProgreso.setText(dialogBundle.getString("SearchProgresoIndex"));
        
        
    
    
    
    }
    
    public List<File> get_ListFileTemporal(){
        return listFileDocsTemporal;
    }
    
    public void deleteFolder(File file){//File de la carpeta a eliminar
      for (File subFile : file.listFiles()) {
         if(subFile.isDirectory()) {
            deleteFolder(subFile);
         } else {
            subFile.delete();
         }
      }
      file.delete();
    }
    
    public void set_ListFileTemporal(List<File> listFile) {
        System.out.println("Entro acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            System.out.println("Entro acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            
            
            pIndicator.setVisible(true);
            this.listFileDocsTemporal = listFile;
            String nombre = "\\analysis\\asist-analysis\\IndexFiles";//Se crea una carpeta con este nombre
            
            System.out.println("Acaaaaaaaaa"+pathTranscription+nombre);
            
            //System.out.println("Path parent: "+listFileDocsTemporal.get(0).getParent());
            
            
            indexPath = pathTranscription + nombre;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacion");
            alert.setHeaderText(null);
            alert.setContentText(dialogBundle.getString("SearchIndexando")+indexPath+"." );
            
            alert.showAndWait();
            
            termsDocument.clear();
            listTerm.clear();
            list.clear();
            tableViewResum.getItems().clear();
            tableViewReference.getItems().clear();
            //textAreaReference.clear();
            listWithoutDuplicate.clear();
            
            String path = indexPath;//cambiar esto
            if(!path.equals(null)){
                File file = new File(path);
                if(file.exists()){
                    deleteFolder(file);
                    
                    
                    
                }
            }
            
            
            dir = FSDirectory.open(Paths.get(indexPath));//Donde se guarda el path
            iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(dir, iwc);
            
            
            
            
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws UnsupportedAudioFileException, IOException  {
                    for(int i= 0; i<listFileDocsTemporal.size();i++){
                        index(listFileDocsTemporal.get(i).getAbsolutePath());
                        updateProgress(i+1, listFileDocsTemporal.size());
        
                    }
                    return null;   
                }
            };
            
            Thread thread = new Thread(task);
            thread.start();
            
            /*bind the progress with task*/
            pIndicator.progressProperty().bind(task.progressProperty());
            
            pIndicator.progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue) {
                    // If progress is 100% then show Text
                    if (newValue.doubleValue() >= 1) {
                        try {
                            // This text replaces "Done"
                            
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            
//System.out.println(combine(listFileDocsTemporal.get(0).getAbsolutePath(), listFileDocsTemporal.get(1).getAbsolutePath()));
//////        for(int i= 0; i<listFileDocsTemporal.size();i++){
//////            index(listFileDocsTemporal.get(i).getAbsolutePath());
//////        
//////        }
//////        writer.close();
//index("C:\\Users\\Lathy\\Desktop\\NuevaCarpeta");
//count();
        } catch (IOException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String combine(String path1, String path2){
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
    
    @FXML
    private void clickSearch(MouseEvent event) throws IOException, ParseException {
        if(textFieldConsult.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacion");
            alert.setHeaderText(null);
            alert.setContentText(dialogBundle.getString("SearchBusqueda"));

            alert.showAndWait();
        }else{
            termsFrequency.clear();//se eliminan los terminos de la consulta anterior
            list.clear();
            
            pIndicator.setVisible(false);
            
            long inicio = System.currentTimeMillis();
            
            search(textFieldConsult.getText());
            
            long fin = System.currentTimeMillis()-inicio;
            
            System.out.println("El tiempo de busqueda: "+ fin);
        
        }
    }
    
    /** Indexes a single document */
    void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        try (InputStream stream = Files.newInputStream(file)) {
          FieldType myFieldType = new FieldType(TextField.TYPE_NOT_STORED);
          myFieldType.setStoreTermVectors(true);
          myFieldType.setStoreTermVectorPositions(true);
          myFieldType.setStoreTermVectorOffsets(true);
          
          
          // make a new, empty document
          Document doc = new Document();

          // Add the path of the file as a field named "path".  Use a
          // field that is indexed (i.e. searchable), but don't tokenize 
          // the field into separate words and don't index term frequency
          // or positional information:
          Field pathField = new StringField("path", file.toString(), Field.Store.YES);//file.toString() IS PATH
          //file.toString()
          //in file.toString() cambiando "C:\\Users\\Lathy\\Desktop\\NuevaCarpeta\\doc2.txt"
          //System.out.println("Path field: " + pathField);
          
          doc.add(pathField);
          
          // Add the last modified date of the file a field named "modified".
          // Use a LongPoint that is indexed (i.e. efficiently filterable with
          // PointRangeQuery).  This indexes to milli-second resolution, which
       // is often too fine.  You could instead create a number based on
          // year/month/day/hour/minutes/seconds, down the resolution you require.
          // For example the long value 2011021714 would mean
          // February 17, 2011, 2-3 PM.
          doc.add(new LongPoint("modified", lastModified));

          // Add the contents of the file to a field named "contents".  Specify a Reader,
          // so that the text of the file is tokenized and indexed, but not stored.
          // Note that FileReader expects the file to be in UTF-8 encoding.
          // If that's not the case searching for special characters will fail.
          
          //----//doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
          
          

          doc.add(new Field("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)), myFieldType));
          
          if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            System.out.println("adding " + file);
            writer.addDocument(doc);
          } else {
            // Existing index (an old copy of this document may have been indexed) so 
            // we use updateDocument instead to replace the old one matching the exact 
            // path, if present:
            System.out.println("updating " + file);
            writer.updateDocument(new Term("path", file.toString()), doc);
          }
        }
    }
    
    void indexDocs(final IndexWriter writer, Path path) throws IOException {
        String nameFile = listFileDocsTemporal.get(0).getName();
        if (Files.isDirectory(path)) {
          Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
              try {
                  indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                  //System.out.println("Afuera" +"Path index: "+file.toFile().getAbsolutePath()+" Path list: "+ pathFile);
                  if(file.toFile().getName().equals(nameFile)){
                      //System.out.println("adentro");
                      //indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                  
                  }
                
                
              } catch (IOException ignore) {
                // don't index files that can't be read.
              }
              return FileVisitResult.CONTINUE;
            }
          });
        } else {
          indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
    
    public void index(String pathDocuments){
        docsPath = pathDocuments;
        //Path doc1 = Paths.get("C:\\Users\\Lathy\\Desktop\\NuevaCarpeta\\doc1.txt", "C:\\Users\\Lathy\\Desktop\\NuevaCarpeta\\doc2.srt");
        
        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
          System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
          System.exit(1);
        }

        Date start = new Date();
        try {
          System.out.println("Indexing to directory '" + indexPath + "'...");

          //Directory dir = FSDirectory.open(Paths.get(indexPath));
          //Analyzer analyzer = new StandardAnalyzer();
          //IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

          if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
          } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
          }

          // Optional: for better indexing performance, if you
          // are indexing many documents, increase the RAM
          // buffer.  But if you do this, increase the max heap
          // size to the JVM (eg add -Xmx512m or -Xmx1g):
          //
          // iwc.setRAMBufferSizeMB(256.0);
          

            //IndexWriter writer = new IndexWriter(dir, iwc);
          indexDocs(writer, docDir);

          // NOTE: if you want to maximize search performance,
          // you can optionally call forceMerge here.  This can be
          // a terribly costly operation, so generally it's only
          // worth it when your index is relatively static (ie
          // you're done adding documents to it):
          //
          // writer.forceMerge(1);

          //writer.close();

          Date end = new Date();
          System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
          System.out.println(" caught a " + e.getClass() +
           "\n with message: " + e.getMessage());
        }
        
        
    }

    
    public void doPagingSearch(IndexReader indexReader, BufferedReader in, IndexSearcher searcher, Query query, 
                                        int hitsPerPage, boolean raw, boolean interactive) throws IOException {
        termsDocument.clear();
        listTerm.clear();
        list.clear();
       tableViewResum.getItems().clear();
       tableViewReference.getItems().clear();
       listWithoutDuplicate.clear();
       
       // Collect enough docs to show 5 pages
       String texto = textFieldConsult.getText();
       String [] array = texto.split("[ANDOR]");//Verifica si la consulta tiene un caracter especial
       
       
       TopDocs results = searcher.search(query, 5 * hitsPerPage);//Cantidad de documentos resultado de la busqueda
       ScoreDoc[] hits = results.scoreDocs; //

       int numTotalHits = Math.toIntExact(results.totalHits.value);
       System.out.println(numTotalHits + " total matching documents");
       //Terms termVector = indexReader.getTermVector(0, "field");
       //System.out.println(termVector + " TermVector");
       
//        
//        SpanTermQuery fleeceQ = new SpanTermQuery(new Term("contents", "eduardo"));//Eduardo es la consulta
//        TopDocs resultsSpan = searcher.search(fleeceQ, 10);
//        for (int i = 0; i < resultsSpan.scoreDocs.length; i++) {
//          ScoreDoc scoreDoc = resultsSpan.scoreDocs[i];
//          System.out.println("Score Doc Span: " + scoreDoc);
//        }
//        
//        Term t = new Term("contents", "eduardo");
//        Query q = new TermQuery(t);
//        TopDocs resultsT = searcher.search(q, 1);
        
        //Esto obtiene el id del documento
        //Y se crea una lista con las posiciones en palabras, posicion en el documento, largo y la palabra
        
        columnPalabra.setCellValueFactory(new PropertyValueFactory <ResultText, String>("palabra"));
        columnDocumento.setCellValueFactory(new PropertyValueFactory <ResultText, String>("pathDocumento"));
        columnDocumento.setCellFactory(TooltippedTableCell.forTableColumn());
        
        
        columnLinea.setCellValueFactory(new PropertyValueFactory <ResultText, Integer>("linea"));
        columnTiempo.setCellValueFactory(new PropertyValueFactory <ResultText, String>("tiempo")); 
        columnTiempo.setCellFactory(TooltippedTableCell.forTableColumn());
        columnAccion.setCellValueFactory(new PropertyValueFactory <ResultText, Button>("action"));
        
        int x=0;
        
        
        
        for ( ScoreDoc scoreDoc: results.scoreDocs ) {
            Document doc = searcher.doc(hits[x].doc);
            String path = doc.get("path");//Se obtiene el path del documento
            paths.add(path);
//            String[] splitName = path.split("\\\\");    
//            String nameFile = splitName[splitName.length-1];
            
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            String textFile = "";
            while (myReader.hasNextLine()) {
                
              String data = myReader.nextLine();
              textFile = textFile + data + "\n";
              //System.out.println(data);
            }
            myReader.close();
            
            String content;

            content = new String(Files.readAllBytes(Paths.get(path)));
            //System.out.println("Largo: "+content.length());
            
            
            
            Fields termVs = indexReader.getTermVectors(scoreDoc.doc);
            //System.out.println("Documento path: "+path);
            Terms f = termVs.terms("contents");
            TermsEnum te = f.iterator();
            PostingsEnum docsAndPosEnum = null;
            BytesRef bytesRef;
            while ( (bytesRef = te.next()) != null ) {//Toma los terminos de cada doc
                docsAndPosEnum = te.postings(docsAndPosEnum, PostingsEnum.ALL);
                // for each term (iterator next) in this field (field)
                // iterate over the docs (should only be one)
                int nextDoc = docsAndPosEnum.nextDoc();
                assert nextDoc != DocIdSetIterator.NO_MORE_DOCS;
                final int fr = docsAndPosEnum.freq();//Frecuencia
                final int p = docsAndPosEnum.nextPosition();//Posicion
                final int o = docsAndPosEnum.startOffset();//Largo ?
                //System.out.println("Id_doc: "+scoreDoc.doc+", p="+ p + ", o=" + o + ", l=" + bytesRef.length + ", f=" + fr + ", s=" + bytesRef.utf8ToString());
                //Posicion en palabras, posicion exacta de la palabra en documento, largo de la palabra, frecuencia, palabra
                listTerm.add(""+scoreDoc.doc);//ID DOCUMENTO
                listTerm.add(""+p);//POSICION EN PALABRAS
                listTerm.add(""+o);//POSICION EN TEXTO
                listTerm.add(""+bytesRef.length);//LARGO PALABRA
                listTerm.add(""+fr);//FRECUENCIA
                listTerm.add(""+bytesRef.utf8ToString());//PALABRA
                
                if(fr>1){//Si es mayor a 1, es porque tiene mas repeticiones
                    for(int i=1; i<fr;i++){
                        //System.out.println("Id_doc: "+scoreDoc.doc+", Palabra: "+bytesRef.utf8ToString()+ ", Posicion siguiente: "+docsAndPosEnum.nextPosition()+", Offset Siguiente: "+docsAndPosEnum.startOffset());
                        listTerm.add(""+scoreDoc.doc);
                        listTerm.add(""+docsAndPosEnum.nextPosition());
                        listTerm.add(""+docsAndPosEnum.startOffset());
                        listTerm.add(""+bytesRef.length);
                        listTerm.add(""+fr);
                        listTerm.add(""+bytesRef.utf8ToString());

                    }

                    //System.out.println("Palabra: "+bytesRef.utf8ToString()+"Posicion: "+p + "Posicion siguiente: "+docsAndPosEnum.nextPosition()+"Offset Siguiente: "+docsAndPosEnum.startOffset());

                }
                
                
                
            }
            
            termsDocument.add(listTerm);
                
            if(array.length==1){//Si es igual a 1, es porque no tiene AND o OR y es una consulta completa
                String[] arraySplit = texto.split("[\\s+]");
                Button button = new Button(dialogBundle.getString("SearchBtnCargarTabla"));
                Button buttonConsulta = new Button(dialogBundle.getString("SearchBtnSeleccionarTabla"));
                
                for(List<String> termsD : termsDocument){
                    
                    //System.out.println(termsD);
                        
                        int frecuencia=0;
                        String textoTextArea = null;
                        
                        
                        
                        while(termsD.indexOf(arraySplit[0])!=-1){//Significa que se encuentra
                            //System.out.println("Numero id: "+(Integer.parseInt((termsD.get(termsD.indexOf(arraySplit[0])-5)))+1));
                            
                            resultText.setPathDocumento(path);
                            resultText.setPalabra(textFieldConsult.getText());
                            //textAreaReference.appendText("Document "+path+"\n");
                            //textAreaReference.appendText("Reference: "+ textFieldConsult.getText()+ "\n");
                            
                            int cantidad = (Integer.parseInt(termsD.get(termsD.indexOf(arraySplit[0])-3))+ textFieldConsult.getText().length())+30;
                            int inicio = Integer.parseInt(termsD.get(termsD.indexOf(arraySplit[0])-3));
                            int fin = Integer.parseInt(termsD.get(termsD.indexOf(arraySplit[0])-3))+2;
                            int numero = 0 ;
                            if(cantidad < content.length()){//Verifica que cantidad este dentro del rango del documento

                                
                                while(!content.substring(inicio, fin).equals("--")){
                                    inicio--;
                                    fin --;
                                
                                }
                                
                                
                                resultText.setTiempo(content.substring(inicio-10, fin+11));
                                //System.out.println("Contenido-12 if: "+content.substring(inicio-15, fin-13));
                                
                                try{//Para numeros de 3 digitos
                                    numero = Integer.parseInt(content.substring(inicio-15, fin-13).trim());
//                                    if(NumberUtils.isNumber(content.substring(inicio-15, fin-13))){//Para todos los numeros de 3 digios y menos el primer numero
//                                        System.out.println("PRIMER IF"+ content.substring(inicio-15, fin-13));
//                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13));
//                                        
//                                    }//else if(NumberUtils.isNumber(content.substring(inicio-12, fin-13))){
////                                        System.out.println("SEGUNDO ELIF"+ content.substring(inicio-12, fin-13));
////                                        
////                                    }
                                }catch(NumberFormatException ex){
                                    System.out.println("F");//Para los deas numeros
                                    numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                }catch(StringIndexOutOfBoundsException ex){
                                    System.out.println("StringIndexOutOfBoundsException");//Para la primera fila
                                    numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                }  
                                
                                
                                resultText.setLinea(numero);
                                
                            }else{//cantidad > content.length()//caso contrario
                                
                                while(!content.substring(inicio, fin).equals("--")){
                                    inicio--;
                                    fin --;
                                
                                }
                                
                                
                                resultText.setTiempo(content.substring(inicio-10, fin+11));
                                
                                //System.out.println("Contenido-13 else: "+content.substring(inicio-13, fin-13));
                                try {
                                    //System.out.println("Contenido-13 else: "+content.substring(inicio-13, fin-13));
                                    System.out.println("asssssssss IF");
                                    numero = Integer.parseInt(content.substring(inicio-13, fin-13));
                                }
                                catch (Exception e)
                                {
                                    System.out.println("aca if catch: ");
                                    numero = Integer.parseInt(content.substring(inicio-12, fin-13));
                                }
                                //System.out.println("Numero: "+numero);
                                resultText.setLinea(numero);
                                
                                //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arraySplit[0])-3))+3),content.length())+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                            
                                
                            }
                            //textAreaReference.appendText("-------------------------------------------------" + "\n");
//                            System.out.println("Se encuentra la coindicencia"+ termsD.indexOf(arraySplit[0]));
//                            System.out.println("Frencuencia: "+ termsD.get(termsD.indexOf(arraySplit[0])-1));//Frecuencia
//                            System.out.println("Posiicon en texto : "+ termsD.get(termsD.indexOf(arraySplit[0])-3));//Poscion en texto
                               if(Integer.parseInt((termsD.get(termsD.indexOf(arraySplit[0])-1)))>=1 && frecuencia ==0){//Se guarda una vez
                               //termsD.subList(termsD.indexOf(arrayOR[i])+1, termsD.size()).indexOf(arrayOR[i])==-1
                                   list.add( new QueryText (
                                    (Integer.parseInt((termsD.get(termsD.indexOf(arraySplit[0])-5)))+1),
                                    path,
                                    Integer.parseInt(termsD.get(termsD.indexOf(arraySplit[0])-1)),//
                                    button = new Button(dialogBundle.getString("SearchBtnCargarTabla"))
                                    ));
                                    frecuencia++;
                               }

                                termsD =  termsD.subList(termsD.indexOf(arraySplit[0])+1, termsD.size());//Se corta la lista desde la primera ocurrecnia hasta el final
                                listResult.add(new ResultText(
                                        resultText.getPalabra(),
                                        resultText.getPathDocumento(),
                                        resultText.getLinea(),
                                        resultText.getTiempo(),
                                        buttonConsulta = new Button(dialogBundle.getString("SearchBtnSeleccionarTabla"))
                                
                                
                                
                                ));
                        }
                    
                
                }
                
            }else if(array.length > 1){//Signifca que existe un AND o OR
                Button button = new Button(dialogBundle.getString("SearchBtnCargarTabla"));
                Button buttonConsulta = new Button(dialogBundle.getString("SearchBtnSeleccionarTabla"));
                String textoTextField = textFieldConsult.getText();
                String [] arrayAND = textoTextField.split("[AND]");//Verifica si la consulta tiene un caracter especial
                List<String> temporalList = new ArrayList<>();
                if(arrayAND.length == 1){//Significa que hay OR----------------
                    String arrayOR[] = textoTextField.split("[\\s+OR]");
                    for(List<String> termsD : termsDocument){
                        int idDocument = 0;
                        temporalList = termsD;
                        int frecuencia = 1;
                        //System.out.println(termsD);
                        
                        for(int i=0; i<arrayOR.length;i++){//Se itera por cada palabra del OR, ej hola OR chao, arrayOR[hola, chao]
                        
                            if(i>0){//signifca que ya paso una vez
                                termsD=temporalList;//Permite que se carge denuevo la lsita de terminos
                            
                            }
                            
                            
                            while(termsD.indexOf(arrayOR[i])!=-1){//Significa que se encuentra
                                
                                int cantidad = (Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))+ textFieldConsult.getText().length())+30;
                                int inicio = Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3));
                                int fin = Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))+2;
                                int numero;
//System.out.println("Entro al while "+ arrayOR[i]);
                                
                                resultText.setPathDocumento(path);
                                resultText.setPalabra(arrayOR[i]);
                                
//                                textAreaReference.appendText("Document "+path+"\n");
//                                textAreaReference.appendText("Reference of: " + arrayOR[i]+"\n");
                            
                                if(cantidad < content.length()){
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))),cantidad)+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                                    while(!content.substring(inicio, fin).equals("--")){
                                        inicio--;
                                        fin --;
                                
                                    }
                                
                                
                                    resultText.setTiempo(content.substring(inicio-10, fin+11));
                                    //System.out.println("Contenido-12 if: "+content.substring(inicio-12, fin-13));
                                
                                
                                
                                    try{//Para numeros de 3 digitos
                                        numero = Integer.parseInt(content.substring(inicio-15, fin-13).trim());
        //                                    if(NumberUtils.isNumber(content.substring(inicio-15, fin-13))){//Para todos los numeros de 3 digios y menos el primer numero
        //                                        System.out.println("PRIMER IF"+ content.substring(inicio-15, fin-13));
        //                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13));
        //                                        
        //                                    }//else if(NumberUtils.isNumber(content.substring(inicio-12, fin-13))){
        ////                                        System.out.println("SEGUNDO ELIF"+ content.substring(inicio-12, fin-13));
        ////                                        
        ////                                    }
                                    }catch(NumberFormatException ex){
                                        System.out.println("F");//Para los deas numeros
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                    }catch(StringIndexOutOfBoundsException ex){
                                        System.out.println("StringIndexOutOfBoundsException");//Para la primera fila
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                    }  
                                
                                
                                    //System.out.println("Numero: "+numero);
                                    resultText.setLinea(numero);
                                    
                                    
                                    
                                    
                                    
                                }else{
                                    //System.out.println("Primero: "+(Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))+3)+", Segundo: "+content.length());
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))+3),(Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))+ textFieldConsult.getText().length())+3)+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayOR[i])-3))),content.length())+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                                    while(!content.substring(inicio, fin).equals("--")){
                                        inicio--;
                                        fin --;
                                
                                    }
                                
                                
                                    resultText.setTiempo(content.substring(inicio-10, fin+11));
                                    //System.out.println("Contenido-12 if: "+content.substring(inicio-12, fin-13));
                                
                                
                                
                                    try {
                                        //System.out.println("Contenido-13 else: "+content.substring(inicio-13, fin-13));
                                        numero = Integer.parseInt(content.substring(inicio-13, fin-13));
                                    }
                                    catch (Exception e)
                                    {
                                        //System.out.println("aca if catch: ");
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13));
                                    }
                                
                                
                                    //System.out.println("Numero: "+numero);
                                    resultText.setLinea(numero);
                                }
                            
                                //textAreaReference.appendText("-------------------------------------------------" + "\n");
                                 //System.out.println("aca entro");
                                 
                                 if( termsD.subList(termsD.indexOf(arrayOR[i])+1, termsD.size()).indexOf(arrayOR[i])==-1){//Se guarda una vez
                                     
                                     //if(arrayOR[i].equals(arrayOR[arrayOR.length-1]) || arrayOR[i+1].equals(arrayOR[arrayOR.length-1])){
//                                             System.out.println("Array solo"+arrayOR[i]);
//                                             System.out.println("Array mas -1"+arrayOR[i+1]);
                                            idDocument = (Integer.parseInt((termsD.get(termsD.indexOf(arrayOR[i])-5)))+1);
                                            //------------------------------------------------------------------------------
//                                            list.add( new QueryText (
//                                            (Integer.parseInt((termsD.get(termsD.indexOf(arrayOR[i])-5)))+1),//Es el id
//                                            path, //Es el path del documento
//                                            frecuencia,//Es la frecuencia
//                                            button  
//                                            ));
                                        //-------------------------------------------------
                                        //}
                                
                                 }
                                
                                frecuencia++;
                                termsD =  termsD.subList(termsD.indexOf(arrayOR[i])+1, termsD.size());
                                listResult.add(new ResultText(
                                        resultText.getPalabra(),
                                        resultText.getPathDocumento(),
                                        resultText.getLinea(),
                                        resultText.getTiempo(),
                                        buttonConsulta = new Button(dialogBundle.getString("SearchBtnSeleccionarTabla"))
                                
                                
                                
                                ));
                            }
                        }
                        list.add( new QueryText (
                                            idDocument,//Es el id
                                            path, //Es el path del documento
                                            frecuencia-1,//Es la frecuencia, menos 1
                                            button  
                                            ));
                    
                
                    }
                
                    
                }else if(arrayAND.length > 1){//Existe un AND
                    
                    String arrayANDS[] = textoTextField.split("[\\s+AND]"); 
                    for(List<String> termsD : termsDocument){
                        //System.out.println(termsD);
                        int idDocument = 0;
                        int contando = 1;
                        int frecuencia = 1;
                        List<String> temporal = new ArrayList<>();
                        temporal = termsD;
                        for(int i=0; i<arrayANDS.length;i++){
                            
                            
                            if(i>0){//signifca que ya paso una vez
                                termsD=temporal;//Permite que se carge denuevo la lsita de terminos
                            
                            }
                            
                            
                            
                            while(termsD.indexOf(arrayANDS[i])!=-1){    //Se encuentra el termino
                                int cantidad = (Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))+ textFieldConsult.getText().length())+30;
                                int inicio = Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3));
                                int fin = Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))+2;
                                int numero;
//System.out.println("Entro al while "+ arrayOR[i]);
                                
                                resultText.setPathDocumento(path);
                                resultText.setPalabra(arrayANDS[i]);
                                
                            
                                if(cantidad < content.length()){
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))),cantidad)+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                                    while(!content.substring(inicio, fin).equals("--")){
                                        inicio--;
                                        fin --;
                                
                                    }
                                
                                
                                    resultText.setTiempo(content.substring(inicio-10, fin+11));
                                    //System.out.println("Contenido-12 if: "+content.substring(inicio-12, fin-13));
                                
                                
                                
                                    try{//Para numeros de 3 digitos
                                        numero = Integer.parseInt(content.substring(inicio-15, fin-13).trim());
    //                                    if(NumberUtils.isNumber(content.substring(inicio-15, fin-13))){//Para todos los numeros de 3 digios y menos el primer numero
    //                                        System.out.println("PRIMER IF"+ content.substring(inicio-15, fin-13));
    //                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13));
    //                                        
    //                                    }//else if(NumberUtils.isNumber(content.substring(inicio-12, fin-13))){
    ////                                        System.out.println("SEGUNDO ELIF"+ content.substring(inicio-12, fin-13));
    ////                                        
    ////                                    }
                                    }catch(NumberFormatException ex){
                                        System.out.println("F");//Para los deas numeros
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                    }catch(StringIndexOutOfBoundsException ex){
                                        System.out.println("StringIndexOutOfBoundsException");//Para la primera fila
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13).trim());
                                    }  
                                
                                
                                    //System.out.println("Numero: "+numero);
                                    resultText.setLinea(numero);
                                    
                                    
                                    
                                    
                                }else{
                                    while(!content.substring(inicio, fin).equals("--")){
                                        inicio--;
                                        fin --;
                                
                                    }
                                
                                
                                    resultText.setTiempo(content.substring(inicio-10, fin+11));
                                    //System.out.println("Contenido-12 if: "+content.substring(inicio-12, fin-13));
                                
                                
                                
                                    try {
                                        //System.out.println("Contenido-13 else: "+content.substring(inicio-13, fin-13));
                                        numero = Integer.parseInt(content.substring(inicio-13, fin-13));
                                    }
                                    catch (Exception e)
                                    {
                                        //System.out.println("aca if catch: ");
                                        numero = Integer.parseInt(content.substring(inicio-12, fin-13));
                                    }
                                
                                
                                    //System.out.println("Numero: "+numero);
                                    resultText.setLinea(numero);
                                    
                                    
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))+3),(Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))+ textFieldConsult.getText().length())+3)+"..."+"\n");//Desde la ubicacion hasta unas palabras mas
                                    //textAreaReference.appendText("..."+content.substring((Integer.parseInt(termsD.get(termsD.indexOf(arrayANDS[i])-3))),content.length())+"..."+"\n");//Desde la ubicacion hasta unas palabras mas

                                    
                                }
                                
                                //textAreaReference.appendText("-------------------------------------------------" + "\n");
                                
                                if( termsD.subList(termsD.indexOf(arrayANDS[i])+1, termsD.size()).indexOf(arrayANDS[i])==-1){//Se guarda una vez
                                       //System.out.println((Integer.parseInt((termsD.get(termsD.indexOf(arrayOR[i])-5)))+1));
                                       idDocument = (Integer.parseInt((termsD.get(termsD.indexOf(arrayANDS[i])-5)))+1);
                                        //System.out.println(arrayANDS[i]);
                                        //System.out.println(contando + " y " + frecuencia);
                                        
                                        //--------------------------------------------------------------
//                                        list.add( new QueryText (
//                                        (Integer.parseInt((termsD.get(termsD.indexOf(arrayANDS[i])-5)))+1),//Es el id
//                                        path, //Es el path del documento
//                                        frecuencia,//Es la frecuencia
//                                        button    
//                                        ));
                                        
                                       
                                }
                              
                                
                                frecuencia++;
                                termsD =  termsD.subList(termsD.indexOf(arrayANDS[i])+1, termsD.size());
                                listResult.add(new ResultText(
                                        resultText.getPalabra(),
                                        resultText.getPathDocumento(),
                                        resultText.getLinea(),
                                        resultText.getTiempo(),
                                        buttonConsulta = new Button(dialogBundle.getString("SearchBtnSeleccionarTabla"))
                                
                                
                                
                                ));
                            }
                            //System.out.println("Entro al and desdepues del while");
                        
                        }
                        //System.out.println("Entro al and desdepues del for int i=0");
                        list.add( new QueryText (
                                        idDocument,//Es el id
                                        path, //Es el path del documento
                                        frecuencia-1,//Es la frecuencia
                                        button    
                                        ));
                        
                    
                    }
                    
                
                }else{//No existe ningun resultado
                    
                    
                    
                
                
                }
                
                
                
            
            }else {
                System.out.println("No existen resutaodooos");
                tableViewReference.setPlaceholder(new Label(dialogBundle.getString("SearchNoExistenResultados")));
                tableViewResum.setPlaceholder(new Label(dialogBundle.getString("SearchNoExistenResultados")));
            
            }
            
            
            listTerm.clear();
            termsDocument.clear();
            
            
            x++;
            
//                            list.add( new QueryText (
//                            (x+1),
//                            path,
//                            termsFrequency.get(queryString).intValue()//
//                            ));
        
            
            
            
        }
        
        for(int i=0; i<listResult.size();i++){
            
            //System.out.println(listResult.get(i).getLinea());
        
        }
        tableViewReference.setItems(listResult);
        
        if(tableViewReference.getItems().isEmpty()){
            tableViewReference.setPlaceholder(new Label(dialogBundle.getString("SearchNoExistenResultados")));
            tableViewResum.setPlaceholder(new Label(dialogBundle.getString("SearchNoExistenResultados")));
        
        }
        
        
        
       // System.out.println("Posicion 0: "+termsDocument.get(0));
//        System.out.println("Posicion 1: "+termsDocument.get(1));
        
      
        for(ScoreDoc scoredoc: hits){
            Hashtable<Integer, Integer> hitss = new Hashtable<Integer, Integer>();
//            while (spans.next() == true)
//            {
//                 int docID = spans.doc();
//                 int hit = hitss.get(docID) != null ? hitss.get(docID) : 0;
//                 hit++;
//                 hitss.put(docID, hit);
//            }
            
            //List docIds = // doc ids for documents that matched the query, 
              // sorted in ascending order 

//            int totalFreq = 0;
//            TermsEnum termDocs = searcher.termDocs();
//            termDocs.seekExact(1,new Term("my_field", "congress"));
//            for (ScoreDoc id : hits) {
//                termDocs.skipTo(id);
//                totalFreq += termDocs.freq();
//            }
            
            
            
//            //Document doc = searcher.doc(scoredoc.doc);
//            Document d = searcher.doc(scoredoc.doc);
//                System.out.println("Document Number : " + scoredoc.doc + " :: Document Name : " + d.get("name")
//                        + "  :: Content : " + d.get("content") + "  :: Score : " + scoredoc.score);
//            
            //int docId = results.scoreDocs[0].doc;
            //System.out.println("DocID: "+docId);
            //Retrieve the matched document and show relevant details
            //Document doc = searcher.doc(scoredoc.doc);
            
            //System.out.println(doc.getFields());
//            System.out.println("\nSender: "+doc.getField("sender").stringValue());
//            System.out.println("Subject: "+doc.getField("subject").stringValue());
//            System.out.println("Email file location: "
//                          +doc.getField("emailDoc").stringValue());   
         }
       
       
       
       
       int start = 0;
       int end = Math.min(numTotalHits, hitsPerPage);
       while (true) {
           if (end > hits.length) {
               System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
               System.out.println("Collect more (y/n) ?");
               String line = in.readLine();
               if (line.length() == 0 || line.charAt(0) == 'n') {
                 break;
               }

               hits = searcher.search(query, numTotalHits).scoreDocs;
           }

         end = Math.min(hits.length, start + hitsPerPage);

           for (int i = start; i < end; i++) {
               if (raw) {                              // output raw format
                   System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
                   continue;
               }
               

               Document doc = searcher.doc(hits[i].doc);
               String path = doc.get("path");//Se obtiene el path del documento
               if (path != null) {//Si el path existe
                   //__________________________________________________---------------------------------------
                   
                    for ( ScoreDoc scoreDoc: results.scoreDocs ) {//Itera por cada documento
                        Fields termVs = indexReader.getTermVectors(scoreDoc.doc);
                        //System.out.println("Path"+ path);
                        Terms f = termVs.terms("contents");
                        TermsEnum te = f.iterator();
                        PostingsEnum docsAndPosEnum = null;
                        BytesRef bytesRef;
                        while ( (bytesRef = te.next()) != null ) {//Itera para cada palabra del documento
                            docsAndPosEnum = te.postings(docsAndPosEnum, PostingsEnum.ALL);
                            // for each term (iterator next) in this field (field)
                            // iterate over the docs (should only be one)
                            int nextDoc = docsAndPosEnum.nextDoc();
                            assert nextDoc != DocIdSetIterator.NO_MORE_DOCS;
                            final int fr = docsAndPosEnum.freq();//Frecuencia
                            final int p = docsAndPosEnum.nextPosition();//Posicion
                            final int o = docsAndPosEnum.startOffset();//Largo ?
                            
                            //System.out.println("Id_doc: "+scoreDoc.doc+", p="+ p + ", o=" + o + ", l=" + bytesRef.length + ", f=" + fr + ", s=" + bytesRef.utf8ToString());
                            //Posicion en palabras, posicion exacta de la palabra en documento, largo de la palabra, frecuencia, palabra
                            if(fr>1){//Si es mayor a 1, es porque tiene mas repeticiones
                                for(int j=1; j<fr;j++){
                                    
                                    //System.out.println("Id_doc: "+scoreDoc.doc+", Palabra: "+bytesRef.utf8ToString()+ ", Posicion siguiente: "+docsAndPosEnum.nextPosition()+", Offset Siguiente: "+docsAndPosEnum.startOffset());


                                }

                                //System.out.println("Palabra: "+bytesRef.utf8ToString()+"Posicion: "+p + "Posicion siguiente: "+docsAndPosEnum.nextPosition()+"Offset Siguiente: "+docsAndPosEnum.startOffset());

                            }

                        }
                    }
                   
                   
                   //-----------------------------------------------------
                   
                   
                   
                   
                   
                   
                   
                   for (int j = 0; j < numTotalHits; j++){
                        int docID = hits[j].doc;
                        Terms termVector  = indexReader.getTermVector(docID, field);//getTermVector(docID, "docField");
                        TermsEnum itr = termVector.iterator();
                        int allTermFrequency=0;
                        BytesRef term;

                        while ((term = itr.next()) != null){
                          String termText = term.utf8ToString();
                          //System.out.println("Doc id: "+docID+"Term: "+termText);
                          long tf = itr.totalTermFreq();
                          termsFrequency.put(termText, tf);
                          //
                          allTermFrequency += itr.totalTermFreq();
                          
//                          if(termVector.hasPositions()){
//                                TermsEnum termsEnum = termVector.iterator();
//                                PostingsEnum postings = null;
//                                while(termsEnum.next() != null){
//                                    postings  = termsEnum.postings(postings ,PostingsEnum.ALL);
//                                    while(postings.nextDoc() != PostingsEnum.NO_MORE_DOCS){
//
//                                        System.out.println("Posicion id: "+postings.nextPosition() + termText);
//                                    }
//                                }
//                            }
                          
                        }
                        //System.out.println("Doc id: "+docID+"Term frecuenci: "+termsFrequency);
                        
                     //-----------------------------------------------------------------------------------------
//                    Terms terms = indexReader.getTermVector(docID, field);
//                    
//
//                    if(terms.hasPositions()){
//                        TermsEnum termsEnum = terms.iterator();
//                        PostingsEnum postings = null;
//                        while(termsEnum.next() != null){
//                            postings  = termsEnum.postings(postings ,PostingsEnum.ALL);
//                            while(postings.nextDoc() != PostingsEnum.NO_MORE_DOCS){
//                                
//                                System.out.println("Posicion: "+postings.nextPosition());
//                            }
//                        }
//                    }
                            //-----------------------------------------------------------------------------------------
                        
                        if(termsFrequency.get(queryString)!= null){//Si se encuentra la palabra en el documento
//                            list.add( new QueryText (
//                            (i+1),
//                            path,
//                            termsFrequency.get(queryString).intValue()//
//                                   
//                        ));
                            //------------------------------------------------------------------------------
                            //tiene que funcionar para mas de una palabra en la busqueda
                            //cuando pongo hola, se agregan 4 veces cada documento
                            //------------------------------------------------------------------------------
                            //System.out.println((i+1)+", "+path+", "+ termsFrequency.get(queryString));
                        
                        
                        }
                        
                        
                        
                    }
                   
                   
                 //System.out.println((i+1) + ". " + path );//+ hits[i]
                 
                 
                 
                 
                 String title = doc.get("title");
                 if (title != null) {
                   System.out.println("   Title: " + doc.get("title"));
                 }
               } else {
                 System.out.println((i+1) + ". " + "No path for this document");
               }

           }
           
           //ArrayList<T> newList = new ArrayList<T>(); 
           int numero = 0;
           int countA = 0;
           for (QueryText element : list) {
               
               
           
           }
           
           
           
           for (int j=0; j<paths.size();j++){
               
               //System.out.println("Nombre: "+ paths.get(j));
               int numeroContar=0;
               for (QueryText element : list) { //element tiene un elemento de QueryText con sus atributos
                   if(element.getPath().equals(paths.get(j))){
                       numeroContar++;
//                       numero = element.getReference(); 
//                       System.out.println(numero);
//                       System.out.println("Nombre: "+ element.getPath() + "and "+ element.getReference());
                   
                   }
                   
                
                } 
               cantidadVeces.add(numeroContar);
               
               //System.out.println(numeroContar);
               
           }
           for (int j=0; j<paths.size();j++){
               
               //System.out.println("Nombre: "+ paths.get(j));
               
               for (QueryText element : list) { //element tiene un elemento de QueryText con sus atributos
                   if(element.getPath().equals(paths.get(j)) && cantidadVeces.get(j)==element.getReference()){
                       if(listWithoutDuplicate.size()==0){//si es igual a 0, agregar
                            //System.out.println("Entro es 0");
                           //listWithoutDuplicate.add(element);
                           //System.out.println("Nombre: "+ element.getPath() + "and "+ element.getReference());
                       }else{
                           for(int i=0; i<listWithoutDuplicate.size();i++){
                               if(!listWithoutDuplicate.get(i).getPath().equals(element.getPath())){//Si son distintos agregar, si no no agregar
                                  // listWithoutDuplicate.add(element);
                                   System.out.println("Entro es porque no esta en listWithout");
                               }
                           
                           }
                       
                       
                       }
                   }else{
                   
                   }
                   
                
                } 
                
               
               
               
                
                for (int a=0; a<listResult.size();a++){
                  

                }

                    
               
                
               
               
                   //System.out.println("Pathh: "+path );
                   int mayor = 0;
                   for (QueryText element : list) {
//                       if(element.getReference()<mayor && path.equals(element.getPath())){
//                           
//                       
//                       }else{
//                           mayor = element.getReference();
//                           //System.out.println("Path: "+ path + " Reference: "+ mayor);
//                       }
//                    System.out.println(element.getRanking());
                    //System.out.println(element.getPath());
//                    System.out.println(element.getReference());
//                    System.out.println(element.getAction());
//                   
                   
               
                    }
                   
                   
               
               //System.out.println(numeroContar);
               
           }
            
            
           
           
           
           rankingColumn.setCellValueFactory(new PropertyValueFactory <QueryText, Integer>("ranking"));
           nameColumn.setCellValueFactory(new PropertyValueFactory <QueryText, String>("path"));
           nameColumn.setCellFactory(TooltippedTableCell.forTableColumn());
           referenceColumn.setCellValueFactory(new PropertyValueFactory <QueryText, Integer>("reference"));
           actionColumn.setCellValueFactory(new PropertyValueFactory <QueryText, Button>("action"));
           
           tableViewResum.setItems(list);
           
           
           

           if (!interactive || end == 0) {
               break;
           }

         if (numTotalHits >= end) {
           boolean quit = false;
           while (true) {
             System.out.print("Press ");
             if (start - hitsPerPage >= 0) {
               System.out.print("(p)revious page, ");  
             }
             if (start + hitsPerPage < numTotalHits) {
               System.out.print("(n)ext page, ");
             }
             System.out.println("(q)uit or enter number to jump to a page.");

             String line = in.readLine();
             if (line.length() == 0 || line.charAt(0)=='q') {
               quit = true;
               break;
             }
             if (line.charAt(0) == 'p') {
               start = Math.max(0, start - hitsPerPage);
               break;
             } else if (line.charAt(0) == 'n') {
               if (start + hitsPerPage < numTotalHits) {
                 start+=hitsPerPage;
               }
               break;
             } else {
               int page = Integer.parseInt(line);
               if ((page - 1) * hitsPerPage < numTotalHits) {
                 start = (page - 1) * hitsPerPage;
                 break;
               } else {
                 System.out.println("No such page");
               }
             }
           }
           if (quit) break;
           end = Math.min(numTotalHits, start + hitsPerPage);
         }
       }
    }
    
    public static boolean isNumeric(String string) {
    int intValue;
		
    System.out.println(String.format("Parsing string: \"%s\"", string));
		
    if(string == null || string.equals("")) {
        System.out.println("String cannot be parsed, it is null or empty.");
        return false;
    }
    
    try {
        intValue = Integer.parseInt(string);
        return true;
    } catch (NumberFormatException e) {
        System.out.println("Input String cannot be parsed to Integer.");
    }
    return false;
}
    
    public void search(String querie) throws IOException, ParseException{
        queryString = querie;
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        BufferedReader in = null;
        if (queries != null) {
          in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        } else {
          in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }
        QueryParser parser = new QueryParser(field, analyzer);
        while (true) {
            if (queries == null && queryString == null) {                        // prompt the user
                System.out.println("Enter query: ");
            }

            String line = queryString != null ? queryString : in.readLine();

            if (line == null || line.length() == -1) {
                break;
            }

            line = line.trim();
            if (line.length() == 0) {
              break;
            }

            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));

            if (repeat > 0) {                           // repeat & time as benchmark
                Date start = new Date();
                for (int i = 0; i < repeat; i++) {
                  searcher.search(query, 100);
                }
                Date end = new Date();
                System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
            }
//            //_----------------------------------------------
//            Term term = new Term("myfield", querie);
//            long numOccurances = reader.totalTermFreq(term);
//            System.out.println("Numero ocurrencias: "+numOccurances);
//            //_----------------------------------------------
            doPagingSearch(reader,in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
            if(tableViewReference.getItems().isEmpty()){//Si esta vacia no hacer nada
            
            }else{//Caso contrario 
                
                tableViewReference.getSelectionModel().select(0);
                tableViewReference.getFocusModel().focus(0);
                
                
                tableViewResum.getSelectionModel().select(0);
                tableViewResum.getFocusModel().focus(0);
            
            }
            
            
            
            if (queryString != null) {
              break;
            }
        }
        reader.close();
    }
    
    @FXML
    private void clickedButton(MouseEvent event) {
        //System.out.println("Click metodo");
        
        
        for (QueryText bean : list){
            
            bean.getAction().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Method uploadFileSearch = null;
                        Object instanceNew = instanceCoding;
                        
                        uploadFileSearch = classControllerCoding.getDeclaredMethod("uploadFileSearch", String.class);

                        uploadFileSearch.invoke(instanceNew, bean.getPath());
                        //System.out.println("Path: "+ bean.getPath());



                        //controllerLabel.uploadFileSearch(bean.getPath());
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
        });

        }
        
            
        
        
        
        
        
        //System.out.println("Click metodo");
//        for (QueryText bean : list){
//            bean.getAction().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Path: "+ bean.getPath());
////                try {
////                    //System.out.println("Path: "+ bean.getPath());
////                    //controllerLabel.uploadFileSearch(bean.getPath());
////                } catch (IOException ex) {
////                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//        });
//        
//        }
        
//        for (ResultText beanReference : listResult){
//            beanReference.getAction().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Linea: "+beanReference.getLinea());
////                try {
////                    //System.out.println("Path: "+ bean.getPath());
////                    //controllerLabel.uploadFileSearch(bean.getPath());
////                } catch (IOException ex) {
////                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//        });
//        
//        }
        
    }
    
        
    public void setInstanceCoding(Class instanceC, Object insta){
        try {
            this.classControllerCoding = instanceC;
            
            Constructor<?> ctorr = classControllerCoding.getConstructor();//Constructor
            instanceCoding = insta;
            
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    
    }

    @FXML
    private void clickedButtonReference(MouseEvent event) {
        //tableViewResum.getSelectionModel().select(1);
        for (ResultText beanReference : listResult){
            beanReference.getAction().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                
                
                    //System.out.println("Linea: "+beanReference.getLinea());//Esto retorna la linea

                    //System.out.println("Path: "+ bean.getPath());
                    //controllerLabel.selectRowTable(beanReference.getLinea()-1, beanReference.getPathDocumento());

                    Method selectRowTable = null;
                    selectRowTable = classControllerCoding.getDeclaredMethod("selectRowTable", int.class,String.class);

                    selectRowTable.invoke(instanceCoding, beanReference.getLinea()-1,beanReference.getPathDocumento());


                
                } catch (IllegalAccessException ex) {
                        Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(QueryTextController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        }
    }

    public void count() {
            
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
            int numTerms = 100;
            
            TermStats[] stats = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
            //System.out.println("GetSum: "+reader.getSumDocFreq(field)); //  Se obtiene la suma totla de las palabras de cada documento
            //System.out.println("GetSum total term freq: "+reader.getSumTotalTermFreq(field));
            //System.out.println("Get getTermVector: "+reader.getTermVector(1, "latimer"));
            
            System.out.println("Get getTermVectors size : "+reader.getTermVectors(1).size());
            System.out.println("Get getTermVectors : "+reader.getTermVectors(1));
            
            for (TermStats termStats : stats) {
                String termText = termStats.termtext.utf8ToString();
                System.out.println(termText + " " + termStats.docFreq);
                System.out.println("Total terms: "+termStats.totalTermFreq);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }
    
    public void setPathTranscription(String path){
         this.pathTranscription = path;
     }
}
