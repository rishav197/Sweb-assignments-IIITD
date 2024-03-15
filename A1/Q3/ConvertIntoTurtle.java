import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConvertIntoTurtle{

    public static void main(String[] args) {
        
        String inFile = "C:\\Users\\RISHAV\\eclipse-workspace\\SwebA1Q2\\rdf_star_input.nt";
        String outFile = "C:\\Users\\RISHAV\\eclipse-workspace\\SwebA1Q2\\rdf_star_output.ttl";

        //conversion method that convert a .nt file into .ttl file
        convertNTtoTTL(inFile, outFile);
    }

    public static void convertNTtoTTL(String inputFile, String outputFile) {
        try {
            //instantiate an empty model
            Model model = ModelFactory.createDefaultModel();

            //reading the N-Triples from the file(rdf_star_input.nt) through the model
            try (InputStream instr = new FileInputStream(inputFile)) {
                RDFDataMgr.read(model, instr, Lang.NTRIPLES);
            }

            //Here, write the content to an output file(rdf_star_output.ttl) in Turtle format
            try (OutputStream outstr = new FileOutputStream(outputFile)) {
                RDFDataMgr.write(outstr, model, Lang.TURTLE);
            }

            System.out.println("Your file has been converted into turtle file successfully.");
        } catch (IOException error) {
            System.err.println("Error encountered: " + error.getMessage());
        }
    }
}
