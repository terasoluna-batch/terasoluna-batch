package jp.terasoluna.fw.collector.db;

public class TestBean001 {
    private String hoge = null;

    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    public String getHoge() {
        if (true) {
            throw new RuntimeException();
        }
        return hoge;
    }

}
