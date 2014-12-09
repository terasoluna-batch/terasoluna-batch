package jp.terasoluna.fw.batch.dao.support;

public class TestBean001a {
    private String hoge = null;

    protected boolean throwException = false;

    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    public String getHoge() {
        if (this.throwException) {
            throw new IllegalArgumentException("hoge");
        }
        return hoge;
    }
}
