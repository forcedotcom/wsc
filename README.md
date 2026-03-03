# Force.com Web Service Connector (WSC)

The Force.com Web Service Connector (WSC) is a high performing web service client stack implemented using a streaming parser. WSC also makes it much easier to use the Force.com API (Web Services/SOAP or Asynchronous/BULK API). 

## Building WSC
    git clone https://github.com/forcedotcom/wsc.git
    mvn clean package
    
To skip the gpg signing, run the following command

    mvn clean package -Dgpg.skip

During packaging, there is a dependency security check that requires additional setup.  This dependency check can be skipped by running the following command

    mvn clean package -Ddependency-check.skip=true

In order to build the WSC while including the dependency check, create an account with https://ossindex.sonatype.org and obtain an API token.  Add the following server entry to the .m2/settings.xml

    <server>
        <id>ossindex</id>
        <username>[Enter the username for ossindex.sonatype.org]</username>
        <password>[Enter the API token for ossindex.sonatype.org]</password>
    </server>

## Generating Stubs From WSDLs
    java -jar target/force-wsc-66.0.0-uber.jar <inputwsdlfile> <outputjarfile>

* `inputwsdlfile` is the name of the WSDL to generate stubs for.
* `outputjarfile` is the name of the jar file to create from the WSDL.
* `-dep` if you want to have @Deprecated annotations added to all generated stubs.
* `-nc` if you don't want to compile the stubs.

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

