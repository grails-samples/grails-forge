package appgenerator

import grails.artefact.Controller

trait StreamsData implements Controller {

    void streamBytes(byte[] data, OutputStream outputStream) {
        response.setContentLength(data.size())
        outputStream << data
    }

    void outputToStream(String name, Closure closure) {
        response.setHeader("Content-disposition", "filename=\"${name}\"")
        outputToStream(closure)
    }

    void outputToStreamDirect(Closure closure) {
        def outputStream
        try {
            outputStream = response.outputStream
            if (closure.maximumNumberOfParameters == 1) {
                closure.call(outputStream)
            }
            if (closure.maximumNumberOfParameters == 2) {
                closure.call(outputStream, response)
            }
        } catch (IOException e){
            null
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close()
                } catch (IOException e) {
                    null
                }
            }
        }
    }

    void outputToStream(Closure closure) {
        response.setContentType("application/octet-stream")
        outputToStreamDirect(closure)
    }
}
