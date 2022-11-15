package com.example.demoback.baseModule.system.vo;

import lombok.Data;

import java.util.List;

@Data
public class RootMenu {

    private String name="root";

    private String path="/";

    private String component="Layout";

    private List<MenuVo> children;

}
