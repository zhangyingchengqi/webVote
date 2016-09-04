package com.hcq.utils;

import java.io.File;
import java.io.FileNotFoundException;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;  
import org.apache.lucene.document.Field;  
import org.apache.lucene.document.TextField;  
import org.apache.lucene.document.Field.Store;  
import org.apache.lucene.index.DirectoryReader;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.index.Term;  
import org.apache.lucene.index.IndexWriterConfig.OpenMode;  
import org.apache.lucene.queryparser.classic.ParseException;  
import org.apache.lucene.queryparser.classic.QueryParser;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.search.Query;  
import org.apache.lucene.search.ScoreDoc;  
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.FSDirectory;  
import org.apache.lucene.util.Version;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.hcq.service.VoteService;
import com.hcq.service.impl.VoteServiceImpl;
import com.hcq.vote.entity.VoteSubject;  
  
public class LuceneUtil  
{  
    private static Directory directory;  
    private IndexWriter writer;  
    private IndexReader reader;  
    private VoteService vss = new VoteServiceImpl();
    static  
    {  
        try  
        {  
            // 读取硬盘上的索引信息  
            directory = FSDirectory.open(new File("D://index"));  
            // 读取内存中的索引信息 因为是在内存中 所以不需要指定索引文件夹  
            //内存存储：优点速度快，缺点程序退出数据就没了，所以记得程序退出时保存索引库，FSDirectory结合使用
            //由于此处只暂时保存在内存中，程序退出时没进行索引库保存，因此在搜索时程序会报错
            // directory = new RAMDirectory();  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 将Directory做成静态对象 便于获取 
     *  
     * @return 
     */  
    public static Directory getDirectory()  
    {  
        return directory;  
    }  
  
    /** 
     * 获取IndexWriter对象 
     * @return 
     */  
    public IndexWriter getWriter(OpenMode createOrAppend)  
    {  
        if (writer != null)  
            return writer;  
  
        Analyzer analyzer = new MMSegAnalyzer();  
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);  
        if (createOrAppend == null)  
            // 默认策略为新建索引  
            conf.setOpenMode(OpenMode.CREATE);  
        else  
            conf.setOpenMode(createOrAppend);  
  
        try  
        {  
            writer = new IndexWriter(directory, conf);  
            return writer;  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public IndexReader getIndexReader()  
    {  
        try  
        {  
            DirectoryReader newReader = null;  
            // 判断reader是否为空 若为空就创建一个新的reader  
            if (reader == null)  
                reader = DirectoryReader.open(directory);  
            else  
                // 若不为空 查看索引文件是否发生改变 如果发生改变就重新创建reader  
                newReader = DirectoryReader.openIfChanged((DirectoryReader) reader);  
            if (newReader != null)  
                reader = newReader;  
            return reader;  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return null;  
  
    }  
  
    /** 
     * 获取IndexSearcher对象 
     *  
     * @return 
     */  
    public IndexSearcher getIndexSearcher()  
    {  
        return new IndexSearcher(getIndexReader());  
    }  
  
    /** 
     * 创建索引 有几个概念需要理解 1. Directory类似于数据库中的表 2. Document类似于数据库的一条记录 3. Field类似于数据库中一条记录的某一列 
     * @throws Exception 
     */  
    public void index() throws Exception  
    {  
        Document document = null;  
        writer = getWriter(OpenMode.CREATE);  
  
        // 设置需要被索引的对象集合
        List<VoteSubject>list = vss.getAllSubjects();
        // 遍历需要被索引的对象集合 
        for (VoteSubject vo:list)  
        {  
            document = new Document();  
            try  
            {  
                Field field = new TextField("title",vo.getTitle(),Store.YES);  
                document.add(field);  
                Field field1 = new TextField("vsid",String.valueOf(vo.getVsid()),Store.YES);  
                document.add(field1);  
                Field field2 = new TextField("optioncount",String.valueOf(vo.getOptioncount()),Store.YES);  
                document.add(field2); 
                Field field3 = new TextField("usercount",String.valueOf(vo.getUsercount()),Store.YES);  
                document.add(field3); 
                if (writer.getConfig().getOpenMode() == OpenMode.CREATE)  
                {  
                    System.out.println("adding " + vo);  
                    writer.addDocument(document);  
                } else  
                {  
                    System.out.println("updating " + vo);  
                    writer.updateDocument(new Term("path", vo.toString()), document);  
                }  
            } catch (FileNotFoundException e)  
            {  
                e.printStackTrace();  
            } catch (IOException e)  
            {  
                e.printStackTrace();  
            }  
        }  
        try  
        {  
            // 如果不是时常创建索引 一定要记得关闭writer 当然也可以将writer设计成单例的  
            if (writer != null)  
                writer.close();  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
    public List<VoteSubject> search(String queryStr, int num) throws InvalidTokenOffsetsException  
    {  
    	Analyzer analyzer = new MMSegAnalyzer();  
        //使用分词器  
        QueryParser parser = new QueryParser("title", analyzer);  
        IndexSearcher searcher = getIndexSearcher();  
        List<VoteSubject>list=new ArrayList<VoteSubject>();
        try  
        {  
            Query query = parser.parse(queryStr);  
            TopDocs docs = searcher.search(query, num);  
            System.out.println("一共搜索到结果:" + docs.totalHits + "条");
            
            /**自定义标注高亮文本标签*/ 
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");  
            /**创建QueryScorer    对命中结果进行评分操作*/
            QueryScorer scorer=new QueryScorer(query);  
            /**创建Fragmenter 将原始字符串拆分成独立的片段*/
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);  
            //高亮分析器
            Highlighter highlight=new Highlighter(formatter,scorer);  
            highlight.setTextFragmenter(fragmenter);  
          
            
            Set<String> fieldSet = new HashSet<String>();  
            fieldSet.add("vsid"); 
            fieldSet.add("title");
            fieldSet.add("usercount");
            fieldSet.add("optioncount");
            for (ScoreDoc scoreDoc : docs.scoreDocs)  
            {  
            	Document document = searcher.doc(scoreDoc.doc,fieldSet); 
            	VoteSubject voteSubject=new VoteSubject();
                System.out.println("title===="+document.get("title"));
                System.out.println("vsid===="+document.get("vsid"));
                
                //高亮显示
            	String str = highlight.getBestFragment(new MMSegAnalyzer(), "title",document.get("title"));
                
            	voteSubject.setTitle(str);
                voteSubject.setVsid(Long.valueOf(document.get("vsid")));
                voteSubject.setOptioncount(Integer.valueOf(document.get("optioncount")));
                voteSubject.setUsercount(Integer.valueOf(document.get("usercount")));
                list.add(voteSubject);
            }  
        } catch (ParseException e)  
        {  
            e.printStackTrace();  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return list;
    }  
}  
