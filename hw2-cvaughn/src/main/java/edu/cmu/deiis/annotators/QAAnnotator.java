package edu.cmu.deiis.annotators;

import java.util.ArrayList;
import java.util.Iterator;

import edu.cmu.deiis.types.*;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;


/**
 * Annotator that detects and stores questions and answers
 * in a given text file.
 */
public class QAAnnotator extends JCasAnnotator_ImplBase {
  
  public void process(JCas aJCas) {
    String docText = aJCas.getDocumentText();
    
    // get an ArrayList of all the Tokens from the CAS
    ArrayList<Token> tokArray = new ArrayList<Token>();
    
    FSIndex tokIndex = aJCas.getAnnotationIndex(Token.type);
    Iterator tokIter = tokIndex.iterator();

    while (tokIter.hasNext()) {
      Token currTok = (Token) tokIter.next();
      tokArray.add(currTok);
    }
    
    ArrayList<Integer> aBegins = new ArrayList<Integer>();
    ArrayList<Integer> aEnds = new ArrayList<Integer>();
    
    // find the question and answer indices in the text
    Token prev;
    for(int i=0; i < tokArray.size(); i++) {
      Token tok = tokArray.get(i);
      int begin = tok.getBegin();
      int end = tok.getEnd();
      
      String tokString = docText.substring(begin, end);
      
      
      if (tokString.equals("Q")) {
        // this is the beginning of a question! Yay! Note that.
        Question quest = new Question(aJCas);
        quest.setBegin(begin+2);
        
        // find the question's ending:
        int k = i;
        while ( !tokArray.get(k).equals("A") ) {
          k++;
        }
        Token lastTok = tokArray.get(k-1);
        quest.setEnd(lastTok.getEnd());
        
        
      } else if (tokString.equals("A")) {
        
        Answer ans = new Answer(aJCas);
        ans.setBegin(begin + 2);
        
        // find the question's ending:
        int k = i;
        while ( !tokArray.get(k).equals("A") && k < tokArray.size()) {
          k++;
        }
        Token lastTok = tokArray.get(k-1);
        ans.setEnd(lastTok.getEnd());

      }
      prev = tok;
    }
    
  }
}