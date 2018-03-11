package driver;
import adt.Row;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JaxB {
	public static void main(String[] args) {

		  Row row = new Row();
		  row.put("aaaa", 1);
		  row.put("aaa", 2);
		  row.put("aa", 3);
		  row.put("a", 4);
		  try {

			File file = new File("file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Row.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(row, file);
			jaxbMarshaller.marshal(row, System.out);

		      } catch (JAXBException e) {
			e.printStackTrace();
		      }

		}
}