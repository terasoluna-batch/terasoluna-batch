package jp.terasoluna.fw.collector.db;

public class TestBean001 {
    @SuppressWarnings("unused")
    private String hoge = null;

    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    public String getHoge() {
        throw new RuntimeException();
    }

}
