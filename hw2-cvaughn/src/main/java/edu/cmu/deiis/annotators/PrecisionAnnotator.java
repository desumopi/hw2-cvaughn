package edu.cmu.deiis.annotators;

import java.util.ArrayList;
import java.util.Iterator;

import edu.cmu.deiis.types.*;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;


/**
 * Annotator that sorts answers by the number of NGrams they 
 * share with the question.
 */
public class PrecisionAnnotator extends JCasAnnotator_ImplBase {
  
  public void process(JCas aJCas) {
    System.out.println("CURRENTLY RUNNING PrecisionAnnotator.java");
    
    // get the answers from the CAS (and store in ArrayList):
    int N=0;
    ArrayList<Answer> ansArray = new ArrayList<Answer>();
    
    FSIndex ansIndex = aJCas.getAnnotationIndex(Answer.type);
    Iterator ansIter = ansIndex.iterator();

    while (ansIter.hasNext()) {
      Answer currAns = (Answer) ansIter.next();
      ansArray.add(currAns);
      if (currAns.getIsCorrect()) {
        N++;
      }
    }
    
    // get an ArrayList of all the AnswerScores from the CAS
    ArrayList<AnswerScore> asArray = new ArrayList<AnswerScore>();
    
    FSIndex asIndex = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator asIter = asIndex.iterator();

    while (asIter.hasNext()) {
      AnswerScore currAS = (AnswerScore) asIter.next();
      asArray.add(currAS);
    }
    
    // sort the AnswerScores into descending order:
    sortArray(asArray);
    
    ArrayList<AnswerScore> asTopN = new ArrayList<AnswerScore>();
    
    for (int x=0; x<N; x++) {
      asTopN.add(asArray.get(x));
    }
    
    
  }
  
  
  
  
  private void sortArray(ArrayList<AnswerScore> asArray) {
    int maxInd;
    AnswerScore temp;
    
    for (int i=0; i<asArray.size(); i++) {
      maxInd = i;
      for (int j=0; j<asArray.size(); j++) {
        if (asArray.get(j).getScore() > asArray.get(maxInd).getScore()) {
          maxInd = j;
        }
      }
      if (i != maxInd) {
        temp = asArray.get(i);
        asArray.set(i, asArray.get(maxInd));
        asArray.set(maxInd, temp);
      }
    }
  }
  
}