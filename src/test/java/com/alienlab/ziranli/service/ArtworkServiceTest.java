package com.alienlab.ziranli.service;

import com.alienlab.ziranli.ZiranliserverApp;
import com.alienlab.ziranli.domain.Artwork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by 鸠小浅 on 2017/6/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZiranliserverApp.class)
public class ArtworkServiceTest {
    @Autowired
    private ArtworkService artworkService;
    @Test
    public void TestGetAllType(){
        List<String> types = artworkService.getAllType();
        for (String type : types){
            System.out.println(type);
        }
    }
//    public void TestGetAll(){
//        List<Artwork> artworks = artworkService.getAll();
//        for (Artwork artwork:artworks){
//            System.out.println(artwork);
//        }
//    }
}
