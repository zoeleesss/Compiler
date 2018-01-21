package parser.entity;

import parser.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sss on 02/01/2018.
 */
public class MyObjectTranslation {


    private static List<MyObject> leaves=MyUtil.leaves,allNodes=MyUtil.allNodes;
    private static MyObject root=MyUtil.root;
    private static List<Grammar> grammarList = MyUtil.grammarList;
    private static MyObjectTranslation instance=null;

    public static MyObjectTranslation getInstance()
    {
        if (instance==null)
        {
            instance=new MyObjectTranslation();
        }
        return instance;
    }

    private MyObjectTranslation()
    {

    }





}


