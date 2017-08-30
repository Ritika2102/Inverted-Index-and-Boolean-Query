//import java.lang.Object;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class practice {
	static HashMap<String,LinkedList> mapInv=new HashMap();
	 static String nL = System.lineSeparator();
	 String str="";
	 int comp=0;
	 int compOr=0;
	 public static LinkedList<Integer> testA = new LinkedList<Integer>();
	 public static LinkedList<Integer> testB = new LinkedList<Integer>();
		public static LinkedList<Integer> testC = new LinkedList<Integer>();
		public static LinkedList<Integer> testD = new LinkedList<Integer>();
		public static LinkedList<Integer> testE = new LinkedList<Integer>();
	
	
 public static void main(String args[]) throws IOException {
	 String dirRead=args[0];
	 String OutputFilePath=args[1];
	 String InputFilePath=args[2]; 
	// System.out.println(dirRead);
	// System.out.println(OutputFilePath);
	// System.out.println(InputFilePath);
	// String dirRead="C:\\Users\\hp\\Desktop\\Fall2016\\index";
	 String term=null;
	 File f1 = new File (InputFilePath);
	 Path f=Paths.get(dirRead);
	 
	 Directory indexDir=FSDirectory.open(f);
	 IndexReader indexReader=DirectoryReader.open(indexDir);
     Fields fields = MultiFields.getFields(indexReader);
     for(String field: fields){
    	 if(!field.equals("id") && !field.equals("_version_")){ 
     Terms terms = fields.terms(field);
     TermsEnum iterator = terms.iterator();
     
     BytesRef byteRef ;
    
     int count1=0;
     while((byteRef = iterator.next()) != null) {
    	
          term = byteRef.utf8ToString();
         PostingsEnum postingsEnum = MultiFields.getTermDocsEnum(indexReader,
				    field, byteRef, PostingsEnum.FREQS);   //PostingsEnum- associated stream of docs
 		 int i;
 	//	System.out.println("term"+":"+term);
 		LinkedList <Integer> l1= new LinkedList<Integer>();
 		 while ((i = postingsEnum.nextDoc()) != PostingsEnum.NO_MORE_DOCS) 
 		 {
 			
 			 int doc = postingsEnum.docID(); // The document
 			// int freq = postingsEnum.freq(); // Frequency of term in doc
 			 l1.add(doc);
 			mapInv.put(term,l1);
 			//System.out.println("DocID"+doc);
 		 }
 		
          count1++;
       //   System.out.println("Term"+" "+term+" "+l1
       //   f1.write(field+" "+"Term"+":"+term+" "+l1+"\n");
          }

     }
     }
      
     InputStreamReader ir = new InputStreamReader(new FileInputStream(InputFilePath), StandardCharsets.UTF_8);
     BufferedReader b1 = new BufferedReader(ir);
  	String line;
  	practice p1=new practice();
  	
  	while((line = b1.readLine()) != null )
  	{  
  	// System.out.println(line);
  		p1.resultTaatand(line);
  		p1.resultTaatOr(line);
  		p1.ResultDaatAnd(line);
  		p1.ResultDaatOr(line);
     }
  	b1.close();
  	writeToFile(OutputFilePath, p1.str);
  
     for (Entry<String, LinkedList> entry : mapInv.entrySet()) {
	     //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	    // f1.write("key"+" "+ entry.getKey()+" "+"value"+" "+ entry.getValue()+"\n");
	 }
     
 }
 public void resultTaatand(String line) throws IOException{
	 comp=0;
	 line=line.trim();
	 String arr[]=line.split(" ");
	// System.out.println(line);
	
	 ArrayList<LinkedList<Integer>> lists = new ArrayList<>();
	  //ArrayList<String> allterms = new ArrayList<String>(arr);
	 
	 for(String term : arr)
	 {
		// System.out.println(term);
		 LinkedList <Integer> posting= mapInv.get(term);
		 lists.add(posting);
		 str=str+"GetPostings"+nL+term+nL+"Postings list: ";
		// System.out.println(posting);
		 for(int i : posting)
		 {
			 str+=i;
			 str+=" ";
		 }
		 str= str.substring(0, str.length()-1);
		 str+=nL;
		// str= str.substring(0, str.length()-1);
	 }
	// writeToFile(OutputFilePath, str);
	// str="";
	 str =str+"TaatAnd"+nL+line;
	 int nTerms = arr.length;
	// System.out.println(nTerms);
	 int i=1;
	 LinkedList<Integer> result = lists.get(0);
	 while(i<nTerms)
	 {
		 LinkedList list = lists.get(i);
		 result = findingTaatAnd(result, list);
		// System.out.println(result);
		 i++;
	 }
	 str = str+nL+"Results: ";
	 if(result.size()!=0){
	 for(int j:result)
	 {
		 str= str+j+" ";
	 }
	str= str.substring(0, str.length()-1);
	 }
	 else{
		 str+="empty";
	 }
	// System.out.println(result);
	 str+=nL;
	 str+="Number of documents in results: ";
	 str+=result.size();
	 str+=nL;
	 str+="Number of comparisons: ";
	 str+=comp;
	 str+=nL;
	 
 }
 public static void writeToFile(String filePath, String output) throws IOException
 {
	 File file = new File(filePath);
	 FileOutputStream fw = new FileOutputStream(file);
	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fw, StandardCharsets.UTF_8));
	 bw.write(output);
	 bw.close();
 }
    	 
 public LinkedList <Integer> findingTaatAnd( LinkedList <Integer> posting1, LinkedList <Integer> posting2 ){
      LinkedList <Integer> answerAnd= new LinkedList<Integer>();
    	 int k=0;
    	 int l=0;
    	 int comp1=0;
    	 int total=0;
    	 int sizeP1=posting1.size();
    	 int sizeP2=posting2.size();
    	 while(k<sizeP1 && l<sizeP2){
    		 if(posting1.get(k).equals(posting2.get(l))){
    			 answerAnd.add(posting1.get(k));
    			 k++;
    			 l++;
    			 comp1++;
    		 }
    		 
    		 else if(posting1.get(k)>posting2.get(l)){
    			 l++;
    			 comp1++;
    		 }
    		 else if(posting1.get(k)<posting2.get(l)){
    			 k++;
    			 comp1++;
    		 }
    		 
    	 }
    	 comp=comp+comp1;
    	 return answerAnd;
    	 
 }
 public LinkedList <Integer> findingTaatOr(LinkedList <Integer> posting1, LinkedList <Integer> posting2){
	 LinkedList <Integer> answerOr= new LinkedList<Integer>();
	 int m=0;
	 int n=0;
	 int comp1=0;
	 
	 int sizeP1Or=posting1.size();
	 int sizeP2Or=posting2.size();
	 while(m<sizeP1Or && n<sizeP2Or){
		 if(posting1.get(m).equals(posting2.get(n))){
			 answerOr.add(posting1.get(m));
			 comp1++;
			 m++;
			 n++;
		 }
		 else if(posting1.get(m)>posting2.get(n)){
			 answerOr.add(posting2.get(n));
			 comp1++;
			 n++;
		 }
		 else if(posting1.get(m)<posting2.get(n)){
			 answerOr.add(posting1.get(m));
			 comp1++;
			 m++;
		 } 
		 
	 }
	 compOr=compOr+comp1;
	 while(m<=(sizeP1Or-1)){
		 answerOr.add(posting1.get(m)); 
		 m++;
		 }
	 while(n<=(sizeP2Or-1)){
	  answerOr.add(posting2.get(n));
	  n++;
		}
return answerOr;
} 
public void resultTaatOr(String line) throws IOException{
	compOr=0;
	 line=line.trim();
	 String arr[]=line.split(" ");
	// System.out.println(line);
	
	 ArrayList<LinkedList<Integer>> lists = new ArrayList<>();
	 for(String term : arr)
	 {
		// System.out.println(term);
		 LinkedList <Integer> posting= mapInv.get(term);
		 lists.add(posting);
		// str=str+"GetPostings"+nL+term+nL+"Postings list: ";
		// System.out.println(posting);
		 for(int i : posting)
		 {
			// str+=i;
			// str+=" ";
		 }
		// str+=nL;
	 }
	 //writeToFile("C:\\Users\\hp\\Desktop\\Fall2016\\output.txt", str);
	// str="";
	 str =str+"TaatOr"+nL+line;
	 int nTerms = arr.length;
	// System.out.println(nTerms);
	 int i=1;
	 LinkedList<Integer> result = lists.get(0);
	 while(i<nTerms)
	 {
		 LinkedList list = new LinkedList();
		 list.addAll(lists.get(i));
		 result = findingTaatOr(result, list);
		// System.out.println(result);
		 i++;
	 }
	 str = str+nL+"Results: ";
	 for(int j:result)
	 {
		 str= str+j+" ";
	 }
	str= str.substring(0, str.length()-1);
	// System.out.println(result);
	 str+=nL;
	 str+="Number of documents in results: ";
	 str+=result.size();
	 str+=nL;
	 str+="Number of comparisons: ";
	 str+=compOr;
	 str+=nL;
	 
	 
 }
 
 public void ResultDaatAnd( String line ){
	 int cmp = 0;
	 int numTerms;
//	 ArrayList<LinkedList<Integer>> saving = new ArrayList<LinkedList<Integer>>();
//	 saving.add(testA);
//	 saving.add(testB);
//	 saving.add(testC);
//	 saving.add(testD);
//	 saving.add(testE);
//	 ArrayList<Integer> pointers = new ArrayList<Integer>();
//	 ArrayList<Integer> sizes = new ArrayList<Integer>();
//	 for(LinkedList<Integer> list : saving)
//	 {
//		 System.out.println(list);
//		 pointers.add(0);
//		 sizes.add(list.size());
//	 }
//	 LinkedList<Integer> answerDaatAnd = new LinkedList<Integer>();
	 line=line.trim();
	 String arr[]=line.split(" ");
	 //System.out.println(line);
	 
	 
 numTerms=arr.length;
 int z=0;
 ArrayList<LinkedList<Integer>> saving = new ArrayList<>();
 ArrayList<Integer> pointers=new ArrayList<>();
 ArrayList<Integer> sizes=new ArrayList<>();
  LinkedList <Integer> answerDaatAnd= new LinkedList<Integer>();
  while(z<numTerms){
	  pointers.add(0);
   //   System.out.println(arr[z]);
      
       LinkedList <Integer> postingDaat= mapInv.get(arr[z]);
       
    //   System.out.println(postingDaat.size());
       saving.add(postingDaat);
       sizes.add(postingDaat.size());
       z++;
  }
  
  int a=0;
  LinkedList<Integer> term1List = saving.get(0);
  boolean listChk = false;
  for(int term1Id : term1List)
  {
	//  System.out.println("The term in play: "+term1Id);
	  int t = 1;
	  boolean isDocAvail = true;
	  while(t<saving.size())
	  {
		//  System.out.println("Checking with list ::: "+t);
		  LinkedList<Integer> p = saving.get(t);
		  int ptr = pointers.get(t);
		 // System.out.println("Initial pointer ::: "+ptr);
		  int tsz = sizes.get(t);
		//  System.out.println("Size ::: "+tsz);
		  int curId = p.get(ptr);
		  while(ptr < tsz && term1Id > curId)
		  {
			  cmp++;
			  ptr++;
			  if(ptr>=tsz)
			  {
				  break;
			  }
			  curId = p.get(ptr);
		  }
		//  System.out.println("The pointer has moved to :: "+ptr);
		  cmp++;
		  if(ptr == tsz)
		  {
			  listChk = true;
			  pointers.set(t, ptr-1);
			  break;
		  }
		  else
		  {
			  pointers.set(t, ptr);
		  }
		  if(term1Id != curId)
		  {
			  cmp++;
			isDocAvail = false;
			break;
		  }
		  t++;
	  }
	  if(listChk)
	  {
		  break;
	  }
	  if(isDocAvail)
	  {
		  answerDaatAnd.add(term1Id);
	  }
	  
  }
//  System.out.println("The result for daatand is ");
//  System.out.println(answerDaatAnd);
//    while(a<saving.size()){
//         saving.get(0);
//  
//}
  str=str+"DaatAnd"+nL;
  str=str+line+nL;
  str=str+"Results: ";
  if(answerDaatAnd.size()!=0){
  for(int s : answerDaatAnd)
  {
	  str+=s;
	  str+=" ";
  }
  str=str.substring(0, str.length()-1);
  }
  else{
	  str+="empty";
  }
  str+=nL;
  str=str+"Number of documents in results: "+answerDaatAnd.size()+nL;
  str=str+"Number of comparisons: "+cmp;
  str+=nL;
  
 }
 public void ResultDaatOr( String line ){
	 int cmpOr = 0;
	 int numTerms;
	 /*ArrayList<Boolean> exLists = new ArrayList<Boolean>();
	 ArrayList<LinkedList<Integer>> saving = new ArrayList<>();
	 saving.add(testA);
	 saving.add(testB);
	 saving.add(testC);
	 saving.add(testD);
	 saving.add(testE);
	 ArrayList<Integer> pointers=new ArrayList<>();
	 ArrayList<Integer> sizes=new ArrayList<>();
	 for(LinkedList<Integer> list: saving)
	 {
		 exLists.add(false);
		 pointers.add(0);
		 sizes.add(list.size());
		 System.out.println("size of list ===> "+list.size());
	 }
	 numTerms = saving.size();
	 LinkedList <Integer> answerDaatOr= new LinkedList<Integer>();*/
	 line=line.trim();
	 String arr[]=line.split(" ");
	 numTerms=arr.length;
	 int z=0;
	 ArrayList<LinkedList<Integer>> saving = new ArrayList<>();
	 ArrayList<Integer> pointers=new ArrayList<>();
	 ArrayList<Integer> sizes=new ArrayList<>();
	 LinkedList <Integer> answerDaatOr= new LinkedList<Integer>();

	 ArrayList<Boolean> exLists = new ArrayList<Boolean>();
	 while(z<numTerms){
		  pointers.add(0);
		  exLists.add(false);
	    //  System.out.println(arr[z]);
	       LinkedList <Integer> postingDaat= mapInv.get(arr[z]);
	       
	    //   System.out.println(postingDaat.size());
	       saving.add(postingDaat);
	       sizes.add(postingDaat.size());
	       z++;
	  }
	 HashSet<Integer> fList = new HashSet<Integer>();
	 while(fList.size()<numTerms)
	 {
		 int i = -1;
		 int m = Integer.MAX_VALUE;
		 ArrayList<Integer> mList = new ArrayList<Integer>();
		 while(i<numTerms-1)
		 {
			 i++;
			 int ptr = pointers.get(i);
			 int size = sizes.get(i);
			 if(ptr >= size)
			 {
				 fList.add(i);
				 continue;
			 }
			 LinkedList<Integer> list = saving.get(i);
			 int cur = list.get(ptr);
		//	 System.out.println("Cur of list ::: "+i+" is "+cur+" at "+ptr+"th position");
			 cmpOr++;
			 if(m > cur)
			 {
				 cmpOr++;
				 m = cur;
				 mList = new ArrayList<Integer>();
				 mList.add(i);
			 }
			 else if(m == cur)
			 {
				 mList.add(i);
			 }
		 }
	//	 System.out.println("The minimum element ::: "+m);
	//	 System.out.println("The min list ::: "+mList);
		 if(m!=Integer.MAX_VALUE)
		 {
		 answerDaatOr.add(m);
		 }
		 for(int j : mList)
		 {
			 int ptr = pointers.get(j);
			 ptr++;
			 pointers.set(j, ptr);
		 }
	 }
	// System.out.println(answerDaatOr);
	 
	// System.out.println("size is ==> "+answerDaatOr.size());
	 
	 str=str+"DaatOr"+nL;
	  str=str+line+nL;
	  str=str+"Results: ";
	  for(int s : answerDaatOr)
	  {
		  str+=s;
		  str+=" ";
	  }
	  str=str.substring(0, str.length()-1);
	  str+=nL;
	  str=str+"Number of documents in results: "+answerDaatOr.size()+nL;
	  if(numTerms!=1){
	  str=str+"Number of comparisons: "+cmpOr;
	  }
	  else
	  {
		  str=str+"Number of comparisons: "+0;
	  }
	  str+=nL;
	  
 }

}