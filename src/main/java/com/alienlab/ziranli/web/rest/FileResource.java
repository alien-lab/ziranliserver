package com.alienlab.ziranli.web.rest;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.ziranli.web.rest.util.ExecResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by 橘 on 2017/6/2.
 */
@RestController
@RequestMapping("/api")
public class FileResource {
    @Value("${alienlab.upload.path}")
    String upload_path;
    @Value("${alienlab.image.path}")
    String image_path;

    @PostMapping("/image/upload")
    public ResponseEntity uploadImage(@RequestPart("file") MultipartFile file, HttpServletRequest request){
        String path=request.getSession().getServletContext().getRealPath(upload_path);
        System.out.println(path);
        BufferedOutputStream stream = null;
        if (!file.isEmpty()) {
            String fName = file.getOriginalFilename();
            String exName = fName.substring(fName.indexOf('.') + 1);
            String fileName = UUID.randomUUID().toString();
            try {
                byte[] bytes = file.getBytes();

                stream = new BufferedOutputStream(new FileOutputStream(new File(path + File.separator + fileName + "." + exName)));
                stream.write(bytes);
                stream.close();
                String s=image_path+fileName + "." + exName;
                JSONObject result=new JSONObject();
                result.put("path",s);
                JSONArray array=new JSONArray();
                array.add(result);
                return ResponseEntity.ok(array);
            } catch (Exception e) {
                stream = null;
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
            }
        }else{
            ExecResult er=new ExecResult(false,"没有上传文件.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

    }

}
