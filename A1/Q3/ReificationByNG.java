import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

import java.io.*;


public class ReificationByNG{
	public static void main(String[] args) {
        
        String inputFile = "rdf_star_output.ttl";
        Model model = ModelFactory.createDefaultModel();
        InputStream inputStr = FileManager.get().open(inputFile);
        if(inputStr == null) {
            throw new IllegalArgumentException("File NOT Found: " + inputFile);
        }
        model.read(inputStr, null, "TTL");

        //Here's is the model for reification task
        String ng = "http://example.org/reification"; //ng-> named_graph
        Model reifiedModel = ModelFactory.createDefaultModel();
        reifiedModel.setNsPrefix("reification", ng);

        //traverse over all triples of the .ttl file
        StmtIterator itr = model.listStatements(); //iterator
        while (itr.hasNext()) {
            Statement stmt = itr.nextStatement(); //statement
            Resource subj = stmt.getSubject(); //subject
            Property pred = stmt.getPredicate(); //predicate
            RDFNode obj = stmt.getObject(); //object

            //Create blank node for reification
            Resource blankNode = reifiedModel.createResource();

            // Adding reified statements 
            reifiedModel.add(blankNode, RDF.type, RDF.Statement);
            reifiedModel.add(blankNode, RDF.subject, subj);
            reifiedModel.add(blankNode, RDF.predicate, pred);
            reifiedModel.add(blankNode, RDF.object, obj);

            
            Resource namedSubj = reifiedModel.createResource(subj.getURI());
            namedSubj.addProperty(reifiedModel.createProperty(ng, "statement"), blankNode);
        }

        //write the reified model to a reified_ng.ttl file
        try {
            OutputStream outStr = new FileOutputStream("reified_ng.ttl");
            reifiedModel.write(outStr, "TTL");
        } catch (FileNotFoundException error) {
        	error.printStackTrace();
        }
    }
}
