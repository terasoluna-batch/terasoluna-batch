package jp.terasoluna.fw.batch.dao.support;

public class TestBean001 {
    private String hoge = null;

    private TestBean001a testBean001a = null;

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

    public void setTestBean001a(TestBean001a testBean001a) {
        this.testBean001a = testBean001a;
    }

    public TestBean001a getTestBean001a() {
        if (this.throwException) {
            throw new IllegalArgumentException("hoge");
        }
        return testBean001a;
    }

}
