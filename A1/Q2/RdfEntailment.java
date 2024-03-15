import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RdfEntailment{

    public static void main(String[] args) {
        
    	//path for custom.rules file which consist 10 entailment rules
        String rulesFile = "C:\\Users\\RISHAV\\eclipse-workspace\\SwebA1Q2\\custom.rules";

    	//path for rdf_graph.ttl file which consist of rdf graph in turtle format 
        String turtFile = "C:\\Users\\RISHAV\\eclipse-workspace\\SwebA1Q2\\rdf_graph.ttl";

        //instantiate an empty model
        Model model = ModelFactory.createDefaultModel();

        //Read the Turtle contents into the model
        try (InputStream inStr = FileManager.get().open(turtFile)) 
        {
            if (inStr == null) 
            {
                throw new IllegalArgumentException("File: " + turtFile + " is NOT Found");
            }
            model.read(inStr, null, "TTL");
            
        } catch (Exception error) {
        	error.printStackTrace();
        }

        //Loading 10 Entailment Rules from the custom.rules file
        Reasoner reasoner = rdf_reasoner(rulesFile);

        //create instance inference model
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);
        
        //generating inference using the entailment rules
        StmtIterator stmtItr = infModel.listStatements();
        while(stmtItr.hasNext()){
            Statement stmt = stmtItr.nextStatement(); //stmt->inferred_statement
            System.out.println(stmt+"\n");
        }
    }

    private static Reasoner rdf_reasoner(String rulesFile) {
        
    	//Load rules from the Rules file
        List<Rule> rules = loadRules(rulesFile);
        
        //Reasoner Instance
        return new GenericRuleReasoner(rules);
    }

    //Here, we traversing over each entailment rule then return a list of rules
    private static List<Rule> loadRules(String rulesFile) {
        List<Rule> rules = new ArrayList<>();
        
        //Error Handling
        try (InputStream inStr = FileManager.get().open(rulesFile)) {
            if (inStr == null) 
            {
                throw new IllegalArgumentException("File : " + rulesFile + " is NOT Found");
            }
            
            //create a rule iterator 
            Iterator<Rule> ruleItr = Rule.rulesFromURL(rulesFile).iterator();
            
            while (ruleItr.hasNext()) 
            {
                rules.add(ruleItr.next());
            }
            
        } catch (Exception error) {
        	error.printStackTrace();
        }
        return rules;
    }

}