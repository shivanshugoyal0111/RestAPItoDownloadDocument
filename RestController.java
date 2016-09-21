@RequestMapping(value="/download", method = RequestMethod.POST)
    public void downloadDocument(@RequestParam("id") long id, HttpServletResponse httpServletResponse) {
        Map response = documentController.downloadDocument(id);
        Map result = (Map) response.get("result");

        String documentName = "documentNotFound.txt";
        byte[] documentData = "Document not found for the given document id".getBytes();

        if(null != result) {
            documentName = result.get("documentName").toString();
            documentData = (byte[]) result.get("documentData");
        }

        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "inline; filename='" + documentName +"'");
        BufferedOutputStream bout = null;

        try {
            ServletOutputStream out = httpServletResponse.getOutputStream();
            bout = new BufferedOutputStream(out);
            bout.write(documentData);
            bout.flush();
            bout.close();
            out.close();
        } catch (Exception exception) {
            LOGGER.debug("exception while writing bytes back to document", exception);
        }finally {
            try {
                bout.close();
            }catch (Exception exception){
                LOGGER.debug("exception while writing bytes back to document", exception);
            }
        }
    }
