public class CacheEntry {

    private final String value;
    private final Long expireAt;

    public CacheEntry(String value,Long expireAt){
        this.value = value;
        this.expireAt = expireAt;
    }

    public String getValue(){
        return value;
    }

    public Long getExpireAt(){
        return expireAt;
    }

    public boolean isExpired(){
        if(expireAt==null){
            return false;
        }
        return System.currentTimeMillis() > expireAt;
    }
}
