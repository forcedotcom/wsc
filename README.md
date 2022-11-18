# Force.com Web Service Connector (WSC)

The Force.com Web Service Connector (WSC) is a high performing web service client stack implemented using a streaming parser. WSC also makes it much easier to use the Force.com API (Web Services/SOAP or Asynchronous/BULK API). 

## Building WSC
    git clone https://github.com/forcedotcom/wsc.git
    mvn clean package
    
To skip the gpg signing, run the following command

    mvn clean package -Dgpg.skip

## Generating Stubs From WSDLs
    java -jar target/force-wsc-56.2.0-uber.jar <inputwsdlfile> <outputjarfile>

* `inputwsdlfile` is the name of the WSDL to generate stubs for.
* `outputjarfile` is the name of the jar file to create from the WSDL.

## Write Application Code
The following sample illustrates creating a connection and creating a new Account SObject.  Login is automatically handled by the Connector.

```java
    import com.sforce.soap.partner.*;
    import com.sforce.soap.partner.sobject.*;
    import com.sforce.ws.*;

    public static void main(String[] args) {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername("username");
        config.setPassword("password");

        PartnerConnection connection = Connector.newConnection(config);
        SObject account = new SObject();
        account.setType("Account");
        account.setField("Name", "My Account");
        connection.create(new SObject[]{account});
    }

