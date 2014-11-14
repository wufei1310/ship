/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util
import org.grails.plugins.excelimport.*

/**
 *
 * @author DELL
 */
class GoodsExcelImporter extends AbstractExcelImporter {
    
        def static cellReporter = new DefaultImportCellCollector()
        
        static Map configuratiomMap = [	
            market: ([expectedType: ExpectedPropertyType.StringType]),	
            floor: ([expectedType: ExpectedPropertyType.StringType]),	
            stalls: ([expectedType: ExpectedPropertyType.StringType]),	
            goods_sn: ([expectedType: ExpectedPropertyType.StringType]),	
            spec: ([expectedType: ExpectedPropertyType.StringType]),	
            num: ([expectedType: ExpectedPropertyType.StringType]),	
            price: ([expectedType: ExpectedPropertyType.StringType]),	
        ]
    
	static Map<String,String> CONFIG_BOOK_COLUMN_MAP = [
          sheet:'Sheet1', 
			 startRow: 2,
          columnMap:  [
                  'A':'market',
                  'B':'floor',
                  'C':'stalls',
                  'D':'goods_sn',
                  'E':'spec',
                  'F':'num',
                  'G':'price',
          ]
        ]    //can also configure injection in resources.groovy
        
	def getExcelImportService() {
		ExcelImportService.getService()
	}  
       
    GoodsExcelImporter(){
        super();
    }
     GoodsExcelImporter(InputStream inp){
        this.read(inp)
    }
    
     GoodsExcelImporter(String fileName) {
        super(fileName)
    }   
  
    List<Map> getGoods() {
       List bookList = excelImportService.columns(workbook, CONFIG_BOOK_COLUMN_MAP,cellReporter,configuratiomMap)
    }
 
}

