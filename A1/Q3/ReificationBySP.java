import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

import java.io.*;


public class ReificationBySP{

    public static void main(String[] args) {
        
        String inputFile = "rdf_star_output.ttl";
        Model model = ModelFactory.createDefaultModel();
        InputStream inStr = FileManager.get().open(inputFile);
        if (inStr == null) {
            throw new IllegalArgumentException("File NOT Found: " + inputFile);
        }
        model.read(inStr, null, "TTL");

        //Creating a model for reification
        String singleton = "http://example.org/reification";
        Model reified_Model = ModelFactory.createDefaultModel();
        reified_Model.setNsPrefix("reification", singleton);

        //instantiate the object of singleton property
        Property sp = reified_Model.createProperty(singleton, "hasStatement");

        //traversing over all triples
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subj = stmt.getSubject(); //subject
            Property pred = stmt.getPredicate(); //predicate
            RDFNode obj = stmt.getObject(); //object

            //Adding reified statements
            Resource reified_Stmt = reified_Model.createResource()
                    .addProperty(RDF.type, RDF.Statement)
                    .addProperty(RDF.subject, subj)
                    .addProperty(RDF.predicate, pred)
                    .addProperty(RDF.object, obj);

       
            Resource namedSubj = reified_Model.createResource(subj.getURI());
            namedSubj.addProperty(sp, reified_Stmt);
        }

  
        //write the reified model to a reified_sp.ttl file
        try {
            OutputStream outStr = new FileOutputStream("reified_sp.ttl");
            reified_Model.write(outStr, "TTL");
        } catch (FileNotFoundException error) {
        	error.printStackTrace();
        }
    }
}
