package com.hcq.web.test;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;  
import org.apache.lucene.index.DirectoryReader;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;  
import org.apache.lucene.search.TermQuery;  
import org.apache.lucene.search.TopDocs;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.FSDirectory;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.sun.org.apache.bcel.internal.generic.NEW;  
  
public class IndexSearch {  
  
    /** 
     * 查询 
     * @throws Exception 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {
    	Analyzer analyzer=new MMSegAnalyzer(); 
        String filePath="D://index";  
        Directory dir=FSDirectory.open(new File(filePath));  
        IndexReader reader=DirectoryReader.open(dir);  
        IndexSearcher searcher=new IndexSearcher(reader);  
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse("where");
        TopDocs topdocs=searcher.search(query, null,100);  
        ScoreDoc[] scoreDocs=topdocs.scoreDocs;  
        System.out.println("查询结果总数---" + topdocs.totalHits+"最大的评分--"+topdocs.getMaxScore());
        
        for(int i=0; i < scoreDocs.length; i++) {  
            int doc = scoreDocs[i].doc;  
            Document document = searcher.doc(doc);  
            System.out.println("title===="+document.get("title"));  
        }  
        reader.close();  
    }  
}  
