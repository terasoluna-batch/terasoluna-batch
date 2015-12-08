package jp.terasoluna.fw.collector.db;

public class HogeBean {

    private String hoge = null;

    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    public String getHoge() {
        return hoge;
    }

    public static HogeBeanBuilder buider() {
        return new HogeBeanBuilder();
    }

    public static class HogeBeanBuilder {

        private String hoge = null;

        private HogeBeanBuilder() {
        }

        public HogeBeanBuilder hoge(String hoge) {
            this.hoge = hoge;
            return this;
        }

        public HogeBean build() {
            HogeBean hogeBean = new HogeBean();
            hogeBean.setHoge(hoge);
            return hogeBean;
        }
    }
}
