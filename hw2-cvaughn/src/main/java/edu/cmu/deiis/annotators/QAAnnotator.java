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
    System.out.println("CURRENTLY RUNNING QAAnnotator.java");
    
    String docText = aJCas.getDocumentText();
    
    // get an ArrayList of all the Tokens from the CAS
    ArrayList<Token> tokArray = new ArrayList<Token>();
    
    FSIndex tokIndex = aJCas.getAnnotationIndex(Token.type);
    Iterator tokIter = tokIndex.iterator();

    while (tokIter.hasNext()) {
      Token currTok = (Token) tokIter.next();
      tokArray.add(currTok);
    }
    
    System.out.println("tokArray:");
    for(int tmp=0; tmp < tokArray.size(); tmp++) {
      System.out.println(docText.substring(tokArray.get(tmp).getBegin(), tokArray.get(tmp).getEnd()));
    }
    
    // find the question and answer indices in the text
    for(int i=0; i < tokArray.size(); i++) {
      Token tok = tokArray.get(i);
      int begin = tok.getBegin();
      int end = tok.getEnd();
      
      String tokString = docText.substring(begin, end);
      
      System.out.println("Looking for a question. Currently on token " + tokString + " at index " + begin);
      
      if (tokString.equals("Q")) {
        // this is the beginning of a question! Yay! Note that.
        Question quest = new Question(aJCas);
        quest.setBegin(begin+2);
        
        // find the question's ending:
        int k = i;
        while ( k < tokArray.size() && !tokArray.get(k).equals("A") ) {
          k++;
        }
        Token lastTok = tokArray.get(k-1);
        quest.setEnd(lastTok.getEnd());
        quest.addToIndexes();
        System.out.println("Just made a question with the span from " + (begin+2) + " to " + lastTok.getEnd());
        
      } else if (tokString.equals("A")) {
        
        Answer ans = new Answer(aJCas);
        ans.setBegin(begin + 2);
        
        // find the question's ending:
        int k = i;
        while ( k < tokArray.size() && !tokArray.get(k).equals("A") ) {
          k++;
        }
        Token lastTok = tokArray.get(k-1);
        ans.setEnd(lastTok.getEnd());
        ans.addToIndexes();
        System.out.println("Just made an answer with the span from " + (begin+2) + " to " + lastTok.getEnd());
        
      }
    }
    
  }
}