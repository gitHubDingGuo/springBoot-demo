public enum TypeEnum {

      JAVA(".java","java"),
      XML(".xml","xml"),
      YML(".yml","yml"),
      PNG(".png","png"),
      TXT(".txt","txt");

    private String prefix;
    private String data;


    TypeEnum(String prefix,String data) {
        this.prefix=prefix;
        this.data=data;
    }

    public static String getName(String data){
        String name = "";
        for(TypeEnum typeEnum:TypeEnum.values()){
            if(data.contains(typeEnum.getPrefix())){
                name=typeEnum.getData();
            }
        }
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}


