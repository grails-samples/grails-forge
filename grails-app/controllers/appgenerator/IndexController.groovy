package appgenerator

class IndexController implements CurlAware {
	
    def index() {
        if(isCurlRequest()) {
            response.contentType = "text/plain"
            response.outputStream << this.class.classLoader.getResourceAsStream('curl_instructions.txt').bytes
        } else {
            forward(uri: '/index.html')
        }
    }
}
