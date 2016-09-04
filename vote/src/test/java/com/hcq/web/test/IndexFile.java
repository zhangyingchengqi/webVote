package com.hcq.web.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.standard.StandardAnalyzer;  
import org.apache.lucene.document.Document;  
import org.apache.lucene.document.Field.Store;  
import org.apache.lucene.document.StringField;  
import org.apache.lucene.document.TextField;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.FSDirectory;  
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.hcq.service.VoteService;
import com.hcq.service.impl.VoteServiceImpl;
import com.hcq.vote.entity.VoteSubject;  
  
public class IndexFile {  
	private static VoteService vss = new VoteServiceImpl();
    private static Directory dir;  
    protected static String[] ids={"1", "2"};  
    /** 
     * 初始添加文档 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {
	 
        String pathFile="D://index";  
        dir=FSDirectory.open(new File(pathFile));  
        IndexWriter writer=getWriter();
        List<VoteSubject>list = vss.getAllSubjects();
        for(VoteSubject vo:list) {  
            Document doc=new Document();  
            doc.add(new StringField("title",vo.getTitle(), Store.YES));  
            writer.addDocument(doc);  
            System.out.println(doc);  
        }  
        writer.close();  
    }   
  
    /** 
     * 获得IndexWriter对象 
     * @return 
     * @throws Exception 
     */  
    public static IndexWriter getWriter() throws Exception {  
        Analyzer analyzer=new MMSegAnalyzer();  
        IndexWriterConfig iwc=new IndexWriterConfig(Version.LUCENE_4_10_4,analyzer);  
        return new IndexWriter(dir, iwc);  
    }  
  
}  
