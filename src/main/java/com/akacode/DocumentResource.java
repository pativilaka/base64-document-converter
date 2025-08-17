package com.akacode;

import com.akacode.common.PdfValidator;
import com.akacode.domain.dto.RetencaoB64DTO;
import com.akacode.domain.dto.RetencaoDTO;
import com.akacode.domain.mapper.RetencaoB64Mapper;
import com.akacode.domain.mapper.RetencaoMapper;
import com.akacode.infrastructure.extractor.RetencaoExtractor;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.nio.file.Files;

@Path("/docs")
@Produces(MediaType.APPLICATION_JSON)
public class DocumentResource {

    private final RetencaoExtractor extractor;

    @Inject
    public DocumentResource(RetencaoExtractor extractor) {
        this.extractor = extractor;
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@RestForm("file") FileUpload file) {
        try {
            byte[] bytes = Files.readAllBytes(file.uploadedFile());
            PdfValidator.ensurePdf(file.contentType(), bytes);

            // 1) extrai cru
            var raw = extractor.extractFields(bytes);
            // 2) normaliza (tipagem)
            RetencaoDTO dto = RetencaoMapper.toDto(raw);
            // 3) Base64
            RetencaoB64DTO b64 = RetencaoB64Mapper.toB64(dto);

            return Response.status(Response.Status.CREATED)
                    .entity(new Resp(dto, b64))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Err(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new Err("Erro ao processar PDF de retenção")).build();
        }
    }

    public record Resp(RetencaoDTO original, RetencaoB64DTO base64) {}
    public record Err(String error) {}
}
