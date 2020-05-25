package ttn.bootcamp.linksharing.enums;

public enum ResourceType {

        LINK("Link Resource"),
        DOCUMENT("Document Resource");
        String value;
        ResourceType(String resourceType){
            value = resourceType;
        }
        public String getValue(){
            return value;
        }

}
