package com.cinovo.backend.Utils;

import lombok.extern.jbosslog.JBossLog;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
@JBossLog
public class ImageProxyController
{
    //    private final WebClient client = WebClient.builder().defaultHeader("User-Agent", "Mozilla/5.0")
    //            .codecs(c -> c.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)).build();
    //
    //    @GetMapping("/proxy-image")
    //    public Mono<ResponseEntity<byte[]>> proxy(@RequestParam String url)
    //    {
    //        return client.get().uri(url).retrieve().toEntity(byte[].class).map(entity -> {
    //            HttpHeaders headers = new HttpHeaders();
    //            headers.set("Access-Control-Allow-Origin", "*");
    //
    //            MediaType contentType = entity.getHeaders().getContentType();
    //            if(contentType == null)
    //            {
    //                contentType = MediaType.APPLICATION_OCTET_STREAM;
    //            }
    //
    //            headers.setContentType(contentType);
    //            log.error(entity);
    //            return new ResponseEntity<>(entity.getBody(), headers, HttpStatus.OK);
    //        }).onErrorResume(e -> {
    //            e.printStackTrace();
    //            return Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null));
    //        });
    //    }
}
