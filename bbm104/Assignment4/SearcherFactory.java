public class SearcherFactory {
    public static Board board = null;
    public static SearcherInterface getSearcher(String data)
    {
        BaseSearcher searcher = null;
        if(data.equals("T"))
            searcher = new T_Searcher();
        else if(data.equals("D"))
            searcher = new D_Searcher();
        else if(data.equals("S"))
            searcher = new S_Searcher();
        else if(data.equals("W"))
            searcher = new W_Searcher();
        else if(data.equals("/"))
            searcher = new Slash_Searcher();
        else if(data.equals("\\"))
            searcher = new BackSlash_Searcher();
        else if(data.equals("-"))
            searcher = new Minus_Searcher();
        else if(data.equals("+"))
            searcher = new Plus_Searcher();
        else if(data.equals("|"))
            searcher = new Or_Searcher();

        if(searcher != null)
            searcher.setBoard(board);

        return searcher;
    }
}
