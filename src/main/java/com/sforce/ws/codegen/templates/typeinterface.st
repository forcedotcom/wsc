typeinterface(gen) ::=<<
package $gen.packageName$;

$if(gen.addDeprecatedAnnotation)$
import java.lang.Deprecated;
$endif$
/**
 * This is a generated class for the SObject Enterprise API.
 * Do not edit this file, as your changes will be lost.
 */
$if(gen.addDeprecatedAnnotation)$
@Deprecated
$endif$
public interface $gen.interfaceName$ $if(gen.hasBaseComplexClass)$extends $gen.baseComplexClassInterface$$endif$ {

    $gen.memberMetadataList: {member |
  /**
   * element : $member.elementDoc$
   * java type: $member.javaType$
   */

  public $member.javaTypeInterface$ $member.getMethodName$();

  $if(member.booleanJavaType)$
  public $member.javaType$ $member.boolMethodName$();
$endif$

  public void $member.setMethodName$($member.javaTypeInterface$ $member.fieldName$);

   }$
}

>>
